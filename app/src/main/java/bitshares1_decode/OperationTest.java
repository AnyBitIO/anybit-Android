package bitshares1_decode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import bitshares1_decode.interfaces.JsonSerializable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Bytes;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedList;
import bitshares1_decode.ByteHandle;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream ;
import java.io.ByteArrayOutputStream ;
/**
 * Created by sunny on 12/03/18.
 */
public class OperationTest extends ByteSerializable {

    public static final String TYPE = "type";
    public static final String DATA = "data";
    public static final String T1 = "t1";
    public static final String T2 = "t2";

    protected Byte type;
    protected List<Byte> data;
    Long t1;
    Integer t2;
    String testStr;
    Character[] testchararray ;
    List<Long> testlongList;
    Set<Long> testlongSet;
    Map<Integer,String> testMap;
    Multimap<Integer,String> testmultiMap;
    public static final int testchararraySize =5 ;
    
    Optional<Long> testoptlong;

    public void settestchararrayArray(Object[] array) {
    	for(int i =0;i<testchararraySize;++i){
    		this.testchararray[i] = (Character)array[i];
    	}
    }
    
    public OperationTest(Byte type){
        this.type = type;
        this.data = new LinkedList();
        this.t1 = (long)(12345678);
    	this.t2 = 1234;
    	this.testStr = "11231321312312aaaaa";
    	this.testchararray = new Character[5];
    	for(int i =0;i<testchararraySize;++i) {
    		this.testchararray[i] = (char)(byte)(55);
    	
    	}
    	this.testoptlong = new Optional<Long>((long)0);
    	this.testlongList = new LinkedList<Long>();
    	this.testlongList.add((long)1);
    	this.testlongSet = new HashSet<Long>();
    	this.testlongSet.add((long)1);
    	this.testMap = new TreeMap<Integer,String>();
    	this.testMap.put(12, "2sss");
    	this.testmultiMap = ArrayListMultimap.create();
    	this.testmultiMap.put(12, "2sss");
    	
    	
    }
    public OperationTest(){
        this.type = (byte) 0;
        this.data = new LinkedList();
        this.t1 = (long)(12345678);
    	this.t2 = 1234;
    	this.testStr = "12aaaaaaaaaa";
    	this.testchararray = new Character[5];
    	for(int i =0;i<testchararraySize;++i) {
    		this.testchararray[i] = (char)((byte)(85));
    
    	}
    	this.testoptlong = new Optional<Long>((long)0);
    	this.testlongList = new LinkedList<Long>();
    	this.testlongList.add((long)1);
    	this.testlongSet = new HashSet<Long>();
    	this.testlongSet.add((long)1);
    	this.testMap = new TreeMap<Integer,String>();
    	this.testMap.put(12, "2sss");
    	this.testmultiMap = ArrayListMultimap.create();
    	this.testmultiMap.put(12, "2sss");
    }

    
   /* @Override
    public byte[] toBytes() {
    	byte[] typeBytes = ByteHandle.pack(type);
    	byte[] t1Bytes = ByteHandle.pack(t1);
    	byte[] t2Bytes = ByteHandle.pack(t2);
    	return Bytes.concat(typeBytes,t1Bytes,t2Bytes);
		
	}
    @Override
    public void readBytes(InputStream input) {
    	//InputStream input = new ByteArrayInputStream(data);
    	this.type = ByteHandle.unpack(input,this.type);
		this.t1 = ByteHandle.unpack(input, this.t1);
		this.t2 = ByteHandle.unpack(input, this.t2);
		System.out.println("自定义的readObject");
	}*/
	
	@Override
	public  Field[]  getOrderedFieldName(){
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(TYPE));
			fields.add(this.getClass().getDeclaredField(T1));
			fields.add(this.getClass().getDeclaredField(T2));
			fields.add(this.getClass().getDeclaredField("testStr"));
			fields.add(this.getClass().getDeclaredField("testchararray"));
			fields.add(this.getClass().getDeclaredField("testoptlong"));
			fields.add(this.getClass().getDeclaredField("testlongList"));
			fields.add(this.getClass().getDeclaredField("testlongSet"));
			fields.add(this.getClass().getDeclaredField("testMap"));
			fields.add(this.getClass().getDeclaredField("testmultiMap"));
			
			
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
	
