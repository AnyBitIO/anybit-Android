package bitshares1_decode.operations;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.bitcoinj.core.ECKey;

import bitshares1_decode.Address;
import bitshares1_decode.Optional;

public class WithdrawWithSignature extends WithdrawType {
	public static final String OWNER = "owner";
    public static final String MEMO = "memo";
	public Address owner;
	public Optional<TransferMemo> memo;

	public WithdrawWithSignature() {
		this.type = 1;
		owner = new Address();
		memo = new Optional<TransferMemo>(new TransferMemo());
	}
	
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(OWNER));
			fields.add(this.getClass().getDeclaredField(MEMO));
			
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
