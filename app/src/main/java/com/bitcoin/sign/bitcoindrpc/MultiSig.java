package com.bitcoin.sign.bitcoindrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiSig {
  /**
   * The P2SH address for this multisig redeem script.
   */
  @JsonProperty("address")
  private String address;

  /**
   * The multisig redeem script encoded as hex.
   */
  @JsonProperty("redeemScript")
  private String redeemScript;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getRedeemScript() {
    return redeemScript;
  }

  public void setRedeemScript(String redeemScript) {
    this.redeemScript = redeemScript;
  }

  @Override
  public String toString() {
    return "MultiSig [address=" + address + ", redeemScript=" + redeemScript + "]";
  }
}
