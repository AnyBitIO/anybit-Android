package bitshares1_decode.operations;

import bitshares1_decode.*;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedLong;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class RegisterAccountOperation extends OperationBase {

    public static final String NAME = "name";
    public static final String PUBLIC_DATA = "public_data";
    public static final String OWNER_KEY = "owner_key";
    public static final String ACTIVE_KEY = "active_key";

    public static final String DELEGATE_PAY_RATE = "delegate_pay_rate";
    public static final String META_DATA = "meta_data";


    public String name;
    //ToDo: variant
    VariantString public_data;
    public PublicKeyType owner_key;
    public PublicKeyType active_key;
    public Byte delegate_pay_rate;
    //ToDo: AccountMetaInfo
    //Optional<AccountMetaInfo> meta_data;

    public RegisterAccountOperation(){
        this.type = OperationType.register_account_op_type.getValue();
    }


    @Override
    public Field[] getOrderedFieldName() {
        List<Field> fields;
        fields = new LinkedList<Field>();
        try {
            fields.add(this.getClass().getDeclaredField(NAME));
            fields.add(this.getClass().getDeclaredField(PUBLIC_DATA));
            fields.add(this.getClass().getDeclaredField(OWNER_KEY));
            fields.add(this.getClass().getDeclaredField(ACTIVE_KEY));
            fields.add(this.getClass().getDeclaredField(DELEGATE_PAY_RATE));
            fields.add(this.getClass().getDeclaredField(META_DATA));

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
