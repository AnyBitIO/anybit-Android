package com.bitcoin.sign.bitcoindrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Outpoint {
  /**
   * The TXID of the outpoint encoded as hex in RPC byte order.
   */
  @JsonProperty("txid")
  private String transactionId;

  /**
   * The output index number (vout) of the outpoint; the first output in a transaction is index 0.
   */
  @JsonProperty("vout")
  private long outputIndex;

  @JsonProperty("address")
  private String address;

  @JsonProperty("account")
  private String account;

  @JsonProperty("scriptPubKey")
  private String scriptPubKey;

  @JsonProperty("redeemScript")
  private String redeemScript;

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("confirmations")
  private long confirmations;

  @JsonProperty("spendable")
  private boolean spendable;

  public Outpoint() {
    // Creates an uninitialized data structure.
  }

  public Outpoint(Outpoint other) {
    setTransactionId(other.getTransactionId());
    setOutputIndex(other.getOutputIndex());
  }

  public Outpoint(String transactionId, long outputIndex) {
    setTransactionId(transactionId);
    setOutputIndex(outputIndex);
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
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

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public long getConfirmations() {
    return confirmations;
  }

  public void setConfirmations(long confirmations) {
    this.confirmations = confirmations;
  }

  public boolean isSpendable() {
    return spendable;
  }

  public void setSpendable(boolean spendable) {
    this.spendable = spendable;
  }

  @Override
  public String toString() {
    return "Outpoint [transactionId=" + transactionId + ", outputIndex=" + outputIndex
        + ", address=" + address + ", account=" + account + ", scriptPubKey=" + scriptPubKey
        + ", redeemScript=" + redeemScript + ", amount=" + amount + ", confirmations="
        + confirmations + ", spendable=" + spendable + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((account == null) ? 0 : account.hashCode());
    result = prime * result + ((address == null) ? 0 : address.hashCode());
    result = prime * result + ((amount == null) ? 0 : amount.hashCode());
    result = prime * result + (int) (confirmations ^ (confirmations >>> 32));
    result = prime * result + (int) (outputIndex ^ (outputIndex >>> 32));
    result = prime * result + ((redeemScript == null) ? 0 : redeemScript.hashCode());
    result = prime * result + ((scriptPubKey == null) ? 0 : scriptPubKey.hashCode());
    result = prime * result + (spendable ? 1231 : 1237);
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
    Outpoint other = (Outpoint) obj;
    if (account == null) {
      if (other.account != null) {
        return false;
      }
    } else if (!account.equals(other.account)) {
      return false;
    }
    if (address == null) {
      if (other.address != null) {
        return false;
      }
    } else if (!address.equals(other.address)) {
      return false;
    }
    if (amount == null) {
      if (other.amount != null) {
        return false;
      }
    } else if (!amount.equals(other.amount)) {
      return false;
    }
    if (confirmations != other.confirmations) {
      return false;
    }
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
    if (spendable != other.spendable) {
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
