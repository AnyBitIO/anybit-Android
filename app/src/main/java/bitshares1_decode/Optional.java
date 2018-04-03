package bitshares1_decode;



/**
 * Container template class used whenever we have an optional field.
 *
 * The idea here is that the binary serialization of this field should be performed
 * in a specific way determined by the field implementing the {@link ByteSerializable}
 * interface, more specifically using the {@link ByteSerializable#toBytes()} method.
 *
 * However, if the field is missing, the Optional class should be able to know how
 * to serialize it, as this is always done by placing an zero byte.
 */
public class Optional<T >  {
    private T optionalField;

    public void setOptionalField(T optionalField) {
		this.optionalField = optionalField;
	}



	public T getOptionalField() {
		return optionalField;
	}



	public Optional(T field){
        optionalField = field;
    }


    public boolean isSet(){
        return this.optionalField != null;
    }
}
