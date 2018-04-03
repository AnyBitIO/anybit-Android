package bitshares1_decode.operations;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import bitshares1_decode.OperationBase;
import bitshares1_decode.OperationType;

public class DepositOperation extends OperationBase{
	public static final String CONDITION = "condition";
    public static final String AMOUNT = "amount";
    
    
	public Long amount;
	public WithdrawCondition condition;
	
	public DepositOperation(){
		this.type = OperationType.deposit_op_type.getValue();
		amount =(long)0;
		condition = new WithdrawCondition();
		
	}
	
	
	
	
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(AMOUNT));
			fields.add(this.getClass().getDeclaredField(CONDITION));	
			
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
