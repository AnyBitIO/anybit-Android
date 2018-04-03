package bitshares1_decode;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class VariantString extends ByteSerializable {
    public static final String TYPE = "type";
    public static final String DATA = "data";

    public Byte type = 5;
    public String data;

    public VariantString(){
        this.type = 5;
        this.data = "";
    }

    @Override
    public Field[] getOrderedFieldName() {
        List<Field> fields;
        fields = new LinkedList<Field>();
        try {
            fields.add(this.getClass().getDeclaredField(TYPE));
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
