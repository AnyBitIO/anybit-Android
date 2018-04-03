package com.bitcoin.api.core;

public class CurrencyParametersRecipient {
  private String recipientAddress;
  private String amount;

  public String getRecipientAddress() {
    return recipientAddress;
  }

  public void setRecipientAddress(String recipientAddress) {
    this.recipientAddress = recipientAddress;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "CurrencyParametersRecipient [recipientAddress=" + recipientAddress + ", amount="
        + amount + "]";
  }
}
