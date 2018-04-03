package bitshares1_decode.operations;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import bitshares1_decode.ByteSerializable;

public class TransferMemo extends ByteSerializable{
	public static final String ONE_TIME_KEY = "one_time_key";
    public static final String ENCRYPTED_MEMO_DATA = "encrypted_memo_data";
    PublicKeyType one_time_key;
	List<Byte> encrypted_memo_data;
	
	public TransferMemo() {
		one_time_key = new PublicKeyType();
		encrypted_memo_data = new LinkedList<Byte>();
		encrypted_memo_data.add(new Byte((byte) 0));
	}
	
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(ONE_TIME_KEY));
			fields.add(this.getClass().getDeclaredField(ENCRYPTED_MEMO_DATA));
			
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
