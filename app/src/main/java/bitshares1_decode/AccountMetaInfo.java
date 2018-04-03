package bitshares1_decode;

import bitshares1_decode.operations.WithdrawType;
import com.google.common.primitives.Bytes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class AccountMetaInfo extends ByteSerializable {
    public static final String TYPE = "type";
    public static final String DATA = "data";

    public CompressUnsignedInt type;
    List<Byte> data;
    AccountMetaInfo(){
        this.type = new CompressUnsignedInt(1);
        this.data = new LinkedList<Byte>();
        this.data.add((byte)0);
    }

    public void fromData(MultisigMetaInfo input){
        this.type = new CompressUnsignedInt(2);
        this.data = Bytes.asList(ByteHandle.pack(input,input.getClass()));
    }

    public MultisigMetaInfo toData() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException, NoSuchMethodException, IOException {
        if(this.type == new CompressUnsignedInt(2)) {
            MultisigMetaInfo res = new MultisigMetaInfo();
            InputStream stream = new ByteArrayInputStream(Bytes.toArray(this.data));
            res = ByteHandle.unpack(stream,res.getClass());
            return (MultisigMetaInfo) res;
        }
        return null;
    }

    @Override
    public Field[] getOrderedFieldName() {
        List<Field> fields;
        fields = new LinkedList<Field>();
        try {
            fields.add(this.getClass().getDeclaredField(TYPE));
            fields.add(this.getClass().getDeclaredField(DATA));

        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Field[] ret_fields = new Field[fields.size()];
        fields.toArray(ret_fields);
        return ret_fields;
    }
}