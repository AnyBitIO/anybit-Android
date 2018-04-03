package bitshares1_decode;

import com.google.common.primitives.UnsignedInteger;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MultisigMetaInfo extends ByteSerializable {
    public static final String REQUIRED = "required";
    public static final String OWNERS = "owners";
    public UnsignedInteger required;
    public Set<Address> owners;

    public MultisigMetaInfo(){
        required = UnsignedInteger.fromIntBits(0);
        owners = new HashSet<Address>();
        owners.add(new Address());
    }

    @Override
    public Field[] getOrderedFieldName() {
        List<Field> fields;
        fields = new LinkedList<Field>();
        try {
            fields.add(this.getClass().getDeclaredField(REQUIRED));
            fields.add(this.getClass().getDeclaredField(OWNERS));

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
