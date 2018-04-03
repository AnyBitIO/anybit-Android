package bitshares1_decode;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class Asset extends ByteSerializable {
	public static final String AMOUNT = "amount";
	public static final String AMOUNTID = "amountId";
	public Long amount;
	public CompressSignedInt amountId;
	
	public Asset(){
		this.amount = (long)0;
		this.amountId = new CompressSignedInt(0);
	}
	Asset(long amount){
		this.amount = amount;
		this.amountId = new CompressSignedInt(0);
	}
	Asset(long amount,Integer amountId){
		this.amount = amount;
		this.amountId = new CompressSignedInt(amountId);
	}
	Asset(long amount,CompressSignedInt amountId){
		this.amount = amount;
		this.amountId = amountId;
	}
	
	@Override
	public Field[]  getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(AMOUNT));
			fields.add(this.getClass().getDeclaredField(AMOUNTID));		
			
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
