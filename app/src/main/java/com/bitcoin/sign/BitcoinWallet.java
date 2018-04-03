package com.bitcoin.sign;

import com.bitcoin.api.currency.Wallet;
import com.bitcoin.common.ByteUtilities;
import com.bitcoin.common.Json;
import com.bitcoin.common.crypto.Secp256k1;
import com.bitcoin.sign.bitcoindrpc.*;
import com.bitcoin.sign.common.BitcoinTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BitcoinWallet implements Wallet {
    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinWallet.class);
    private final BitcoindRpc bitcoindRpc = BitcoinResource.getResource().getBitcoindRpc();
    private static final String PUBKEY_PREFIX = "PK-";

    BitcoinConfiguration config;

    private final HashMap<String, String> multiSigRedeemScripts = new HashMap<>();

    private Thread multiSigSubscription = new Thread(() -> {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                LOGGER.info("Scanning BTC multi-sig addresses");
                scanForAddresses();
                Thread.sleep(60000);
            } catch (Exception e) {
                LOGGER.debug("Multisig scan interrupted.");
            }
        }
    });

    private Thread rescanThread = new Thread(() -> {
        if (config.getRescanTimer() == 0) {
            return;
        }
        while (true) {
            try {
                try {
                    LOGGER.debug("Initiating blockchain rescan...");
                    byte[] key = Secp256k1.generatePrivateKey();
                    String privateKey = BitcoinTools.encodePrivateKey(ByteUtilities.toHexString(key));
                    String address = BitcoinTools.getPublicAddress(privateKey, true);
                    bitcoindRpc.importaddress(address, "RESCAN", true);
                } catch (Exception e) {
                    LOGGER.debug("Rescan thread interrupted, or import timed out (expected)", e);
                }
                Thread.sleep(config.getRescanTimer() * 60L * 60L * 1000L);
            } catch (Exception e) {
                LOGGER.debug("Rescan thread interrupted, or import timed out (expected)", e);
            }
        }
    });

    /**
     * Creates a Bitcoin wallet object with the given configuration.
     */
    public BitcoinWallet(BitcoinConfiguration conf) {
        config = conf;

        if (!multiSigSubscription.isAlive()) {
            multiSigSubscription.setDaemon(true);
            multiSigSubscription.start();
        }

        if (!rescanThread.isAlive()) {
            rescanThread.setDaemon(true);
            rescanThread.start();
        }
    }
    public BitcoinWallet(){

    }

    @Override
    public String generatePrivateKey() {
        String key = ByteUtilities.toHexString(Secp256k1.generatePrivateKey());
        return BitcoinTools.encodePrivateKey(key);
    }

    @Override
    public String generatePublicKey(String privateKey) {
        return ByteUtilities.toHexString(BitcoinTools.getPublicKeyBytes(privateKey));
    }

    @Override
    public String getMultiSigAddress(Iterable<String> addresses, String name) {
        String internalName = BitcoinTools.encodeUserKey(name);
        String newAddress = generateMultiSigAddress(addresses, name);
        bitcoindRpc.importaddress(newAddress, internalName, false);

        return newAddress;
    }

    private void scanForAddresses() {
        try {
            Map<String, BigDecimal> knownAccounts = bitcoindRpc.listaccounts(0, true);
            knownAccounts.keySet().forEach(account -> {
                Pattern pattern = Pattern.compile("^" + PUBKEY_PREFIX + "(.*)");
                Matcher matcher = pattern.matcher(account);
                if (matcher.matches()) {
                    String pubKey = matcher.group(1);
                    try {
                        generateMultiSigAddress(Collections.singletonList(pubKey), null);
                    } catch (Exception e) {
                        LOGGER.debug(null, e);
                        LOGGER.info(account + " appears to be an invalid account - ignoring");
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.debug("No accounts found when scanning");
        }
    }

    private String generateMultiSigAddress(Iterable<String> addresses, String name) {
        LinkedList<String> multisigAddresses = new LinkedList<>();
        addresses.forEach(address -> {
            int rounds = 1;
            String userPrivateKey =
                    BitcoinTools.getDeterministicPrivateKey(name, config.getServerPrivateKey(), rounds);

            String userAddress = BitcoinTools.NOKEY;
            if (!userPrivateKey.equalsIgnoreCase(BitcoinTools.NOKEY)) {
                userAddress = BitcoinTools.getPublicAddress(userPrivateKey, true);

                while (!address.equalsIgnoreCase(userAddress) && rounds <= config
                        .getMaxDeterministicAddresses()) {
                    rounds++;
                    userPrivateKey =
                            BitcoinTools.getDeterministicPrivateKey(name, config.getServerPrivateKey(), rounds);
                    userAddress = BitcoinTools.getPublicAddress(userPrivateKey, true);
                }
            }

            if (address.equalsIgnoreCase(userAddress)) {
                multisigAddresses.add(BitcoinTools.getPublicKey(userPrivateKey));
            } else {
                LOGGER.debug(BitcoinTools.getPublicAddress(address, false));
                multisigAddresses.add(address);
            }
        });

        for (String account : config.getMultiSigAccounts()) {
            if (!account.isEmpty()) {
                multisigAddresses.add(account);
            }
        }

        for (String accountKey : config.getMultiSigKeys()) {
            if (!accountKey.isEmpty()) {
                multisigAddresses.add(BitcoinTools.getPublicKey(accountKey));
            }
        }

        String[] addressArray = new String[multisigAddresses.size()];
        MultiSig newAddress = bitcoindRpc
                .createmultisig(config.getMinSignatures(), multisigAddresses.toArray(addressArray));
        if (name != null && !name.isEmpty()) {
            bitcoindRpc
                    .addmultisigaddress(config.getMinSignatures(), multisigAddresses.toArray(addressArray),
                            BitcoinTools.encodeUserKey(name));
        }

        multiSigRedeemScripts.put(newAddress.getAddress(), newAddress.getRedeemScript());

        return newAddress.getAddress();
    }

    private boolean getOption(String options, String option) {
        if (getOptionValue(options, option) != null) {
            return true;
        }
        return false;
    }

    private String getOptionValue(String options, String option) {
        if (options != null && !options.isEmpty()) {
            try {
                option = option.toLowerCase();
                LinkedList optionList = (LinkedList) Json.objectifyString(LinkedList.class, options);
                if (optionList != null) {
                    for (Object optionPossiblilty : optionList) {
                        String possibleString = (String) optionPossiblilty;
                        possibleString = possibleString.toLowerCase();
                        if (possibleString.contains(option)) {
                            String[] possibleValues = possibleString.split(":");
                            if (possibleValues.length >= 2) {
                                return possibleValues[1];
                            } else {
                                return "";
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.debug("Bad options");
                LOGGER.trace(null, e);
            }
        }
        return null;
    }

    private Iterable<String> getSignersForAddress(String address) {
        LinkedList<String> addresses = new LinkedList<>();
        String redeemScript = multiSigRedeemScripts.get(address);
        if (redeemScript != null) {
            Iterable<String> publicKeys = RawTransaction.decodeRedeemScript(redeemScript);
            publicKeys.forEach(key -> addresses.add(BitcoinTools.getPublicAddress(key, false)));
        } else {
            addresses.add(address);
        }

        return addresses;
    }

    //TODO

    /**
     * 签名处理入口
     *
     * @param transaction
     * @param addressKey
     * @param outputs
     * @return
     */
    @Override
    public String signTransaction(String transaction,Map<String,String> addressKey, Outpoint[] outputs,String coinType) {

        //私钥生成对应地址
        //Map<String,String> addressKey = new HashMap<String,String>();
        //for(String privateKey:privateKeyList){
        //    //addressKey.put(BitcoinTools.getAddressByPrivateKey(privateKey),privateKey);
        //    addressKey.put("mj1seQMAUNuq8A5MaVqfGY3YuKnF7g4nh3",privateKey);
        //}

        SignedTransaction signedTransaction = new SignedTransaction();
        Iterable<Iterable<String>> signatureData = getSigString(transaction, addressKey, outputs, coinType);
        signatureData = signWithPrivateKey(signatureData, false);

        String trx = applySignature(transaction, signatureData,coinType);
        signedTransaction.setTransaction(trx);

        //以下为多签的处理
        //for (String accountKey : config.getMultiSigKeys()) {
        //    if (!accountKey.isEmpty()) {
        //
        //        String msigAddress = BitcoinTools.getPublicAddress(accountKey, true);
        //
        //        if (possibleSigners.contains(msigAddress)) {
        //            transaction = signedTransaction.getTransaction();
        //
        //            signedTransaction = new SignedTransaction();
        //            signatureData = getSigString(transaction, address);
        //            signatureData = signWithPrivateKey(signatureData, accountKey, false);
        //            signedTransaction.setTransaction(applySignature(transaction, address, signatureData));
        //        }
        //    }
        //}

        return signedTransaction.getTransaction();
    }

    private Iterable<Iterable<String>> getSigString(String transaction,Map<String,String> addressKey, Outpoint[] outputs,String coinType) {
        LinkedList<Iterable<String>> signatureData = new LinkedList<>();

        RawTransaction rawTx = RawTransaction.parse(transaction);

        for (RawInput input : rawTx.getInputs()) {
            for (Outpoint output : outputs) {
                if (output.getTransactionId().equalsIgnoreCase(input.getTxHash())
                        && output.getOutputIndex() == input.getTxIndex()) {

                    OutpointDetails outpoint = new OutpointDetails();
                    outpoint.setTransactionId(output.getTransactionId());
                    outpoint.setOutputIndex(output.getOutputIndex());
                    outpoint.setScriptPubKey(output.getScriptPubKey());
                    String address = BitcoinTools.getAddressByScriptPuKey(output.getScriptPubKey(),coinType);
                    //TODO 处理多签
                    // outpoint.setRedeemScript(multiSigRedeemScripts.get(output.getAddress()));

                    if (addressKey.containsKey(address)) {
                        RawTransaction signingTx = RawTransaction.stripInputScripts(rawTx);
                        byte[] sigData;

                        for (RawInput sigInput : signingTx.getInputs()) {
                            if (sigInput.getTxHash().equalsIgnoreCase(outpoint.getTransactionId())
                                    && sigInput.getTxIndex() == outpoint.getOutputIndex()) {
                                // This is the input we're processing, fill it and sign it
                                if (BitcoinTools.isMultiSigAddress(address)) {
                                    sigInput.setScript(outpoint.getRedeemScript());
                                } else {
                                    sigInput.setScript(outpoint.getScriptPubKey());
                                }

                                String sigString = signingTx.encode();

                                if("UBTC".equals(coinType)){
                                    byte[] hashTypeBytes = ByteUtilities.stripLeadingNullBytes(BigInteger.valueOf(9).toByteArray());
                                    hashTypeBytes = ByteUtilities.leftPad(hashTypeBytes, 4, (byte) 0x00);
                                    hashTypeBytes = ByteUtilities.flipEndian(hashTypeBytes);

                                    sigString = sigString + ByteUtilities.toHexString(hashTypeBytes) + "02" + ByteUtilities.toHexString("ub".getBytes());
                                }else{
                                    byte[] hashTypeBytes = ByteUtilities.stripLeadingNullBytes(BigInteger.valueOf(1).toByteArray());
                                    hashTypeBytes = ByteUtilities.leftPad(hashTypeBytes, 4, (byte) 0x00);
                                    hashTypeBytes = ByteUtilities.flipEndian(hashTypeBytes);

                                    sigString = sigString + ByteUtilities.toHexString(hashTypeBytes);
                                }

                                try {
                                    sigData = ByteUtilities.toByteArray(sigString);
                                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                                    sigData = md.digest(md.digest(sigData));
                                    LinkedList<String> inputSigData = new LinkedList<>();
                                    //获取地址对应的私钥
                                    inputSigData.add(addressKey.get(address));
                                    inputSigData.add(input.getTxHash());
                                    inputSigData.add(Integer.toString(input.getTxIndex()));
                                    inputSigData.add(output.getRedeemScript());
                                    inputSigData.add(ByteUtilities.toHexString(sigData));
                                    signatureData.add(inputSigData);
                                } catch (Exception e) {
                                    LOGGER.error(null, e);
                                }
                            }
                        }
                    }
                }
            }
        }
        return signatureData;
    }

    public Iterable<Iterable<String>> signWithPrivateKey(Iterable<Iterable<String>> data) {
        return signWithPrivateKey(data, false);
    }

    private Iterable<Iterable<String>> signWithPrivateKey(Iterable<Iterable<String>> data, boolean onlyMatching) {

        Iterator<Iterable<String>> signatureData = data.iterator();
        LinkedList<Iterable<String>> signatures = new LinkedList<>();
        while (signatureData.hasNext()) {
            Iterator<String> signatureEntry = signatureData.next().iterator();
            LinkedList<String> signatureResults = new LinkedList<>();

            //获取私钥
            String privateKey = signatureEntry.next();

            byte[] addressData = BitcoinTools.getPublicKeyBytes(privateKey);
            int last = (int)addressData[addressData.length-1];
            addressData = Arrays.copyOfRange(addressData, 1, 33);
            byte[] privateKeyBytes = ByteUtilities.toByteArray(BitcoinTools.decodeAddress(privateKey));
            privateKeyBytes = Arrays.copyOfRange(privateKeyBytes, 0, 32);

            //final byte[] addressData = BitcoinTools.getPublicKeyBytes(privateKey);
            //final byte[] privateKeyBytes =
            //        ByteUtilities.toByteArray(BitcoinTools.decodeAddress(privateKey));

            // Hash
            signatureResults.add(signatureEntry.next());
            // Index
            signatureResults.add(signatureEntry.next());
            // Redeem Script
            String redeemScript = signatureEntry.next();
            signatureResults.add(redeemScript);
            Iterable<String> possibleSigners = RawTransaction.decodeRedeemScript(redeemScript);
            if (possibleSigners.iterator().hasNext()) {
            }
            boolean foundSigner = false;
            if (onlyMatching && possibleSigners.iterator().hasNext()) {
                for (String signer : possibleSigners) {
                    if (BitcoinTools.getPublicKey(privateKey).equalsIgnoreCase(signer)) {
                        foundSigner = true;
                    }
                }

                if (!foundSigner) {
                    continue;
                }
            }
            if(last%2==0){
                signatureResults.add("02" + ByteUtilities.toHexString(addressData));
            }else{
                signatureResults.add("03" + ByteUtilities.toHexString(addressData));
            }

           // signatureResults.add(ByteUtilities.toHexString(addressData));
            byte[] sigData = ByteUtilities.toByteArray(signatureEntry.next());
            byte[][] sigResults = Secp256k1.signTransaction(sigData, privateKeyBytes);
            BigInteger lowSlimit =
                    new BigInteger("007FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF5D576E7357A4501DDFE92F46681B20A0", 16);
            BigInteger ourSvalue = new BigInteger(1, sigResults[1]);
            while (ourSvalue.compareTo(lowSlimit) > 0) {
                sigResults = Secp256k1.signTransaction(sigData, privateKeyBytes);
                ourSvalue = new BigInteger(1, sigResults[1]);
            }
            StringBuilder signature = new StringBuilder();
            // Only want R & S, don't need V
            for (int i = 0; i < 2; i++) {
                byte[] sig = sigResults[i];
                signature.append("02");
                byte[] sigSize = BigInteger.valueOf(sig.length).toByteArray();
                sigSize = ByteUtilities.stripLeadingNullBytes(sigSize);
                signature.append(ByteUtilities.toHexString(sigSize));
                signature.append(ByteUtilities.toHexString(sig));
            }

            byte[] sigBytes = ByteUtilities.toByteArray(signature.toString());
            byte[] sigSize = BigInteger.valueOf(sigBytes.length).toByteArray();
            sigSize = ByteUtilities.stripLeadingNullBytes(sigSize);
            String signatureString = ByteUtilities.toHexString(sigSize) + signature.toString();
            signatureString = "30" + signatureString;

            signatureResults.add(signatureString);
            signatureResults.add(ByteUtilities.toHexString(sigData));
            signatures.add(signatureResults);

        }
        return signatures;
    }

    /**
     * 非多签处理
     *
     * @param transaction
     * @param signatureData
     * @return
     */
    public String applySignature(String transaction, Iterable<Iterable<String>> signatureData,String coinType) {
        Iterator<Iterable<String>> signatures = signatureData.iterator();
        RawTransaction rawTx = RawTransaction.parse(transaction);

        while (signatures.hasNext()) {
            Iterable<String> signature = signatures.next();
            Iterator<String> sigDataIterator = signature.iterator();
            rawTx = RawTransaction.parse(transaction);
            String signedTxHash = sigDataIterator.next();
            String signedTxIndex = sigDataIterator.next();
            String signedTxRedeemScript = sigDataIterator.next();
            byte[] addressData = ByteUtilities.toByteArray(sigDataIterator.next());
            byte[] sigData = ByteUtilities.toByteArray(sigDataIterator.next());
            byte[] message = ByteUtilities.toByteArray(sigDataIterator.next());

            for (RawInput signedInput : rawTx.getInputs()) {
                if (signedInput.getTxHash().equalsIgnoreCase(signedTxHash) && Integer
                        .toString(signedInput.getTxIndex()).equalsIgnoreCase(signedTxIndex)) {

                    String scriptData = "";
                    byte[] dataSize = RawTransaction.writeVariableStackInt(sigData.length + 1);
                    scriptData += ByteUtilities.toHexString(dataSize);
                    scriptData += ByteUtilities.toHexString(sigData);
                    if("UBTC".equals(coinType)){
                        scriptData += "09";
                    }else{
                        scriptData += "01";
                    }

                    dataSize = RawTransaction.writeVariableStackInt(addressData.length);
                    scriptData += ByteUtilities.toHexString(dataSize);
                    scriptData += ByteUtilities.toHexString(addressData);

                    signedInput.setScript(scriptData);
                    break;
                }
            }
            transaction = rawTx.encode();
        }
        return rawTx.encode();
    }

    public static void main(String[] args){

            byte[] sigData = new  String("c").getBytes();

    }

}
