package com.bitcoin.sign.bitcoindrpc;

import com.googlecode.jsonrpc4j.JsonRpcMethod;

/**
 * Utility RPCs.
 *
 * <p>CreateMultiSig: creates a P2SH multi-signature address. EstimateFee: estimates the transaction
 * fee per kilobyte that needs to be paid for a transaction to be included within a certain number
 * of blocks. New in 0.10.0 PENDING: EstimatePriority: estimates the priority that a transaction
 * needs in order to be included within a certain number of blocks as a free high-priority
 * transaction. New in 0.10.0 ValidateAddress: returns information about the given Bitcoin address.
 * PENDING: VerifyMessage: verifies a signed message
 *
 * @author dquintela
 */
public interface UtilityRpc {

  /**
   * CreateMultiSig.
   *
   * <p>The createmultisig RPC creates a P2SH multi-signature address.
   *
   * @param nrequired The minimum (m) number of signatures required to spend this m-of-n multisig
   *                  script
   * @param keys      the full public keys, or addresses for known public keys
   * @return P2SH address and hex-encoded redeem script
   */
  @JsonRpcMethod("createmultisig")
  MultiSig createmultisig(int nrequired, String[] keys);
}
