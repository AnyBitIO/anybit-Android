package bitshares1_decode;

import java.io.ByteArrayOutputStream;

import java.io.DataOutput;
import java.io.DataOutputStream;
import com.google.common.primitives.UnsignedLong;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedInteger;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



/**
 * @author sunny
 *
 */
/**
 * @author sunny
 *
 */
/**
 * @author sunny
 *
 */
public class ByteHandle {

	/*public static byte[] pack(IValueEnum input) {
		Long tmp_lng = (long) input.getValue();
		return pack(tmp_lng);
	}

	public static <T extends IValueEnum> OperationType unpack(InputStream stream, OperationType type) {
		byte[] tmp_bytes;

		try {
			tmp_bytes = new byte[Long.SIZE/8];
			stream.read(tmp_bytes);
			System.out.println("unpack data:"+Util.restoreLong(tmp_bytes));
			type.setValue(Util.restoreLong(tmp_bytes));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;

	}*/
	
	
	/**
	 * object pack
	 * @param input
	 * @return
	 */
	public static <T extends ByteSerializable> byte[] pack(T input) {
		Field[] fields =  input.getOrderedFieldName();
		byte[] resultBytes ;
		resultBytes = new byte[0];
		for(Field one_field : fields)
		{
			System.out.println("type: "+one_field);
			try {
				one_field.setAccessible(true);  
				System.out.println("value: "+ one_field.get(input));

				resultBytes = Bytes.concat(resultBytes,ByteHandle.pack(one_field.get(input), one_field.get(input).getClass()));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultBytes;
	}
	
	
	
	public static <T> T unpack(InputStream input, Class<T> cls, int length) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, SecurityException, NoSuchMethodException, InvocationTargetException {
		Object[] obj = new Object[length];
		if(cls.isArray()  ) {
			//&& char.class.isAssignableFrom(cls.getComponentType())
			Class elementType = cls.getComponentType();
			
			for(int i =0;i<Array.getLength(obj);++i) {
				Array.set(obj, i, unpack(input,elementType));
			}
		}
		return (T)obj;
	}
	
	public static <T> T unpack(InputStream input, Class<T> cls) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
	{
		
		if(ByteSerializable.class.isAssignableFrom(cls)) {
			Object obj=  cls.newInstance();
			ByteSerializable byteSerializable = (ByteSerializable)obj;
			Field[] fields =  byteSerializable.getOrderedFieldName();
			for(Field oneField : fields){
				oneField.setAccessible(true); 
				if(oneField.getType().isArray())
				{
					Field lenField = cls.getDeclaredField(oneField.getName()+"Size");
					Object fieldValue =  Array.newInstance(oneField.getType().getComponentType(), (Integer)lenField.get(obj));
					System.out.println("Array Length: "+ lenField.get(obj));
					fieldValue = unpack(input,oneField.getType(),(Integer)lenField.get(obj));
					
					Method method = cls.getDeclaredMethod("set"+oneField.getName()+"Array" ,new Class[] {Object[].class});
					method.invoke(obj, fieldValue);
				}
				else if(Optional.class.isAssignableFrom(oneField.getType()))
				{
					Object fieldValue;
					byte first = (byte)input.read();
					if(first == 1)
					{
						fieldValue = new Optional<Object>( unpack(input,((Optional<Object>)oneField.get(obj)).getOptionalField().getClass()));	
					}
					else
					{
						fieldValue = new Optional<Object>(null);
					}
					
					oneField.set(obj, fieldValue);
				}
				else if (List.class.isAssignableFrom(oneField.getType())) {
					
					List<Object> fieldValue = new LinkedList<Object>();
					CompressUnsignedInt size = new CompressUnsignedInt();
					size = unpack(input,size);
					for(int i =0;i<size.value.longValue();++i)
					{
						fieldValue.add(unpack(input,((List<Object>)oneField.get(obj)).get(0).getClass()));
					}
					oneField.set(obj, fieldValue);
				}
				else if (Set.class.isAssignableFrom(oneField.getType())) {
					Set<Object> fieldValue = new HashSet<Object>();
					CompressUnsignedInt size = new CompressUnsignedInt();
					size = unpack(input,size);
					for(int i =0;i<size.value.longValue();++i)
					{
						fieldValue.add(unpack(input,((Set<Object>)oneField.get(obj)).toArray()[0].getClass()));
					}
					oneField.set(obj, fieldValue);
				}
				else if(Multimap.class.isAssignableFrom(oneField.getType())) {
					Multimap<Object,Object> fieldValue = ArrayListMultimap.create();
					CompressUnsignedInt size = new CompressUnsignedInt();
					size = unpack(input,size);
					for(int i =0;i<size.value.longValue();++i) {
						Object key = unpack(input,(((Multimap<Object,Object>)oneField.get(obj)).keySet().toArray()[0].getClass()));
						Object value = unpack(input,(((Multimap<Object,Object>)oneField.get(obj)).values().toArray()[0].getClass()));
						fieldValue.put(key, value);
					}
					oneField.set(obj, fieldValue);
				}
				//Todo: Map unpack handle
				else if(Map.class.isAssignableFrom(oneField.getType())) {
					Map<Object,Object> fieldValue = new TreeMap<Object,Object>();
					CompressUnsignedInt size = new CompressUnsignedInt();
					size = unpack(input,size);
					for(int i =0;i<size.value.longValue();++i) {
						Object key = unpack(input,(((Map<Object,Object>)oneField.get(obj)).keySet().toArray()[0].getClass()));
						Object value = unpack(input,(((Map<Object,Object>)oneField.get(obj)).values().toArray()[0].getClass()));
						fieldValue.put(key, value);
						
					}
					oneField.set(obj, fieldValue);
				}
				//Todo: MultiMap unpack handle
				
				else
				{
					Object fieldValue;
					fieldValue= unpack(input, oneField.getType());
					 
					oneField.set(obj,fieldValue);
				}
				 
				
			}
			return (T)obj;
		}
		else if(Long.class.isAssignableFrom(cls))
		{
			Long obj = unpack(input,new Long(0));
			return (T)obj;
		}
		else if(Integer.class.isAssignableFrom(cls))
		{
			Integer obj = unpack(input,new Integer(0));
			return (T)obj;
		}
		else if(UnsignedInteger.class.isAssignableFrom(cls))
		{
			UnsignedInteger obj = unpack(input,UnsignedInteger.fromIntBits(0));
			return (T)obj;
		}
		else if(UnsignedLong.class.isAssignableFrom(cls))
		{
			UnsignedLong obj = unpack(input,UnsignedLong.fromLongBits(0));
			return (T)obj;
		}		
		else if(String.class.isAssignableFrom(cls))
		{
			String obj = unpack(input,new String(""));
			return (T)obj;
		}
		
		else if(Character.class.isAssignableFrom(cls))
		{
			Character obj  = unpack(input, new Character('a'));
			return (T)obj;
		}
		else if(Byte.class.isAssignableFrom(cls)) {
			Byte obj = unpack(input,new Byte((byte) 0));
			return (T)obj;
		}
		else if(CompressSignedInt.class.isAssignableFrom(cls)) {
			CompressSignedInt obj = unpack(input,new CompressSignedInt(0));
			return (T)obj;
		}
		else if(CompressUnsignedInt.class.isAssignableFrom(cls)) {
			CompressUnsignedInt obj = unpack(input,new CompressUnsignedInt(0));
			return (T)obj;
		}
		
		
		
		Object nullObj=  cls.newInstance();
		return (T) nullObj;
	}
	
	
	/**
	 * 将反射出的变量解析成具体类型进行序列化
	 * @param input
	 * @param type
	 * @return
	 */
	public static byte[] pack(Object input,Class<?> type) {
		if(ByteSerializable.class.isAssignableFrom(type)) {
			return pack((ByteSerializable)input);
		}
		if(Long.class.isAssignableFrom(type))
		{
			return pack((Long)input);
		}
		if(Integer.class.isAssignableFrom(type))
		{
			return pack((Integer)input);
		}
		if(UnsignedInteger.class.isAssignableFrom(type))
		{
			return pack((UnsignedInteger)input);
		}
		if(UnsignedLong.class.isAssignableFrom(type))
		{
			return pack((UnsignedLong)input);
		}
		if(String.class.isAssignableFrom(type))
		{
			return pack((String)input);
		}
		if(char[].class.isAssignableFrom(type)) {
			return pack((char[])input);
		}
		if(Character.class.isAssignableFrom(type)) {
			return pack((Character)input);
		}
		if(type.isArray())
		{
			byte[] resultBytes = new byte[0];
			for(int i =0;i<Array.getLength(input);++i)
			{
				resultBytes = Bytes.concat(resultBytes,pack(Array.get(input, i),type.getComponentType()));
			}
			return resultBytes;
		}
		if(Optional.class.isAssignableFrom(type)){
			
			return pack((Optional<Object>)input);
		}
		if(List.class.isAssignableFrom(type)) {
			return pack((List<Object>)input);
		}
		if(Set.class.isAssignableFrom(type)) {
			return pack((Set<Object>)input);
		}
		if(Multimap.class.isAssignableFrom(type)) {
			return pack((Multimap<Object,Object>)input);
		}
		if(Map.class.isAssignableFrom(type)) {
			return pack((Map<Object,Object>)input);
		}
		if(Byte.class.isAssignableFrom(type)) {
			return pack((Byte)input);
		}
		if(CompressSignedInt.class.isAssignableFrom(type)) {
			return pack((CompressSignedInt)input);
			
		}
		if(CompressUnsignedInt.class.isAssignableFrom(type)) {
			return pack((CompressUnsignedInt)input);
		}
		
		return new byte[0];
		
	}
	
	
	
	
	
	
	
	
	/**
	 * integer pack
	 * @param input
	 * @return
	 */
	public static byte[] pack(Integer input)
	{
		return Util.revertInteger((Integer)input);
	}
	
	/**
	 * Long pack
	 * @param input
	 * @return
	 */
	public static byte[] pack(Long input) {
		return Util.revertLong(input);
	}
	
	/**
	 * AtomicLong pack
	 * @param input
	 * @return
	 */
	public static byte[] pack(AtomicLong input) {
		return pack(input.get());
	}

	/**
	 * UnsignedInteger pack
	 * @param input
	 * @return
	 */
	public static byte[] pack(UnsignedInteger input) {
		return Util.revertInteger(input.intValue());
	}

	
	/**
	 * UnsignedLong pack
	 * @param input
	 * @return
	 */
	public static byte[] pack(UnsignedLong input) {
		return Util.revertLong(input.longValue());
	}

	/**
	 * Integer unpack
	 * @param stream
	 * @param output
	 * @return
	 */
	public static Integer unpack(InputStream stream, Integer output) {

		try {
			byte[] tmp_bytes;
			tmp_bytes = new byte[Integer.SIZE/8];
			stream.read(tmp_bytes);
			return Util.restoreInteger(tmp_bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0 ;
		}
	}

	/**
	 * Long unpack
	 * @param stream
	 * @param output
	 * @return
	 */
	public static Long unpack(InputStream stream, Long output) {

		try {
			byte[] tmp_bytes;
			tmp_bytes = new byte[Long.SIZE/8];
			stream.read(tmp_bytes);
			output = Util.restoreLong(tmp_bytes);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * UnsignedInteger unpack
	 * @param stream
	 * @param output
	 * @return
	 */
	public static UnsignedInteger unpack(InputStream stream, UnsignedInteger output) {
		Integer temp_int;
		temp_int = new Integer(0);
		temp_int = unpack(stream, temp_int);
		output = UnsignedInteger.fromIntBits(temp_int);
		return output;
	}

	/**
	 * UnsignedLong unpack
	 * @param stream
	 * @param output
	 * @return
	 */
	public static UnsignedLong unpack(InputStream stream, UnsignedLong output) {
		Long temp_lng;
		temp_lng = new Long(0);
		temp_lng = unpack(stream, temp_lng);
		output = UnsignedLong.fromLongBits(temp_lng);
		return output;
	}
	
	public static byte[] pack(CompressSignedInt input) {
		
		List<Byte> arrayList = new LinkedList<Byte>();
		UnsignedInteger val  = UnsignedInteger.fromIntBits((input.value<<1) ^ (input.value>>31));
	    do {
	        byte b = (byte)(val.byteValue() & 0x7f);
	        val = val.fromIntBits(val.intValue() >> 7);
	        if(val.intValue() > 0)
	        	b |= ((byte)(1) << 7);
	        else
	        	b |= ((byte)(0) << 7);
	        arrayList.add(b);
	    } while( val.longValue()>0 );
		return Bytes.toArray(arrayList);
	}
	public static CompressSignedInt unpack(InputStream stream,CompressSignedInt output) throws IOException {
		UnsignedInteger v = UnsignedInteger.fromIntBits(0);
		byte b = 0;
		int by = 0;
	    do {
	    	b = (byte)stream.read();
	        v = UnsignedInteger.fromIntBits((int)(v.longValue() | (UnsignedInteger.fromIntBits(b & 0x7f).longValue() << by)));
	        by += 7;
	      } while( (b & 0x80) >0 );
	    output.value = ((v.intValue()>>1) ^ (v.intValue()>>31)) + (v.intValue()&0x01);
	    output.value = (v.intValue()&0x01) >0 ? output.value : -output.value;
	    output.value = -output.value;
		return output;
	}
	
	
	public static byte[] pack(CompressUnsignedInt input) {
		
		List<Byte> arrayList = new LinkedList<Byte>();
		
		UnsignedLong val  = UnsignedLong.fromLongBits(input.value.longValue());

	      do {
	        byte b = (byte)(val.byteValue() & 0x7f);
	        val  = UnsignedLong.fromLongBits(val.longValue()>> 7);
	        if(val.longValue() > 0)
	        {
	        	b |= ((byte)(1) << 7);
	        }
	        else
	        {
	        	b |= ((byte)(0) << 7);
	        }
	        arrayList.add(b);
	      }while( val.longValue()>0 );
		return Bytes.toArray(arrayList);
	}
	public static CompressUnsignedInt unpack(InputStream stream,CompressUnsignedInt output) throws IOException {
		UnsignedLong v = UnsignedLong.fromLongBits(0);
		byte b = 0;
		byte by = 0;
	      do {
	    	  b = (byte)stream.read();
	          v  = UnsignedLong.fromLongBits( (int)(v.longValue() | (UnsignedInteger.fromIntBits(b & 0x7f).longValue() << by))   ); 
	          by += 7;
	      } while( (b & 0x80) >0  );
	      output.value = UnsignedInteger.fromIntBits( v.intValue());
		
		return output;
	}
	
	
	
	public static byte[] pack(String input) {
		byte[] lenSize = pack( new CompressUnsignedInt(input.length()));    
		if(input.length() >0)
		{
			return Bytes.concat(lenSize,input.getBytes());
		}
		else
		{
			return lenSize;
		}
	}
	
	public static String unpack(InputStream stream, String output) throws IOException {
		CompressUnsignedInt len = new CompressUnsignedInt(0);
		len = unpack(stream,len);
		byte[] allStringData = new byte[len.value.intValue()];
		stream.read(allStringData, 0, len.value.intValue());
		return new String(allStringData);
	}
	
	public static byte[] pack(char[] input)
	{
		int len=input.length;
		byte [] arr=new byte[len];
		for(int i=0; i<len; i++){
		  arr[i] = (byte)input[i];
		}
		return arr;
	}
	
	public static char[] unpack(InputStream stream,char[] output,int length) throws IOException
	{
		
		int len=length;
		char [] arr=new char[len];
		for(int i =0;i<len;++i)
		{
			arr[i] = (char)stream.read();
		}
		return arr;
	}
	
	public static byte[] pack(Character input)
	{
		byte [] arr=new byte[1];
		arr[0] = (byte)input.charValue();
		return arr;
	}
	
	public static char unpack(InputStream stream,Character output) throws IOException
	{
		output = (char)stream.read();
		return output;
	}
	
	public static <T> byte[] pack(Optional<T> input){
		if(input.isSet())
		{
			return Bytes.concat(new byte[]{1},pack(input.getOptionalField(),input.getOptionalField().getClass()));
		}
		return new byte[]{0};
	}
	/*public static <T> Optional<T> unpack(InputStream stream,Optional<T> output) throws IOException, InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		byte first = (byte)stream.read();
		if(first == 1)
		{
			
			output =new Optional<T>((T)unpack(stream,output.getOptionalField().getClass()));
			return output;
		}
		else {
			return new Optional<T>(null);
		}
	}*/
	
	public static byte[] pack(List<Object> input)
	{
		CompressUnsignedInt size = new CompressUnsignedInt(input.size());
		byte[] resultBytes = new byte[0];
		if(size.value.intValue()>0) {
			resultBytes = Bytes.concat(resultBytes,pack(size));
			for( int i = 0;i<size.value.longValue();++i) {
				resultBytes = Bytes.concat(resultBytes,pack(input.get(i),input.get(i).getClass()));
			}
		}
		else {
			return pack(size);
		}
		return resultBytes;
		
	}
	
	
	public static byte[] pack(Set<Object> input) {
		byte[] resultBytes = new byte[0];
		CompressUnsignedInt size = new CompressUnsignedInt(input.size());
		resultBytes = Bytes.concat(resultBytes, pack(size));
		for (Object one_data : input) {  
			resultBytes = Bytes.concat(resultBytes,pack(one_data,one_data.getClass()));
		}  
		return resultBytes;
		
	}
	public static byte[] pack(Map<Object,Object> input) {
		byte[] resultBytes = new byte[0];
		CompressUnsignedInt size = new CompressUnsignedInt(input.size());
		resultBytes = Bytes.concat(resultBytes, pack(size));
		for(Entry<Object,Object > iter : input.entrySet()) {
			resultBytes = Bytes.concat(resultBytes,pack(iter.getKey(),iter.getKey().getClass()));
			resultBytes = Bytes.concat(resultBytes,pack(iter.getValue(),iter.getValue().getClass()));
		}
		return resultBytes;
	}
	
	public static byte[] pack(Multimap<Object,Object> input) {
		byte[] resultBytes = new byte[0];
		CompressUnsignedInt size = new CompressUnsignedInt(input.size());
		resultBytes = Bytes.concat(resultBytes, pack(size)); 
		
		for(Entry<Object, Object> iter : input.entries()) {
			resultBytes = Bytes.concat(resultBytes,pack(iter.getKey(),iter.getKey().getClass()));
			resultBytes = Bytes.concat(resultBytes,pack(iter.getValue(),iter.getValue().getClass()));
		}
		return resultBytes;
	}
	public static byte[] pack(Byte input) {
		return new byte[]{input.byteValue()};
	}
	public static Byte unpack(InputStream stream,Byte output) throws IOException {
		output = (byte) stream.read();
		return output;
	}
	

}
