package bitshares1_decode.operations;


import bitshares1_decode.*;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class UpgradeContractOperation extends OperationBase {

    public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESC = "desc";
	public static final String EXEC_LIMIT = "exec_limit";
	public static final String TRANSACTION_FEE = "transaction_fee";
	public static final String BALANCES = "balances";

    public Address id;
	public String name;
	public String desc;
	public Asset exec_limit;
	public Asset transaction_fee;
	Map<Address,Long> balances;

    public UpgradeContractOperation() {
    	this.id = new Address();
		this.name = "";
		this.desc = "";
		this.exec_limit = new Asset();
		this.transaction_fee = new Asset();
		this.balances = new TreeMap<Address,Long>();
		this.balances.put(new Address(),(long)0);


    	this.type = OperationType.contract_upgrade_op_type.getValue();
    }
    
    
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(ID));
			fields.add(this.getClass().getDeclaredField(NAME));
			fields.add(this.getClass().getDeclaredField(DESC));
			fields.add(this.getClass().getDeclaredField(EXEC_LIMIT));
			fields.add(this.getClass().getDeclaredField(TRANSACTION_FEE));
			fields.add(this.getClass().getDeclaredField(BALANCES));
			
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
