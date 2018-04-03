package com.bitcoin.api.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CurrencyParameters {
  @JsonProperty
  private String currencySymbol;

  @JsonProperty("")
  private String userKey;

  @JsonProperty
  private List<String> account;

  @JsonProperty
  private String callback;

  @JsonProperty
  private List<CurrencyParametersRecipient> receivingAccount;

  @JsonProperty
  private String transactionData;

  @JsonProperty
  private String options;

  public String getCurrencySymbol() {
    return currencySymbol;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public String getUserKey() {
    return userKey;
  }

  public void setUserKey(String userKey) {
    this.userKey = userKey;
  }

  public List<String> getAccount() {
    return account;
  }

  public void setAccount(List<String> account) {
    this.account = account;
  }

  public String getCallback() {
    return callback;
  }

  public void setCallback(String callback) {
    this.callback = callback;
  }

  public List<CurrencyParametersRecipient> getReceivingAccount() {
    return receivingAccount;
  }

  public void setReceivingAccount(List<CurrencyParametersRecipient> receivingAccount) {
    this.receivingAccount = receivingAccount;
  }

  public String getTransactionData() {
    return transactionData;
  }

  public void setTransactionData(String transactionData) {
    this.transactionData = transactionData;
  }

  public String getOptions() {
    return options;
  }

  public void setOptions(String options) {
    this.options = options;
  }

  @Override
  public String toString() {
    return "CurrencyParameters{" + "currencySymbol='" + currencySymbol + '\'' + ", userKey='"
        + userKey + '\'' + ", account=" + account + ", callback='" + callback + '\''
        + ", receivingAccount=" + receivingAccount + ", transactionData='" + transactionData + '\''
        + ", options='" + options + '\'' + '}';
  }
}
