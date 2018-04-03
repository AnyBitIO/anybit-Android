package kualian.dc.deal.application.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by mayakui on 2018/2/5 0005.
 */
public class BitcoinAddressValidator {
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static Map<String,Set<Byte>> versionMap = null;
    static{
        versionMap = new HashMap<String,Set<Byte>>();

        Set<Byte> versionSet = new HashSet<Byte>();
        versionSet.add((byte)0);
        versionSet.add((byte)5);
        versionMap.put("BTC",versionSet);

        versionSet = new HashSet<Byte>();
        versionSet.add((byte)0);
        versionSet.add((byte)5);
        versionMap.put("UBTC",versionSet);

        versionSet = new HashSet<Byte>();
        versionSet.add((byte)111);
        versionSet.add((byte)5);
        versionMap.put("TEST",versionSet);

        versionSet = new HashSet<Byte>();
        versionSet.add((byte)48);
        versionSet.add((byte)5);
        versionMap.put("LTC",versionSet);

        versionSet = new HashSet<Byte>();
        versionSet.add((byte)40);
        versionSet.add((byte)100);
        versionMap.put("HSR",versionSet);

        versionSet = new HashSet<Byte>();
        versionSet.add((byte)58);
        versionSet.add((byte)50);
        versionMap.put("QTUM",versionSet);

        versionSet = new HashSet<Byte>();
        versionSet.add((byte)50);
        versionSet.add((byte)5);
        versionMap.put("MONA",versionSet);

        versionSet = new HashSet<Byte>();
        versionSet.add((byte)76);
        versionSet.add((byte)16);
        versionMap.put("DASH",versionSet);

        versionSet = new HashSet<Byte>();
        versionSet.add((byte)73);
        versionSet.add((byte)87);
        versionMap.put("WXC",versionSet);

        versionSet = new HashSet<Byte>();
        versionSet.add((byte)30);
        versionSet.add((byte)22);
        versionMap.put("DOGE",versionSet);
    }

    public static boolean validateBitcoinAddress(String addr, String symbol) {
        if (addr.length() < 26 || addr.length() > 35)
            return false;
        byte[] decoded = decodeBase58To25Bytes(addr);
        if (decoded == null)
            return false;

        if(versionMap.get(symbol) == null){
            return false;
        }

        if(!versionMap.get(symbol).contains(decoded[0])){
            return false;
        }

        byte[] hash1 = sha256(Arrays.copyOfRange(decoded, 0, 21));
        byte[] hash2 = sha256(hash1);

        return Arrays.equals(Arrays.copyOfRange(hash2, 0, 4), Arrays.copyOfRange(decoded, 21, 25));
    }

    private static byte[] decodeBase58To25Bytes(String input) {
        BigInteger num = BigInteger.ZERO;
        for (char t : input.toCharArray()) {
            int p = ALPHABET.indexOf(t);
            if (p == -1)
                return null;
            num = num.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(p));
        }

        byte[] result = new byte[25];
        byte[] numBytes = num.toByteArray();
        System.arraycopy(numBytes, 0, result, result.length - numBytes.length, numBytes.length);
        return result;
    }

    private static byte[] sha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean assertBitcoin(String address, String symbol) {
        boolean actual = validateBitcoinAddress(address,symbol);
        return actual;
    }

   /* public static void main(String[] args) {
        System.out.println(assertBitcoin("1JLkT8RZZKXzZ9uUYpkucjP5fnumsvNf9w","UBTC"));
    }*/
}
