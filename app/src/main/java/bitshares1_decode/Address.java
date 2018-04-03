package bitshares1_decode;

import bitshares1_decode.errors.MalformedAddressException;
import com.google.common.primitives.Bytes;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.spongycastle.crypto.digests.RIPEMD160Digest;
import org.spongycastle.crypto.digests.SHA256Digest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class used to encapsulate address-related operations.
 */
public class Address extends ByteSerializable {

    public final static String ADDR = "addr";

    public final static byte default_version = 86;

    private Byte[] addr;
    public static final int addrSize = 25;

    public Address() {
    	this.addr = new Byte[addrSize];
    	this.addr[0] = default_version;
    	for(int i =1;i<addrSize;++i) {
    		this.addr[i] = 0;
    	}
    }

    public void setaddrArray(Object[] array) {
    	for(int i =0;i<addrSize;++i){
    		this.addr[i] = (Byte)array[i];
    	}
    }

    public Address(ECKey pub_key) {
        byte[] addr_data = calculate_address_from_pub_key(pub_key.getPubKey(),default_version);
        this.addr = new Byte[addrSize];
        for(int i =0;i<addrSize;++i){
            this.addr[i] = addr_data[i];
        }
    }

    private byte[] calculate_address_from_pub_key(byte[] pub_key_data,byte version){
        byte[] sha256_data = new byte[256/8];
        SHA256Digest sha256Digest = new SHA256Digest();
        sha256Digest.update(pub_key_data,0,pub_key_data.length);
        sha256Digest.doFinal(sha256_data,0);
        byte[] ripemd160_data = new byte[160/8];
        RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
        ripemd160Digest.update(sha256_data, 0, sha256_data.length);
        ripemd160Digest.doFinal(ripemd160_data, 0);
        byte[] middle_data = Bytes.concat(new byte[]{version},ripemd160_data);
        byte[] checksum_data = calculateChecksum(middle_data);
        return Bytes.concat(middle_data,checksum_data);
    }


    private byte[] calculateChecksum(byte[] data){
        byte[] checksum = new byte[160 / 8];
        RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
        ripemd160Digest.update(data, 0, data.length);
        ripemd160Digest.doFinal(checksum, 0);
        ripemd160Digest.reset();
        ripemd160Digest.update(checksum, 0, checksum.length);
        ripemd160Digest.doFinal(checksum, 0);
        return Arrays.copyOfRange(checksum, 0, 4);
    }


    public int hashCode() {
    	byte[] result = new byte[4];
    	for(int i =0;i<4;++i) {
    		result[i] = addr[21+i].byteValue();
    	}
		return Util.restoreInteger(result).intValue();
    }


    public Address(ECKey key, int version) {
        byte[] addr_data = calculate_address_from_pub_key(key.getPubKey(),(byte)version);
        this.addr = new Byte[addrSize];
        for(int i =0;i<addrSize;++i){
            this.addr[i] = addr_data[i];
        }
    }


    public Address(String address) throws MalformedAddressException {
        byte[] decoded = Base58.decode(address);
        byte[] pubKey = Arrays.copyOfRange(decoded, 0, decoded.length-4);
        byte[] checksum = Arrays.copyOfRange(decoded, decoded.length - 4, decoded.length);
        byte[] calculatedChecksum = calculateChecksum(pubKey);
        for(int i = 0; i < calculatedChecksum.length; i++){
            if(checksum[i] != calculatedChecksum[i]){
                throw new MalformedAddressException("Checksum error");
            }
        }
        this.addr = new Byte[addrSize];
        for(int i =0;i<addrSize;++i) {
        	this.addr[i] = decoded[i];
        }
    }
    @Override
    public String toString() {
    	
        return Base58.encode(Util.toPrimitives(addr));
    }

    public static String from_pubkey_to_address(ECKey pub_key,byte version){
        Address temp_address = new Address(pub_key,version);
        return temp_address.toString();
    }
    public static String from_pubkey_to_address(ECKey pub_key){
        Address temp_address = new Address(pub_key,default_version);
        return temp_address.toString();
    }

    @Override
	public Field[] getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(ADDR));			
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
