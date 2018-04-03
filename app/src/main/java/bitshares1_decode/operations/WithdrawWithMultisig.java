package bitshares1_decode.operations;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.primitives.UnsignedInteger;

import bitshares1_decode.Address;
import bitshares1_decode.Optional;

public class WithdrawWithMultisig extends WithdrawType {
	public static final String REQUIRED = "required";
	public static final String OWNERS = "owners";
	public static final String MEMO = "memo";
	UnsignedInteger required;
	Set<Address>    owners;
	Optional<TransferMemo> memo;
	
	public WithdrawWithMultisig() {
		this.type = 3;
		this.required = UnsignedInteger.fromIntBits(0);
		this.owners = new HashSet<Address>();
		this.memo  = new Optional<TransferMemo>(new TransferMemo());	
		
	}
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(REQUIRED));
			fields.add(this.getClass().getDeclaredField(OWNERS));
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
