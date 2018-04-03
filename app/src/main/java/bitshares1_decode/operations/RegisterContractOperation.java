package bitshares1_decode.operations;


import bitshares1_decode.*;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class RegisterContractOperation extends OperationBase {

    public static final String CONTRACT_CODE = "contract_code";
	public static final String INITCOST = "initcost";
	public static final String TRANSACTIONFEE = "transaction_fee";
	public static final String BALANCES = "balances";
	public static final String OWNER = "owner";
	public static final String REGISTER_TIME = "register_time";

    public Code contract_code;
	Asset initcost;
	Asset transaction_fee;
	Map<Address,Long> balances;
	PublicKeyType owner;
	Long register_time;
    public RegisterContractOperation() {
    	this.contract_code = new Code();
		this.initcost = new Asset();
		this.transaction_fee = new Asset();
		this.balances = new TreeMap<Address,Long>();
		this.balances.put(new Address(),(long)0);
		this.owner = new PublicKeyType();
		this.register_time = (long)0;

    	this.type = OperationType.contract_register_op_type.getValue();
    }
    
    
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(CONTRACT_CODE));
			fields.add(this.getClass().getDeclaredField(INITCOST));
			fields.add(this.getClass().getDeclaredField(TRANSACTIONFEE));
			fields.add(this.getClass().getDeclaredField(BALANCES));
			fields.add(this.getClass().getDeclaredField(OWNER));
			fields.add(this.getClass().getDeclaredField(REGISTER_TIME));
			
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
