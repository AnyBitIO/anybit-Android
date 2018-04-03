package bitshares1_decode;


import java.lang.reflect.Field;
import java.util.*;


public class Code extends ByteSerializable {
    public static final String ABI = "abi";
    public static final String OFFLINE_ABI = "offline_abi";
    public static final String EVENTS = "events";
    public static final String STORAGE_PROPERTIES = "storage_properties";
    public static final String BYTE_CODE = "byte_code";
    public static final String CODE_HASH = "code_hash";

    Set<String> abi;
    Set<String> offline_abi;
    Set<String> events;
    Map<String,CompressUnsignedInt> storage_properties;
    List<Byte> byte_code;
    String code_hash;

    public Code(){
        this.abi = new HashSet<String>();
        this.abi.add("");
        this.offline_abi = new HashSet<String>();
        this.offline_abi.add("");
        this.events = new HashSet<String>();
        this.events.add("");
        this.storage_properties = new TreeMap<String,CompressUnsignedInt>();
        this.storage_properties.put("a", new CompressUnsignedInt(0));
        this.byte_code = new LinkedList<Byte>();
        this.byte_code.add((byte)0);
        this.code_hash = "";
    }

    @Override
    public  Field[]  getOrderedFieldName(){
        List<Field> fields;
        fields = new LinkedList<Field>();
        try {
            fields.add(this.getClass().getDeclaredField(ABI));
            fields.add(this.getClass().getDeclaredField(OFFLINE_ABI));
            fields.add(this.getClass().getDeclaredField(EVENTS));
            fields.add(this.getClass().getDeclaredField(STORAGE_PROPERTIES));
            fields.add(this.getClass().getDeclaredField(BYTE_CODE));
            fields.add(this.getClass().getDeclaredField(CODE_HASH));



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
