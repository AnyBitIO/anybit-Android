package bitshares1_decode.operations;


import bitshares1_decode.CompressSignedInt;
import bitshares1_decode.OperationBase;
import bitshares1_decode.OperationType;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;


public class ImessageMemoOperation extends OperationBase {

    public static final String IMESSAGE = "imessage";


    public String imessage;

    public ImessageMemoOperation() {
    	this.imessage = "";
    	this.type = OperationType.imessage_memo_op_type.getValue();
    }
    
    
	@Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(IMESSAGE));
			
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
