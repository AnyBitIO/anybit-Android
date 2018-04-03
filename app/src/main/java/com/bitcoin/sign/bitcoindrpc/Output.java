package com.bitcoin.sign.bitcoindrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Output extends Outpoint {
  /**
   * The P2PKH or P2SH address the output paid. Only returned for P2PKH or P2SH output scripts
   */
  @JsonProperty("address")
  private String address;

  /**
   * If the address returned belongs to an account, this is the account. Otherwise not returned
   */
  @JsonProperty("account")
  private String account;

  /**
   * The output script paid, encoded as hex.
   */
  @JsonProperty("scriptPubKey")
  private String scriptPubKey;

  /**
   * If the output is a P2SH whose script belongs to this wallet, this is the redeem script.
   */
  @JsonProperty("redeemScript")
  private String redeemScript;

  /**
   * The amount paid to the output in bitcoins.
   */
  @JsonProperty("amount")
  private BigDecimal amount;

  /**
   * The number of confirmations received for the transaction containing this output.
   */
  @JsonProperty("confirmations")
  private long confirmations;

  /**
   * Added in Bitcoin Core 0.10.0 Set to true if the private key or keys needed to spend this output
   * are part of the wallet. Set to false if not (such as for watch-only addresses)
   */
  @JsonProperty("spendable")
  private boolean spendable;

  @JsonProperty("solvable")
  private boolean solvable;

  @JsonProperty("safe")
  private String safe;

  public String getSafe() {
    return safe;
  }

  public void setSafe(String safe) {
    this.safe = safe;
  }

  public boolean isSolvable() {
    return solvable;
  }

  public void setSolvable(boolean solvable) {
    this.solvable = solvable;
  }

  @Override
  public String toString() {
    return "Output [address=" + address + ", account=" + account + ", scriptPubKey=" + scriptPubKey
        + ", redeemScript=" + redeemScript + ", amount=" + amount + ", confirmations="
        + confirmations + ", spendable=" + spendable + "]";
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
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((account == null) ? 0 : account.hashCode());
    result = prime * result + ((address == null) ? 0 : address.hashCode());
    result = prime * result + ((amount == null) ? 0 : amount.hashCode());
    result = prime * result + (int) (confirmations ^ (confirmations >>> 32));
    result = prime * result + ((redeemScript == null) ? 0 : redeemScript.hashCode());
    result = prime * result + ((scriptPubKey == null) ? 0 : scriptPubKey.hashCode());
    result = prime * result + (spendable ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Output other = (Output) obj;
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
    return spendable == other.spendable;
  }
}
