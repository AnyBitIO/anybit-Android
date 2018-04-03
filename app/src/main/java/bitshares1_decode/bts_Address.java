package bitshares1_decode;

import bitshares1_decode.errors.MalformedAddressException;
import com.google.common.primitives.Bytes;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.spongycastle.crypto.digests.RIPEMD160Digest;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.digests.SHA512Digest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class used to encapsulate address-related operations.
 */
public class bts_Address extends ByteSerializable {

    public final static String ADDR = "addr";


    private String prefix = "TV";

    private Byte[] addr;
    public static final int addrSize = 20;

    public bts_Address() {
    	this.addr = new Byte[addrSize];
    	for(int i =0;i<addrSize;++i) {
    		this.addr[i] = 0;
    	}
    }

    public void setaddrArray(Object[] array) {
    	for(int i =0;i<addrSize;++i){
    		this.addr[i] = (Byte)array[i];
    	}
    }

    public bts_Address(ECKey pub_key) {
        byte[] addr_data = calculate_address_from_pub_key(pub_key.getPubKey());
        this.addr = new Byte[addrSize];
        for(int i =0;i<addrSize;++i){
            this.addr[i] = addr_data[i];
        }
    }
    public bts_Address(ECKey pub_key,String prefix) {
        this.prefix = prefix;
        byte[] addr_data = calculate_address_from_pub_key(pub_key.getPubKey());
        this.addr = new Byte[addrSize];
        for(int i =0;i<addrSize;++i){
            this.addr[i] = addr_data[i];
        }
    }


    private byte[] calculate_address_from_pub_key(byte[] pub_key_data){
        byte[] sha512_data = new byte[512/8];
        SHA512Digest sha512Digest  = new SHA512Digest();
        sha512Digest.update(pub_key_data,0,pub_key_data.length);
        sha512Digest.doFinal(sha512_data,0);
        byte[] ripemd160_data = new byte[160/8];
        RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
        ripemd160Digest.update(sha512_data, 0, sha512_data.length);
        ripemd160Digest.doFinal(ripemd160_data, 0);
        return ripemd160_data;
    }


    private byte[] calculateChecksum(byte[] data){
        byte[] checksum = new byte[160 / 8];
        RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
        ripemd160Digest.update(data, 0, data.length);
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



    public bts_Address(String address) throws MalformedAddressException {
        String handled_address = address.substring(prefix.length());
        byte[] decoded = Base58.decode(handled_address);
        byte[] pubKey = Arrays.copyOfRange(decoded, 0 , decoded.length-4);
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

    	byte[] checksum_data = calculateChecksum(Util.toPrimitives(this.addr));
        return this.prefix + Base58.encode(Bytes.concat(Util.toPrimitives(addr),checksum_data));
    }

    public static String from_pubkey_to_address(ECKey pub_key,String prefix){
        bts_Address temp_address = new bts_Address(pub_key,prefix);
        return temp_address.toString();
    }
    public static String from_pubkey_to_address(ECKey pub_key){
        bts_Address temp_address = new bts_Address(pub_key);
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
