package bitshares1_decode.operations;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import bitshares1_decode.ByteSerializable;

public class PublicKeyType extends ByteSerializable {
	public static final String KEY_DATA = "key_data";
	public Byte[] key_data;
	public static final int key_dataSize = 33;
	
	public void setkey_dataArray(Object[] data) {
		for(int i=0;i<key_dataSize;++i) {
			key_data[i] = (Byte)data[i];
		}
	}
	
	public PublicKeyType() {
		key_data = new Byte[key_dataSize];
	}
	
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(KEY_DATA));
			
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
