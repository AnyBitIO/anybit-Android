package bitshares1_decode.operations;


import bitshares1_decode.Address;
import bitshares1_decode.Asset;
import bitshares1_decode.OperationBase;
import bitshares1_decode.OperationType;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class CallContractOperation extends OperationBase {

    public static final String CALLER = "caller";
	public static final String BALANCES = "balances";
	public static final String CONTRACT = "contract";
	public static final String COSTLIMIT = "costlimit";
	public static final String TRANSACTION_FEE = "transaction_fee";
	public static final String METHOD = "method";
	public static final String ARGS = "args";



	public PublicKeyType caller;
	public Map<Address, Long> balances;
	public Address contract;
	public Asset costlimit;
	public Asset transaction_fee;
	public String method;
	public String args;


    public CallContractOperation() {
    	this.caller = new PublicKeyType();
		this.contract = new Address();
		this.transaction_fee = new Asset();
		this.balances = new TreeMap<Address,Long>();
		this.balances.put(new Address(),(long)0);
		this.costlimit = new Asset();
		this.method = "";
		this.args = "";
    	this.type = OperationType.contract_call_op_type.getValue();
    }
    
    
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(CALLER));
			fields.add(this.getClass().getDeclaredField(BALANCES));
			fields.add(this.getClass().getDeclaredField(CONTRACT));
			fields.add(this.getClass().getDeclaredField(COSTLIMIT));
			fields.add(this.getClass().getDeclaredField(TRANSACTION_FEE));
			fields.add(this.getClass().getDeclaredField(METHOD));
			fields.add(this.getClass().getDeclaredField(ARGS));
			
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
