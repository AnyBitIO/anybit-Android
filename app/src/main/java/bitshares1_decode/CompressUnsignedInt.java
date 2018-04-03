package bitshares1_decode;

import com.google.common.primitives.UnsignedInteger;

public class CompressUnsignedInt {
	public UnsignedInteger value; 
	public CompressUnsignedInt(){
		value = UnsignedInteger.fromIntBits(0);
	}
	public CompressUnsignedInt(UnsignedInteger value){
		this.value = value;
	}
	public CompressUnsignedInt(int value){
		this.value= UnsignedInteger.fromIntBits(value) ;
	}
	public CompressUnsignedInt(Integer value){
		this.value = UnsignedInteger.fromIntBits(value);
	}
}
