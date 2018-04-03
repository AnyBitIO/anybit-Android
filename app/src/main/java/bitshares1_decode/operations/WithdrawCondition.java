package bitshares1_decode.operations;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedLong;

import bitshares1_decode.ByteHandle;
import bitshares1_decode.ByteSerializable;
import bitshares1_decode.CompressUnsignedInt;

public class WithdrawCondition extends ByteSerializable {
	public static final String ASSET_ID = "asset_id";
    public static final String SLATE_ID = "slate_id";
    public static final String TYPE = "type";
    public static final String BALANCE_TYPE = "balance_type";
    public static final String DATA = "data";
	
	
	
	CompressUnsignedInt asset_id;
	UnsignedLong slate_id;
	Byte type;
	Byte balance_type;
	List<Byte> data;
	
	public WithdrawCondition() {
		asset_id = new CompressUnsignedInt(0);
		slate_id = UnsignedLong.fromLongBits(0);
		type = 0;
		balance_type =0;
		
		
		data = new LinkedList<Byte>();
		this.data.add(new Byte((byte)0));
	}
	

	public <T extends WithdrawType> WithdrawCondition(T input){
		
		this.asset_id = new CompressUnsignedInt(0);
		this.slate_id = UnsignedLong.fromLongBits(0);
		
		this.balance_type =0;
		
	
		this.fromData(input);
	}
	
	public <T extends WithdrawType> void fromData(T input){
		this.type = input.type;
		this.data = Bytes.asList(ByteHandle.pack(input,input.getClass()));
	}
	
	public <T extends WithdrawType> T toData() throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IOException{
		if(this.type == 1) {
			WithdrawWithSignature res = new WithdrawWithSignature();
			InputStream stream = new ByteArrayInputStream(Bytes.toArray(this.data));
			res = ByteHandle.unpack(stream,res.getClass());
			return (T) res;
		}
		if(this.type ==3) {
			WithdrawWithMultisig res = new WithdrawWithMultisig();
			InputStream stream = new ByteArrayInputStream(Bytes.toArray(this.data));
			res = ByteHandle.unpack(stream,res.getClass());
			return (T) res;
		}
		return null;
	}
	
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(ASSET_ID));
			fields.add(this.getClass().getDeclaredField(SLATE_ID));	
			fields.add(this.getClass().getDeclaredField(TYPE));
			fields.add(this.getClass().getDeclaredField(BALANCE_TYPE));	
			fields.add(this.getClass().getDeclaredField(DATA));
			
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
