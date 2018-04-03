package bitshares1_decode;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.google.common.primitives.UnsignedInteger;

public class Ripemd160 extends ByteSerializable {

	public static final String HASH = "hash";
	UnsignedInteger[] hash;
	public static final int hashSize =5;
	
	Ripemd160(){
		hash = new UnsignedInteger[hashSize];
		for( int i =0;i<hashSize;++i) {
			hash[i] = UnsignedInteger.fromIntBits(0);
		}
	}
	
	Ripemd160(Ripemd160 ripe){
		hash = new UnsignedInteger[hashSize];
		System.arraycopy(ripe.hash, 0, this.hash, 0, hashSize);
	}
	
	
	public void sethashArray(Object[] array) {
	    	for(int i =0;i<hashSize;++i){
	    		this.hash[i] = (UnsignedInteger)array[i];
	    	}
	    }
	
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(HASH));		
			
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
