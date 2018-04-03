package bitshares1_decode.operations;

import bitshares1_decode.CompressSignedInt;
import bitshares1_decode.OperationBase;
import bitshares1_decode.OperationType;
import bitshares1_decode.Optional;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class UpdateAccountOperation extends OperationBase {

    public static final String ACCOUNT_ID = "account_id";
    public static final String PUBLIC_DATA = "public_data";
    public static final String ACTIVE_KEY = "active_key";
    public static final String DELEGATE_PAY_RATE = "delegate_pay_rate";


    public CompressSignedInt account_id;
    //ToDo: variant
//public_data
    public Optional<PublicKeyType> active_key;
    public Byte delegate_pay_rate;
    //ToDo: AccountMetaInfo
    //Optional<AccountMetaInfo> meta_data;

    public UpdateAccountOperation(){
        this.type = OperationType.update_account_op_type.getValue();
        this.active_key = new Optional<PublicKeyType>(new PublicKeyType());
    }


    @Override
    public Field[] getOrderedFieldName() {
        List<Field> fields;
        fields = new LinkedList<Field>();
        try {
            fields.add(this.getClass().getDeclaredField(ACCOUNT_ID));
            fields.add(this.getClass().getDeclaredField(PUBLIC_DATA));
            fields.add(this.getClass().getDeclaredField(ACTIVE_KEY));
            fields.add(this.getClass().getDeclaredField(DELEGATE_PAY_RATE));

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
