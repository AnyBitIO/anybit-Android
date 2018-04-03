package com.bitcoin.sign.bitcoindrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutpointDetails {
  /**
   * The output’s pubkey script encoded as hex Required.
   */
  @JsonProperty("txid")
  private String transactionId;

  @JsonProperty("vout")
  private long outputIndex;

  @JsonProperty("scriptPubKey")
  private String scriptPubKey;

  /**
   * Optional If the pubkey script was a script hash, this must be the corresponding redeem script.
   */
  @JsonProperty("redeemScript")
  private String redeemScript;

  public OutpointDetails() {
    // Creates an uninitialized data structure.
  }

  /**
   * Create a new outpoint based on an output
   *
   * @param output Output with transaction data.
   */
  public OutpointDetails(Output output) {
    setTransactionId(output.getTransactionId());
    setOutputIndex(output.getOutputIndex());
    setScriptPubKey(output.getScriptPubKey());
    setRedeemScript(output.getRedeemScript());
  }

  public OutpointDetails(Output output, String redeemScript) {
    this(output);
    setRedeemScript(redeemScript);
  }

  /**
   * Create a new outpoint based on transaction data.
   *
   * @param transactionId Transaction hash this outpoint represents.
   * @param outputIndex   Index of the output in the given transaction.
   * @param scriptPubKey  Script that was used on the output.
   * @param redeemScript  Redeem script that was used if this is a P2SH output.
   */
  public OutpointDetails(String transactionId, long outputIndex, String scriptPubKey,
      String redeemScript) {
    setTransactionId(transactionId);
    setOutputIndex(outputIndex);
    setScriptPubKey(scriptPubKey);
    setRedeemScript(redeemScript);
  }

  public String getScriptPubKey() {
    return scriptPubKey;
  }

  public void setScriptPubKey(String scriptPubKey) {
    this.scriptPubKey = scriptPubKey;
  }

  public String getRedeemScript() {
    return redeemScript;
  }

  public void setRedeemScript(String redeemScript) {
    this.redeemScript = redeemScript;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public long getOutputIndex() {
    return outputIndex;
  }

  public void setOutputIndex(long outputIndex) {
    this.outputIndex = outputIndex;
  }

  @Override
  public String toString() {
    return "OutpointDetails [transactionId=" + transactionId + ", outputIndex=" + outputIndex
        + ", scriptPubKey=" + scriptPubKey + ", redeemScript=" + redeemScript + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (outputIndex ^ (outputIndex >>> 32));
    result = prime * result + ((redeemScript == null) ? 0 : redeemScript.hashCode());
    result = prime * result + ((scriptPubKey == null) ? 0 : scriptPubKey.hashCode());
    result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    OutpointDetails other = (OutpointDetails) obj;
    if (outputIndex != other.outputIndex) {
      return false;
    }
    if (redeemScript == null) {
      if (other.redeemScript != null) {
        return false;
      }
    } else if (!redeemScript.equals(other.redeemScript)) {
      return false;
    }
    if (scriptPubKey == null) {
      if (other.scriptPubKey != null) {
        return false;
      }
    } else if (!scriptPubKey.equals(other.scriptPubKey)) {
      return false;
    }
    if (transactionId == null) {
      if (other.transactionId != null) {
        return false;
      }
    } else if (!transactionId.equals(other.transactionId)) {
      return false;
    }
    return true;
  }
}
