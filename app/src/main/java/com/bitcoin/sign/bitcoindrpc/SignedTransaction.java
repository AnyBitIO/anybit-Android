package com.bitcoin.sign.bitcoindrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class SignedTransaction {
  /**
   * The resulting serialized transaction encoded as hex with any signatures made inserted. If no
   * signatures were made, this will be the same transaction provided in parameter #1
   */
  @JsonProperty("hex")
  private String transaction;

  /**
   * The value true if transaction is fully signed; the value false if more signatures are
   * required.
   */
  @JsonProperty("complete")
  private boolean complete;

  @JsonProperty("errors")
  private Errors[] errors = new Errors[0];

  public String getTransaction() {
    return transaction;
  }

  public void setTransaction(String transaction) {
    this.transaction = transaction;
  }

  public boolean isComplete() {
    return complete;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }

  /**
   * Any errors that may have occured in signing.
   *
   * @return Array of errors.
   */
  public Errors[] getErrors() {
    Errors[] retArray = new Errors[errors.length];
    System.arraycopy(errors, 0, retArray, 0, errors.length);
    return retArray;
  }

  public void setErrors(Errors[] errors) {
    this.errors = new Errors[errors.length];
    System.arraycopy(errors, 0, this.errors, 0, errors.length);
  }

  @Override
  public String toString() {
    return "SignedTransaction [transaction=" + transaction + ", complete=" + complete + ", errors="
        + Arrays.toString(errors) + "]";
  }

  public static class Errors {
    @JsonProperty("txid")
    private String txid;

    @JsonProperty("vout")
    private String vout;

    @JsonProperty("scriptSig")
    private String scriptSig;

    @JsonProperty("sequence")
    private String sequence;

    @JsonProperty("error")
    private String error;

    public String getTxid() {
      return txid;
    }

    public void setTxid(String txid) {
      this.txid = txid;
    }

    public String getVout() {
      return vout;
    }

    public void setVout(String vout) {
      this.vout = vout;
    }

    public String getScriptSig() {
      return scriptSig;
    }

    public void setScriptSig(String scriptSig) {
      this.scriptSig = scriptSig;
    }

    public String getSequence() {
      return sequence;
    }

    public void setSequence(String sequence) {
      this.sequence = sequence;
    }

    public String getError() {
      return error;
    }

    public void setError(String error) {
      this.error = error;
    }

    @Override
    public String toString() {
      return "Errors [txid=" + txid + ", vout=" + vout + ", scriptSig=" + scriptSig + ", sequence="
          + sequence + ", error=" + error + "]";
    }
  }
}
