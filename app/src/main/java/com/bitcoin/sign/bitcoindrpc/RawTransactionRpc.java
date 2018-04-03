package com.bitcoin.sign.bitcoindrpc;

import com.googlecode.jsonrpc4j.JsonRpcMethod;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Raw Transaction RPCs.
 *
 * <p>https://bitcoin.org/en/developer-reference
 *
 * @author dquintela
 */
public interface RawTransactionRpc {
  /**
   * CreateRawTransaction.
   *
   * <p>The createrawtransaction RPC creates an unsigned serialized transaction that spends a
   * previous output to a new output with a P2PKH or P2SH address. The transaction is not stored in
   * the wallet or transmitted to the network.
   *
   * @param unspentOutputs references to previous unspent outputs
   * @param addressAmounts P2PKH or P2SH addresses and amounts A key/value pair with the address to
   *                       pay as a string (key) and the amount to pay that address (value) in
   *                       bitcoins
   * @return the unsigned raw transaction in hex
   */
  @JsonRpcMethod("createrawtransaction")
  String createrawtransaction(Outpoint[] unspentOutputs, Map<String, BigDecimal> addressAmounts);

  /**
   * GetRawTransaction.
   *
   * <p>The getrawtransaction RPC gets a hex-encoded serialized transaction or a JSON object
   * describing the transaction. By default, Bitcoin Core only stores complete transaction data for
   * UTXOs and your own transactions, so the RPC may fail on historic transactions unless you use
   * the non-default txindex=1 in your Bitcoin Core startup settings.
   *
   * <p>Note: if you begin using txindex=1 after downloading the block chain, you must rebuild your
   * indexes by starting Bitcoin Core with the option -reindex. This may take several hours to
   * complete, during which time your node will not process new blocks or transactions. This reindex
   * only needs to be done once.
   *
   * @param transactionId The TXID of the transaction to get, encoded as hex in RPC byte order
   * @param verbose       Set to 0 (the default) to return the serialized transaction as hex. Set to
   *                      1 to return a decoded transaction
   */
  @JsonRpcMethod("getrawtransaction")
  String getrawtransaction(String transactionId);

  /**
   * SendRawTransaction.
   *
   * <p>The sendrawtransaction RPC validates a transaction and broadcasts it to the peer-to-peer
   * network.
   *
   * @param transaction   The serialized transaction to broadcast encoded as hex
   * @param allowHighFees Set to true to allow the transaction to pay a high transaction fee. Set to
   *                      false (the default) to prevent Bitcoin Core from broadcasting the
   *                      transaction if it includes a high fee. Transaction fees are the sum of the
   *                      inputs minus the sum of the outputs, so this high fees check helps ensures
   *                      user including a change address to return most of the difference back to
   *                      themselves
   * @return If the transaction was accepted by the node for broadcast, this will be the TXID of the
   *         transaction encoded as hex in RPC byte order. If the transaction was rejected by the
   *         node, this will set to null, the JSON-RPC error field will be set to a code, and the
   *         JSON-RPC message field may contain an informative error message
   */
  @JsonRpcMethod("sendrawtransaction")
  String sendrawtransaction(String transaction, boolean allowHighFees);

  /**
   * SignRawTransaction.
   *
   * <p>The signrawtransaction RPC signs a transaction in the serialized transaction format using
   * private keys stored in the wallet or provided in the call.
   *
   * @param transaction The transaction to sign as a serialized transaction
   * @param outputs     The previous outputs being spent by this transaction
   * @param privateKeys An array holding private keys. If any keys are provided, only they will be
   *                    used to sign the transaction (even if the wallet has other matching keys).
   *                    If this array is empty or not used, and wallet support is enabled, keys from
   *                    the wallet will be used A private key in base58check format to use to create
   *                    a signature for this transaction
   * @param sigHash     The type of signature hash to use for all of the signatures performed. (You
   *                    must use separate calls to the signrawtransaction RPC if you want to use
   *                    different signature hash types for different signatures. The allowed values
   *                    are: ALL, NONE, SINGLE, ALL|ANYONECANPAY, NONE|ANYONECANPAY, and
   *                    SINGLE|ANYONECANPAY
   */
  @JsonRpcMethod("signrawtransaction")
  SignedTransaction signrawtransaction(String transaction, OutpointDetails[] outputs,
                                       String[] privateKeys, SigHash sigHash);

  @JsonRpcMethod("decoderawtransaction")
  Map<String, Object> decoderawtransaction(String transaction);
}
