package com.bitcoin.api.validation;


import com.bitcoin.api.currency.Wallet.TransactionDetails;

/**
 * Indicates whether a currency's wallet makes sense to be validated.
 */
public interface Validatable {
  /**
   * Decode a transaction returned by the prepare and/or sign step.
   */
  TransactionDetails decodeRawTransaction(String transaction);
}
