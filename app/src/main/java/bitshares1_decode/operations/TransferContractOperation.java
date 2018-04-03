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


public class TransferContractOperation extends OperationBase {

    public static final String FROM = "from";
	public static final String COSTLIMIT = "costlimit";
	public static final String TRANSACTION_FEE = "transaction_fee";
	public static final String TRANSFER_AMOUNT = "transfer_amount";
	public static final String BALANCES = "balances";
	public static final String CONTRACT_ID = "contract_id";


	public PublicKeyType from;
	public Asset costlimit;
	public Asset transaction_fee;
	public Asset transfer_amount;

	public Map<Address, Long> balances;
	public Address contract_id;



    public TransferContractOperation() {
    	this.from = new PublicKeyType();
		this.contract_id = new Address();
		this.transaction_fee = new Asset();
		this.balances = new TreeMap<Address,Long>();
		this.balances.put(new Address(),(long)0);
		this.costlimit = new Asset();
		this.transfer_amount = new Asset();
    	this.type = OperationType.transfer_contract_op_type.getValue();
    }
    
    
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(FROM));

			fields.add(this.getClass().getDeclaredField(COSTLIMIT));
			fields.add(this.getClass().getDeclaredField(TRANSACTION_FEE));
			fields.add(this.getClass().getDeclaredField(TRANSFER_AMOUNT));
			fields.add(this.getClass().getDeclaredField(BALANCES));
			fields.add(this.getClass().getDeclaredField(CONTRACT_ID));
			
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
