package bitshares1_decode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;  
/**
 * Interface implemented by all entities for which makes sense to have a
 * byte-array representation.
 */
public abstract class ByteSerializable  {
	
	//public abstract byte[] toBytes();
	//public abstract void readBytes(InputStream input);
	
	public abstract Field[]  getOrderedFieldName();
}
