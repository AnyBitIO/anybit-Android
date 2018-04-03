package com.bitcoin.sign.stubrpc;

import com.bitcoin.sign.bitcoindrpc.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BitcoinTestRpc implements BitcoindRpc {

  @Override
  public BlockChainInfo getblockchaininfo() {
    BlockChainInfo info = new BlockChainInfo();
    info.setChain(BlockChainName.regtest);
    return info;
  }

  @Override
  public long getBlockCount() {
    return 0;
  }

  @Override
  public String getBlockHash(long blockHeight) {
    return "12abeed";
  }

  @Override
  public Map<String, Object> getBlock(String blockHash) {
    HashMap<String, Object> result = new HashMap<>();
    result.put("time", ((Long) (System.currentTimeMillis() / 1000)).toString());
    return result;
  }

  @Override
  public String createrawtransaction(Outpoint[] unspentOutputs,
      Map<String, BigDecimal> addressAmounts) {
    return "deadbeefdeadbeefdeadbeefdeadbeef";
  }

  @Override
  public String getrawtransaction(String transactionId) {
    return null;
  }

  @Override
  public String sendrawtransaction(String transaction, boolean allowHighFees) {
    return "0x1234567890";
  }

  @Override
  public SignedTransaction signrawtransaction(String transaction, OutpointDetails[] outputs,
                                              String[] privateKeys, SigHash sigHash) {
    SignedTransaction signedTx = new SignedTransaction();
    signedTx.setTransaction(transaction + "1234");
    signedTx.setComplete(true);
    return signedTx;
  }

  @Override
  public Map<String, Object> decoderawtransaction(String transaction) {
    return null;
  }

  @Override
  public String addmultisigaddress(int nrequired, String[] keys, String account) {
    return account;
  }

  @Override
  public MultiSig createmultisig(int nrequired, String[] keys) {
    MultiSig multiSig = new MultiSig();
    multiSig.setAddress("2sjJ8zfnqZbkYj79EBtJLN4CDNPRg4s9xn");
    multiSig.setRedeemScript("");
    return multiSig;
  }

  @Override
  public String[] getaddressesbyaccount(String accountName) {
    return new String[]{};
  }

  @Override
  public String getnewaddress(String accountName) {
    return accountName;
  }

  @Override
  public void importaddress(String addressOrScript, String account, boolean rescan) {

  }

  @Override
  public Map<String, BigDecimal> listaccounts(int confirmations, boolean includeWatchOnly) {
    return null;
  }

  @Override
  public Output[] listunspent(int minimumConfirmations, int maximumConfirmations,
      String[] addresses) {

    //Output output = new Output();
    //output.setAccount("");
    //output.setAddress(new BitcoinWallet(new BitcoinConfiguration()).createAddress("deadbeef"));
    //output.setAmount(BigDecimal.valueOf(30));
    //output.setConfirmations(minimumConfirmations);
    //output.setOutputIndex(1);
    //output.setTransactionId("00000000000000000000000000000000000000000000000000000000deadbeef");
    //String decodedAddress = BitcoinTools.decodeAddress(output.getAddress());
    //byte[] addressBytes = ByteUtilities.toByteArray(decodedAddress);
    //String scriptData = "76a914";
    //scriptData += ByteUtilities.toHexString(addressBytes);
    //scriptData += "88ac";
    //output.setScriptPubKey(scriptData);

    Output output = new Output();
    output.setAccount("");
    output.setAddress("n1i8CRPgUdpGctP8mw4yvi2ZwZr3tyk2NP");
    output.setAmount(BigDecimal.valueOf(0.91585490));
    output.setConfirmations(minimumConfirmations);
    output.setOutputIndex(0);
    output.setTransactionId("6e1891a4d73cfa4c29c92b867b261b3fd84b35588e80b59a075ce894142381b4");
    output.setScriptPubKey("76a914dd7f5b96a40d3a63714b5c58be515a631ca2225688ac");

    return new Output[]{output};
  }

  @Override
  public Payment[] listtransactions(String account, int numberToReturn, int numberToSkip,
      boolean includeWatchOnly) {
    return new Payment[]{};
  }

  @Override
  public Map<String, Object> gettransaction(String txid, boolean includeWatchOnly) {

    return null;
  }
}
