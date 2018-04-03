package bitshares1_decode.operations;


import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedLong;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import bitshares1_decode.*;


public class WithdrawOperation extends OperationBase {
	
    public static final String BALANCE_ID = "balance_id";
    public static final String AMOUNT = "amount";
    public static final String CLAIM_INPUT_DATA = "claim_input_data";
    
    
    public Address balance_id;
    public Long amount;
    public List<Character> claim_input_data;
    
    public WithdrawOperation() {
    	this.balance_id = new Address();
    	this.claim_input_data = new LinkedList<Character>();
    	this.claim_input_data.add(new Character('a'));
    	this.type = OperationType.withdraw_op_type.getValue();
    }
    
    
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(BALANCE_ID));
			fields.add(this.getClass().getDeclaredField(AMOUNT));	
			fields.add(this.getClass().getDeclaredField(CLAIM_INPUT_DATA));	
			
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
