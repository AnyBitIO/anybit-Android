package bitshares1_decode.transaction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;

import bitshares1_decode.Asset;
import bitshares1_decode.ByteHandle;
import bitshares1_decode.ByteSerializable;
import bitshares1_decode.Operation;
import bitshares1_decode.OperationBase;
import bitshares1_decode.OperationType;
import bitshares1_decode.Optional;
import bitshares1_decode.Ripemd160;
import bitshares1_decode.Util;
import bitshares1_decode.operations.DepositOperation;
import bitshares1_decode.operations.WithdrawOperation;
import bitshares1_decode.operations.WithdrawWithSignature;
import bitshares1_decode.Chains;
import org.bitcoinj.params.MainNetParams;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.bitcoinj.core.DumpedPrivateKey;
import static com.google.common.base.Preconditions.checkArgument;

public class Transaction extends ByteSerializable {
	
	public static final String EXPIRATION = "expiration";
	public static final String RESERVED = "reserved";
	public static final String FROM_ACCOUNT = "from_account";
	public static final String INPORT_ASSET = "inport_asset";
	public static final String OPERATIONS = "operations";
	public static final String RESULT_TRX_TYPE = "result_trx_type";
	public static final String RESULT_TRX_ID = "result_trx_id";
	
	
	UnsignedInteger expiration;
	Optional<UnsignedLong> reserved;
	String		from_account;
	Asset	inport_asset;
	List<Operation> operations;
	Byte result_trx_type;
	
	Ripemd160 result_trx_id;
	
	public Transaction() {
		this.operations = new LinkedList<Operation>();
		this.operations.add(new Operation());
	}
	
	
	@Override
	public Field[]  getOrderedFieldName() {
		List<Field> fields;
		fields = new LinkedList<Field>();
		try {
			fields.add(this.getClass().getDeclaredField(EXPIRATION));
			fields.add(this.getClass().getDeclaredField(RESERVED));
			fields.add(this.getClass().getDeclaredField(FROM_ACCOUNT));
			fields.add(this.getClass().getDeclaredField(INPORT_ASSET));
			fields.add(this.getClass().getDeclaredField(OPERATIONS));
			fields.add(this.getClass().getDeclaredField(RESULT_TRX_TYPE));
			fields.add(this.getClass().getDeclaredField(RESULT_TRX_ID));
			
			
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
	
	public byte[] getSignature(ECKey privateKey){
        boolean isGrapheneCanonical = false;
        byte[] sigData = null;

        while(!isGrapheneCanonical) {
            byte[] serializedTransaction = ByteHandle.pack(this);
            byte[] chainId = Util.hexToBytes(Chains.CTC.CHAIN_ID);
            serializedTransaction = Bytes.concat(serializedTransaction,chainId);
            System.out.println(Util.bytesToHex(serializedTransaction));


            Sha256Hash hash = Sha256Hash.wrap(Sha256Hash.hash(serializedTransaction));
            System.out.println("sign hash: "+hash.toString());
            int recId = -1;
            ECKey.ECDSASignature sig = privateKey.sign(hash);

            // Now we have to work backwards to figure out the recId needed to recover the signature.
            for (int i = 0; i < 4; i++) {
                ECKey k = ECKey.recoverFromSignature(i, sig, hash, privateKey.isCompressed());
                if (k != null && k.getPubKeyPoint().equals(privateKey.getPubKeyPoint())) {
                    recId = i;
                    break;
                }
            }

            sigData = new byte[65];  // 1 header + 32 bytes for R + 32 bytes for S
            int headerByte = recId + 27 + (privateKey.isCompressed() ? 4 : 0);
            sigData[0] = (byte) headerByte;
            System.arraycopy(Utils.bigIntegerToBytes(sig.r, 32), 0, sigData, 1, 32);
            System.arraycopy(Utils.bigIntegerToBytes(sig.s, 32), 0, sigData, 33, 32);

            // Further "canonicality" tests
            if(((sigData[0] & 0x80) != 0) || (sigData[0] == 0) ||
                    ((sigData[1] & 0x80) != 0) || ((sigData[32] & 0x80) != 0) ||
                    (sigData[32] == 0) || ((sigData[33] & 0x80)  != 0)){
				this.expiration = this.expiration.plus(UnsignedInteger.fromIntBits(1));
            }else{
                isGrapheneCanonical = true;
            }
        }
        return sigData;
    }
	
	public static ECKey fromBase58(String base58) {
		ECKey key2 = DumpedPrivateKey.fromBase58(MainNetParams.get(), base58).getKey();
		return key2;
    }
	
	public static void main(String[]args) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, IOException {
		String hexdata = "7f67af5a000000000000000000000002012228f5b864bdc1aa6e08fc8dc693f431bf43d54814fd97e5725a01e1f5050000000000022e00e1f5050000000000000000000000000001001a284268d3ba7c89791850ffd4f7830ba89acbcc9caab98968a600000000000000000000000000000000000000000000";
		byte[] datas = Util.hexToBytes(hexdata);
		InputStream input = new ByteArrayInputStream(datas);
		Transaction temp;
		temp = ByteHandle.unpack(input,Transaction.class);
		System.out.println(temp);
		System.out.println("from_account: "+ temp.expiration);
		System.out.println("operations: "+ temp.operations);
		System.out.println("operations: "+ temp.operations.size());
		System.out.println("operations 0 type: "+ temp.operations.get(0).type);
		
		for(int i =0;i<temp.operations.size();++i) {
			if(temp.operations.get(i).type == OperationType.withdraw_op_type.getValue()) {
				WithdrawOperation aa = temp.operations.get(i).toData();
				System.out.println("Withdraw amount: "+ ((WithdrawOperation)aa).amount);
				System.out.println("Withdraw balance_id: "+ ((WithdrawOperation)aa).balance_id.toString());
			}
			if(temp.operations.get(i).type == OperationType.deposit_op_type.getValue()) {
				DepositOperation aa = temp.operations.get(i).toData();
				System.out.println("Deposit amount: "+ ((DepositOperation)aa).amount);
				System.out.println("Deposit balance_id: "+ ((DepositOperation)aa).condition);
				WithdrawWithSignature ss  = ((DepositOperation)aa).condition.toData();
				System.out.println("Deposit balance_id: "+ ss.owner);
			}
		}
		
		byte[] ser = ByteHandle.pack(temp,Transaction.class);

		System.out.println(Util.bytesToHex(ser));
		
		String key_str = "5KEmn2XZo1Zdqw3B7Ha1LnKA6t8MbRn8oNJnnpr6PhSvnxxGVpY";
		ECKey eckey;
		byte[] base58_data = Base58.decode(key_str);
		byte[] key_data = new byte[base58_data.length -5];
		System.arraycopy(base58_data, 1, key_data, 0, base58_data.length -5);
		System.out.println(Util.bytesToHex(key_data));
		eckey = ECKey.fromPrivate(key_data,false);
		byte[] signature =  temp.getSignature(eckey);
		// 注意这里修改了交易的过期时间，在广播时需要更新待广播的交易结构体
		System.out.println("long:" +temp.expiration.longValue());


		System.out.println("signature: " + Util.bytesToHex(signature));
	}
}
