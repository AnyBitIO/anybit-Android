package bitshares1_decode.operations;


import bitshares1_decode.Address;
import bitshares1_decode.CompressSignedInt;
import bitshares1_decode.OperationBase;
import bitshares1_decode.OperationType;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;


public class WithdrawPayOperation extends OperationBase {

    public static final String AMOUNT = "amount";
    public static final String ACCOUNT_ID = "account_id";


    public Long amount;
    public CompressSignedInt account_id;

    public WithdrawPayOperation() {
    	this.amount = (long)0;
		account_id = new CompressSignedInt(0);
    	this.type = OperationType.withdraw_pay_op_type.getValue();
    }
    
    
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(AMOUNT));	
			fields.add(this.getClass().getDeclaredField(ACCOUNT_ID));
			
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
