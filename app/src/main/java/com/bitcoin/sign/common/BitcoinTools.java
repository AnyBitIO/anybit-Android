package com.bitcoin.sign.common;

import com.bitcoin.common.Base58;
import com.bitcoin.common.ByteUtilities;
import com.bitcoin.common.DeterministicRng;
import com.bitcoin.common.Json;
import com.bitcoin.common.crypto.Secp256k1;
import com.bitcoin.sign.BitcoinResource;
import com.bitcoin.sign.bitcoindrpc.BlockChainName;
import com.bitcoin.sign.bitcoindrpc.NetworkBytes;
import com.bitcoin.sign.bitcoindrpc.RawInput;
import com.bitcoin.sign.bitcoindrpc.RawTransaction;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;

public class BitcoinTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinTools.class);
    public static final String NOKEY = "NOKEY";
    private static final String SHA256 = "SHA-256";

    /**
     * Generate a deterministic set of private keys based on a secret key.
     *
     * @param userKeyPart   Expect these to be hex strings without the leading 0x identifier. When
     *                      combined with serverKeyPart, it provides the seed for the private keys.
     * @param serverKeyPart Expect these to be hex strings without the leading 0x identifier. When
     *                      combined with userKeyPart, it provides the seed for the private keys.
     * @param rounds        Number of keys to skip when generating the private key.
     * @return The private key that this data generates.
     */
    public static String getDeterministicPrivateKey(String userKeyPart, String serverKeyPart,
                                                    int rounds) {
        if (userKeyPart == null) {
            return NOKEY;
        }

        try {
            byte[] userKey = new BigInteger(userKeyPart, 16).toByteArray();
            byte[] serverKey = new BigInteger(serverKeyPart, 16).toByteArray();
            SecureRandom secureRandom = DeterministicRng.getSecureRandom(userKey, serverKey);

            // Generate the key, skipping as many as desired.
            byte[] privateKeyAttempt = new byte[32];
            for (int i = 0; i < Math.max(rounds, 1); i++) {
                secureRandom.nextBytes(privateKeyAttempt);
                BigInteger privateKeyCheck = new BigInteger(1, privateKeyAttempt);
                while (privateKeyCheck.compareTo(BigInteger.ZERO) == 0
                        || privateKeyCheck.compareTo(Secp256k1.MAXPRIVATEKEY) == 1) {
                    secureRandom.nextBytes(privateKeyAttempt);
                    privateKeyCheck = new BigInteger(1, privateKeyAttempt);
                }
            }

            return encodePrivateKey(ByteUtilities.toHexString(privateKeyAttempt));
        } catch (RuntimeException e) {
            LOGGER.debug(null, e);
            return NOKEY;
        }
    }

    /**
     * Encodes a raw public key in a bitcoind compatible format.
     */
    public static String encodePrivateKey(String privateKeyString) {
        String networkBytes;
        try {
            networkBytes = BitcoinResource.getResource().getBitcoindRpc().getblockchaininfo().getChain()
                    == BlockChainName.main ? NetworkBytes.PRIVATEKEY.toString() :
                    NetworkBytes.PRIVATEKEY_TEST.toString();
        } catch (Exception e) {
            LOGGER.debug("No network connection, assuming regular network", e);
            networkBytes = NetworkBytes.PRIVATEKEY.toString();
        }

        // Encode in format bitcoind is expecting
        byte[] privateKeyAttempt = ByteUtilities.toByteArray(privateKeyString);
        byte[] privateKey = ByteUtilities.toByteArray(networkBytes);
        byte[] privateKey2 = new byte[privateKey.length + privateKeyAttempt.length];
        System.arraycopy(privateKey, 0, privateKey2, 0, privateKey.length);
        System
                .arraycopy(privateKeyAttempt, 0, privateKey2, privateKey.length, privateKeyAttempt.length);
        privateKey = new byte[privateKey2.length];
        System.arraycopy(privateKey2, 0, privateKey, 0, privateKey2.length);

        try {
            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.update(privateKey);
            byte[] checksumHash = Arrays.copyOfRange(md.digest(md.digest()), 0, 4);

            privateKey2 = new byte[privateKey.length + checksumHash.length];
            System.arraycopy(privateKey, 0, privateKey2, 0, privateKey.length);
            System.arraycopy(checksumHash, 0, privateKey2, privateKey.length, checksumHash.length);
            privateKey = new byte[privateKey2.length];
            System.arraycopy(privateKey2, 0, privateKey, 0, privateKey2.length);

            return Base58.encode(privateKey);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(null, e);
            return NOKEY;
        }

    }

    /**
     * Encodes the userKey secret so that it can be referenced and stored in bitcoind's wallet without
     * revealing what the original value is.
     *
     * @param key User key secret value.
     * @return Encoded/hashed version of the key.
     */
    public static String encodeUserKey(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.update(key.getBytes("UTF-8"));
            return new BigInteger(md.digest()).toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            LOGGER.error(null, e);
            return null;
        }
    }

    /**
     * Convert a key into its corresponding public address.
     *
     * @param key          Key to convert
     * @param isPrivateKey Is this private or public
     * @return Public bitcoin address.
     */
    public static String getPublicAddress(String key, boolean isPrivateKey) {
        try {
            byte[] publicKeyBytes;
            if (isPrivateKey) {
                publicKeyBytes = getPublicKeyBytes(key);
            } else {
                publicKeyBytes = ByteUtilities.toByteArray(key);
            }

            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.reset();
            md.update(publicKeyBytes);
            byte[] publicShaKeyBytes = md.digest();

            RIPEMD160Digest ripemd = new RIPEMD160Digest();
            byte[] publicRipemdKeyBytes = new byte[20];
            ripemd.update(publicShaKeyBytes, 0, publicShaKeyBytes.length);
            ripemd.doFinal(publicRipemdKeyBytes, 0);

            // Add network bytes
            String networkBytes =
                    BitcoinResource.getResource().getBitcoindRpc().getblockchaininfo().getChain()
                            == BlockChainName.main ? NetworkBytes.P2PKH.toString() :
                            NetworkBytes.P2PKH_TEST.toString();

            byte[] networkPublicKeyBytes = ByteUtilities.toByteArray(networkBytes);
            byte[] networkPublicKeyBytes2 =
                    new byte[networkPublicKeyBytes.length + publicRipemdKeyBytes.length];
            System.arraycopy(networkPublicKeyBytes, 0, networkPublicKeyBytes2, 0,
                    networkPublicKeyBytes.length);
            System
                    .arraycopy(publicRipemdKeyBytes, 0, networkPublicKeyBytes2, networkPublicKeyBytes.length,
                            publicRipemdKeyBytes.length);
            networkPublicKeyBytes = new byte[networkPublicKeyBytes2.length];
            System.arraycopy(networkPublicKeyBytes2, 0, networkPublicKeyBytes, 0,
                    networkPublicKeyBytes2.length);

            md = MessageDigest.getInstance(SHA256);
            md.reset();
            md.update(networkPublicKeyBytes);
            byte[] publicKeyChecksum = Arrays.copyOfRange(md.digest(md.digest()), 0, 4);

            byte[] decodedPublicKey = new byte[networkPublicKeyBytes.length + publicKeyChecksum.length];
            System.arraycopy(networkPublicKeyBytes, 0, decodedPublicKey, 0, networkPublicKeyBytes.length);
            System.arraycopy(publicKeyChecksum, 0, decodedPublicKey, networkPublicKeyBytes.length,
                    publicKeyChecksum.length);

            return Base58.encode(decodedPublicKey);
        } catch (Exception e) {
            LOGGER.error("Unable to get network information when creating address", e);
            return null;
        }
    }

    /**
     * Decodes a bitcoin address and returns the RIPEMD-160 that it contains.
     *
     * @param address Bitcoin address
     * @return RIPEMD-160 hash of the public key.
     */
    public static String decodeAddress(String address) {
        try {
            byte[] decodedNetworkAddress = Base58.decode(address);
            byte[] networkBytes = ByteUtilities.readBytes(decodedNetworkAddress, 0, 1);
            byte[] addressBytes =
                    ByteUtilities.readBytes(decodedNetworkAddress, 1, decodedNetworkAddress.length - 5);

            String checksumString =
                    ByteUtilities.toHexString(networkBytes) + ByteUtilities.toHexString(addressBytes);
            byte[] checksumData = ByteUtilities.toByteArray(checksumString);

            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.reset();
            byte[] calculatedCheckum = Arrays.copyOfRange(md.digest(md.digest(checksumData)), 0, 4);

            byte[] checksumBytes =
                    ByteUtilities.readBytes(decodedNetworkAddress, decodedNetworkAddress.length - 4, 4);
            if (!ByteUtilities.toHexString(calculatedCheckum)
                    .equalsIgnoreCase(ByteUtilities.toHexString(checksumBytes))) {
                LOGGER.debug("Badchecksum on: " + ByteUtilities.toHexString(addressBytes));
                return "";
            }
            return ByteUtilities.toHexString(addressBytes);
        } catch (Exception e) {
            LOGGER.error(null, e);
            return "";
        }
    }

    /**
     * Converts a RIPEMD-160 address to a base58 encoded one with checksums.
     *
     * @param addressBytes RIPEMD-160 address
     * @param networkBytes Network bytes that identify which network this address belongs to.
     * @return Address that bitcoind can import.
     */
    public static String encodeAddress(String addressBytes, String networkBytes) {
        try {
            String encodedBytes = networkBytes + addressBytes;
            byte[] data = ByteUtilities.toByteArray(encodedBytes);
            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.reset();
            md.update(data);
            byte[] publicKeyChecksum = Arrays.copyOfRange(md.digest(md.digest()), 0, 4);

            encodedBytes = encodedBytes + ByteUtilities.toHexString(publicKeyChecksum);
            encodedBytes = encodedBytes.toLowerCase(Locale.US);
            encodedBytes = Base58.encode(ByteUtilities.toByteArray(encodedBytes));
            return encodedBytes;
        } catch (Exception e) {
            LOGGER.error(null, e);
            return null;
        }
    }

    /**
     * Decodes an address and checks if it's a P2SH.
     *
     * @param address Bitcoin address
     * @return True if it's a P2SH address, false otherwise.
     */
    public static boolean isMultiSigAddress(String address) {
        try {
            // If the address isn't valid.
            if (decodeAddress(address).isEmpty()) {
                return false;
            }

            byte[] decodedNetworkAddress = Base58.decode(address);
            byte[] networkBytes = ByteUtilities.readBytes(decodedNetworkAddress, 0, 1);

            String networkString = ByteUtilities.toHexString(networkBytes);
            return networkString.equalsIgnoreCase(NetworkBytes.P2SH.toString()) || networkString
                    .equalsIgnoreCase(NetworkBytes.P2SH_TEST.toString());

        } catch (Exception e) {
            LOGGER.debug(null, e);
            return false;
        }
    }

    public static String getPublicKey(String privateKey) {
        return ByteUtilities.toHexString(getPublicKeyBytes(privateKey));
    }

    /**
     * Converts a bitcoin-encoded private key to its corresponding public key.
     *
     * @param privateKey Bitcoin-encoded private key.
     * @return ECDSA public key.
     */
    public static byte[] getPublicKeyBytes(String privateKey) {
        try {
            byte[] decodedPrivateKey = Base58.decode(privateKey);
            byte[] networkPrivateKeyBytes = new byte[decodedPrivateKey.length - 4];
            byte[] privateKeyChecksum = new byte[4];

            System.arraycopy(decodedPrivateKey, 0, networkPrivateKeyBytes, 0, decodedPrivateKey.length - 4);
            System.arraycopy(decodedPrivateKey, decodedPrivateKey.length - 4, privateKeyChecksum, 0, 4);

            // Is it valid?
            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.update(networkPrivateKeyBytes);
            byte[] checksumCheck = Arrays.copyOfRange(md.digest(md.digest()), 0, 4);
            for (int i = 0; i < 4; i++) {
                if (privateKeyChecksum[i] != checksumCheck[i]) {
                    return new byte[0];
                }
            }

            // Strip leading network byte and get the public key
            byte[] privateKeyBytes =
                    Arrays.copyOfRange(networkPrivateKeyBytes, 1, 33);
            //byte[] privateKeyBytes =
            //        Arrays.copyOfRange(networkPrivateKeyBytes, 1, networkPrivateKeyBytes.length);
            return Secp256k1.getPublicKey(privateKeyBytes);

        } catch (Exception e) {
            LOGGER.error(null, e);
            return new byte[0];
        }
    }

    public static Iterable<String> getSigners(String signedScript, String redeemScript,
                                              byte[] message) {
        LOGGER.debug("[Get BTC Signers");
        // Break up the signedScript into signatures
        Iterable<String> signatures = RawInput.getSignatures(signedScript, redeemScript);
        // Get the public keys from the redeemScript (These in the reverse order).
        Iterable<String> publicKeys = RawTransaction.decodeRedeemScript(redeemScript);
        LinkedList<String> signers = new LinkedList<>();

        LOGGER.debug("[Signatures] " + Json.stringifyObject(Iterable.class, signatures));
        LOGGER.debug("[Public Keys] " + Json.stringifyObject(Iterable.class, publicKeys));
        // Foreach public key, check it against the signatures in order
        for (String publicKey : publicKeys) {
            for (String signature : signatures) {
                // Break signature back up into R/S
                // [DER Format] 30 sigsize 02 sigsize sigR 02 sigsize sigS
                LOGGER.debug("[Signature] " + signature);
                byte[] sigBytes = ByteUtilities.toByteArray(signature);
                int buffPointer = 3;
                int sigSize =
                        new BigInteger(1, ByteUtilities.readBytes(sigBytes, buffPointer, 1)).intValue();
                buffPointer += 1;
                byte[] r = ByteUtilities.readBytes(sigBytes, buffPointer, sigSize);
                // Skip the type byte (02)
                buffPointer += 1;
                buffPointer += sigSize;
                sigSize = new BigInteger(1, ByteUtilities.readBytes(sigBytes, buffPointer, 1)).intValue();
                buffPointer += 1;
                byte[] s = ByteUtilities.readBytes(sigBytes, buffPointer, sigSize);

                LOGGER.debug("[R] " + ByteUtilities.toHexString(r));
                LOGGER.debug("[S] " + ByteUtilities.toHexString(s));

                if (Secp256k1.verifySignature(r, s, ByteUtilities.toByteArray(publicKey), message)) {
                    LOGGER.debug("Signature matches, adding");
                    signers.add(BitcoinTools.getPublicAddress(publicKey, false));
                    break;
                }
            }
        }

        return signers;
    }

    public static String reorgSignatures(String signedScript, String redeemScript, byte[] message) {
        LOGGER.debug("[Re-org BTC Signatures");
        // Break up the signedScript into signatures
        Iterable<String> signatures = RawInput.getSignatures(signedScript, redeemScript);
        // Get the public keys from the redeemScript (These in the reverse order).
        Iterable<String> publicKeys = RawTransaction.decodeRedeemScript(redeemScript);
        LinkedList<String> sortedSignatures = new LinkedList<>();

        LOGGER.debug("[Signatures] " + Json.stringifyObject(Iterable.class, signatures));
        LOGGER.debug("[Public Keys] " + Json.stringifyObject(Iterable.class, publicKeys));
        // Foreach public key, check it against the signatures in order
        for (String publicKey : publicKeys) {
            if (sortedSignatures.size() >= RawTransaction.getRedeemScriptKeyCount(redeemScript)) {
                LOGGER.debug("Already met signature requirements, stopping.");
                break;
            }
            for (String signature : signatures) {
                // Break signature back up into R/S
                // [DER Format] 30 sigsize 02 sigsize sigR 02 sigsize sigS
                LOGGER.debug("[Signature] " + signature);
                byte[] sigBytes = ByteUtilities.toByteArray(signature);
                int buffPointer = 3;
                int sigSize =
                        new BigInteger(1, ByteUtilities.readBytes(sigBytes, buffPointer, 1)).intValue();
                buffPointer += 1;
                byte[] r = ByteUtilities.readBytes(sigBytes, buffPointer, sigSize);
                // Skip the type byte (02)
                buffPointer += 1;
                buffPointer += sigSize;
                sigSize = new BigInteger(1, ByteUtilities.readBytes(sigBytes, buffPointer, 1)).intValue();
                buffPointer += 1;
                byte[] s = ByteUtilities.readBytes(sigBytes, buffPointer, sigSize);

                LOGGER.debug("[R] " + ByteUtilities.toHexString(r));
                LOGGER.debug("[S] " + ByteUtilities.toHexString(s));

                if (Secp256k1.verifySignature(r, s, ByteUtilities.toByteArray(publicKey), message)) {
                    LOGGER.debug("Signature matches, adding");
                    sortedSignatures.add(signature);
                    break;
                }
            }
        }

        String reOrgedScript = "00";
        // Keys are in reverse order, which means the signatures are too now. Flip Them.
        for (int i = sortedSignatures.size(); i > 0; i--) {
            String sig = sortedSignatures.get(i - 1);
            byte[] sigData = ByteUtilities.toByteArray(sig);
            byte[] dataSize = RawTransaction.writeVariableStackInt(sigData.length);
            reOrgedScript += ByteUtilities.toHexString(dataSize);
            reOrgedScript += sig;
        }
        byte[] redeemScriptBytes = ByteUtilities.toByteArray(redeemScript);
        byte[] dataSize = RawTransaction.writeVariableStackInt(redeemScriptBytes.length);
        reOrgedScript += ByteUtilities.toHexString(dataSize);
        reOrgedScript += redeemScript;

        return reOrgedScript;
    }

    public static byte[] hashTwice(byte[] input, int offset, int length) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(input, offset, length);
            return digest.digest(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getAddressByPrivateKey(String privateKey) {
        try {
            byte[] publicKeyBytes = BitcoinTools.getPublicKeyBytes(privateKey);
            publicKeyBytes = Arrays.copyOfRange(publicKeyBytes, 1, 33);
            byte version = 2;
            byte[] publicKeyBytes2 = new byte[33];
            publicKeyBytes2[0] = version;
            System.arraycopy(publicKeyBytes, 0, publicKeyBytes2, 1, publicKeyBytes.length);

            MessageDigest md = MessageDigest.getInstance(SHA256);
            md.reset();
            md.update(publicKeyBytes2);
            byte[] publicShaKeyBytes = md.digest();

            RIPEMD160Digest ripemd = new RIPEMD160Digest();
            byte[] publicRipemdKeyBytes = new byte[20];
            ripemd.update(publicShaKeyBytes, 0, publicShaKeyBytes.length);
            ripemd.doFinal(publicRipemdKeyBytes, 0);

            //前面加入地址版本号（比特币主网版本号“0x00”）
            byte[] address = new byte[21];
            address[0] = Constant.ADDRESS_VERSION;
            System.arraycopy(publicRipemdKeyBytes, 0, address, 1, publicRipemdKeyBytes.length);

            MessageDigest md1 = MessageDigest.getInstance(SHA256);
            md1.reset();
            md1.update(address);
            byte[] address256 = md1.digest();

            MessageDigest md2 = MessageDigest.getInstance(SHA256);
            md2.reset();
            md2.update(address256);
            byte[] address256256 = md2.digest();

            byte[] address25 = new byte[25];
            System.arraycopy(address, 0, address25, 0, address.length);
            System.arraycopy(address256256, 0, address25, address25.length - 4, 4);
            return Base58.encode(address25);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static byte[] hexToBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    public static String getAddressByScriptPuKey(String scriptPubKey,String coinType) {
        try {

            byte[] script = hexToBytes(scriptPubKey);

            byte[] address = new byte[21];
            if("TEST".equals(coinType)){
                address[0] = Constant.TEST_ADDRESS_VERSION;
            }else{
                address[0] = Constant.ADDRESS_VERSION;
            }

            System.arraycopy(script, 3, address, 1, 20);

            MessageDigest md1 = MessageDigest.getInstance(SHA256);
            md1.reset();
            md1.update(address);
            byte[] address256 = md1.digest();

            MessageDigest md2 = MessageDigest.getInstance(SHA256);
            md2.reset();
            md2.update(address256);
            byte[] address256256 = md2.digest();

            byte[] address25 = new byte[25];
            System.arraycopy(address, 0, address25, 0, address.length);
            System.arraycopy(address256256, 0, address25, address25.length - 4, 4);
            return Base58.encode(address25);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static void main(String[] args) {
        try {
            //String address0 = getAddressByPrivateKey("KzYcfbTzbJ6M23PjBaDxUZHD2S26psGpLWKrDWMdhXFormQsbGSr");
            //System.out.println(address0);
            //byte[] publicKeyBytes = hexToBytes("037a05dc8fe0146f130468113b6c14e0630e4b47ef230b5439a952d0a40b5667b8");
            //publicKeyBytes = Arrays.copyOfRange(publicKeyBytes, 1, 33);
            //
            //byte version = 3;
            //byte[] publicKeyBytes2 = new byte[33];
            //publicKeyBytes2[0] = version;
            //System.arraycopy(publicKeyBytes, 0, publicKeyBytes2, 1, publicKeyBytes.length);
            //
            //MessageDigest md = MessageDigest.getInstance(SHA256);
            //md.reset();
            //md.update(publicKeyBytes2);
            //byte[] publicShaKeyBytes = md.digest();
            //
            //RIPEMD160Digest ripemd = new RIPEMD160Digest();
            //byte[] publicRipemdKeyBytes = new byte[20];
            //ripemd.update(publicShaKeyBytes, 0, publicShaKeyBytes.length);
            //ripemd.doFinal(publicRipemdKeyBytes, 0);
            //
            //byte[] address = new byte[21];
            //address[0] = Constant.ADDRESS_VERSION;
            //System.arraycopy(publicRipemdKeyBytes, 0, address, 1, publicRipemdKeyBytes.length);
            //
            //MessageDigest md1 = MessageDigest.getInstance(SHA256);
            //md1.reset();
            //md1.update(address);
            //byte[] address256 = md1.digest();
            //
            //MessageDigest md2 = MessageDigest.getInstance(SHA256);
            //md2.reset();
            //md2.update(address256);
            //byte[] address256256 = md2.digest();
            //
            //byte[] address25 = new byte[25];
            //System.arraycopy(address, 0, address25, 0, address.length);
            //System.arraycopy(address256256, 0, address25, address25.length - 4, 4);
            //String result =  Base58.encode(address25);
            //System.out.println(result);

            String result = getAddressByScriptPuKey("76a914415264eb41c9c742387f4bcf72757b4af528257588ac","ubtc");
                    //getAddressByPrivateKey("cRpUX1cxovHzDoyTz5AQSqQaFQDXtayDzA8P4XAG4SoerbF5J8PN");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