	 public static void main(String[]args) throws NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException  
	 {
		 OutputStream out;
		try {
			out = new FileOutputStream("data.out");
			OperationTest persons1=new OperationTest(OperationType.contract_call_op_type.getValue());
			 OperationTest persons2=new OperationTest(OperationType.withdraw_op_type.getValue());
			 persons1.t1 = (long)112;
			 persons2.t1 = (long)113;
			 persons1.testStr = "abcdabcdabcdabcdabcdabcdabcdabcdabcd";
			 persons1.testchararray[2] =13;
			 persons1.testoptlong.setOptionalField((long)111);
			 persons1.testlongList.clear();
			 persons1.testlongList.add((long)21112);
			 persons1.testlongList.add((long)2223);
			 persons1.testlongList.add((long)2223);
			 persons1.testlongList.add((long)2223);
			 persons1.testlongList.add((long)33333);
			 persons1.testlongList.add((long)2223);
			 persons1.testlongSet.clear();
			 persons1.testlongSet.add((long)2223);
			 persons1.testlongSet.add((long)3224);
			 persons1.testlongSet.add((long)5532);
			 persons1.testMap.clear();
			 persons1.testMap.put(1, "a");
			 persons1.testMap.put(2, "b");
			 persons1.testMap.put(3, "c");
			 persons1.testMap.put(4, "d");
			 persons1.testmultiMap = ArrayListMultimap.create();
			 persons1.testmultiMap.put(1, "c");
			 persons1.testmultiMap.put(1, "c");
			 persons1.testmultiMap.put(1, "c");
			 persons1.testmultiMap.put(1, "c");
			 persons1.testmultiMap.put(1, "c");
			 persons1.testmultiMap.put(2, "a");
			 
			 persons1.t2 = 1234566112;
			 persons2.t2 = 1234113;
			 persons2.testchararray[2] = 16;
			 persons2.testoptlong.setOptionalField(null);
			 
			 CompressSignedInt ttt = new CompressSignedInt(-210);
			 byte[] dd = ByteHandle.pack(ttt);
			 for(byte b: dd)
			 {
				 System.out.println("data:  "+(int)b);
			 }
			 InputStream as = new ByteArrayInputStream(dd);
			 CompressSignedInt tt1  = new CompressSignedInt(0);
			 tt1 = ByteHandle.unpack(as, tt1);
			 System.out.println("tt1::  "+ tt1.value);
			 
			 
			 
			 CompressUnsignedInt unttt = new CompressUnsignedInt(-2310);
			 byte[] undd = ByteHandle.pack(unttt);
			 for(byte b: undd)
			 {
				 System.out.println("data:  "+(int)b);
			 }
			 InputStream unas = new ByteArrayInputStream(undd);
			 CompressUnsignedInt untt1  = new CompressUnsignedInt(0);
			 untt1 = ByteHandle.unpack(unas, untt1);
			 System.out.println("untt1::  "+ untt1.value);
			 
			 String stringdata ="ssssaaaaaasssssaqqq222222222222222222222222222222222222222qqweeeewwwwwwww";
			 byte[] strData = ByteHandle.pack(stringdata);
			 String newStrData = "";
			 InputStream strStream = new ByteArrayInputStream(strData);
			 newStrData = ByteHandle.unpack(strStream, newStrData);
			 System.out.println(strData.length);
			 System.out.println(stringdata);
			 System.out.println(newStrData);
			 
			 
			 
			 System.out.println("TYPE VALUE "+persons1.type);
			 System.out.println("T1 "+persons1.t1);
		        out.write(ByteHandle.pack(persons1));
		        out.write(ByteHandle.pack(persons2));
		        out.flush();
		        out.close();

		        System.out.println("-------------------下面是从序列化中恢复-------------------------------");

		       InputStream in=new FileInputStream("data.out");
		        
		        
		      
				try {
					OperationTest inpersons = ByteHandle.unpack(in, OperationTest.class);
					System.out.println("1`````"+inpersons.type);
					System.out.println(inpersons.t1);
			        System.out.println(inpersons.t2);
			        System.out.println(inpersons.testStr);			        
			        System.out.println("testoptlong: "+ inpersons.testoptlong.isSet());
			        System.out.println("testoptlong: "+ inpersons.testoptlong.getOptionalField());
			        System.out.println("testlongList Size: "+ inpersons.testlongList.size());
			        System.out.println("testlongList first: "+ inpersons.testlongList.get(0));
			        System.out.println("testlongSet Size: "+ inpersons.testlongSet.size());
			        System.out.println("testlongSet first: "+ inpersons.testlongSet.toArray()[0]);
			        System.out.println("testMap size: "+ inpersons.testMap.size());
			        System.out.println("testMap size: "+ inpersons.testMap.get(2));
			        System.out.println("testmultiMap size: "+ inpersons.testmultiMap.size());
			        System.out.println("testmultiMap data: "+ inpersons.testmultiMap.entries().toString());
			        System.out.println("-------------------------------");
			        for(int i =0;i<5;++i)
			        	System.out.println("testcahrArray: " +(byte)inpersons.testchararray[i].charValue());
			        OperationTest inpersons2 = ByteHandle.unpack(in, OperationTest.class);
			
			        System.out.println("1`````"+inpersons.type);
			        System.out.println("2`````"+inpersons2.type);
			        System.out.println(inpersons2.t1);
			        System.out.println(inpersons2.t2);
			        System.out.println(inpersons2.testStr);
			        for(int i =0;i<5;++i)
			        	System.out.println("testcahrArray: " +(byte)inpersons2.testchararray[i].charValue());
			        System.out.println("testoptlong: "+ inpersons2.testoptlong.isSet());
			        
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
	 }


    
}
