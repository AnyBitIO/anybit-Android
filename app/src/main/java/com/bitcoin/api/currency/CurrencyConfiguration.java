package com.bitcoin.api.currency;

/**
 * Common configuration that each currency needs to provide to cosigner-core.
 *
 * @author Tom
 */
public interface CurrencyConfiguration {
  /**
   * Currency symbol.
   */
  String getCurrencySymbol();

  /**
   * Experimental placeholder Will return what kind of signatures the currency needs. - Async
   * multi-sig (ETH) - Everyone signs before submitting (BTC) - 2-phase if that's different
   */
  SigningType getSigningType();

  /**
   * Minimum number of signatures required in a multi-sig address.
   */
  int getMinSignatures();

  /**
   * Indicates if the currency can send from more than one address in a single transaction.
   */
  boolean hasMultipleSenders();

  /**
   * Indicates if the currency can send to more than one address in a single transaction.
   */
  boolean hasMultipleRecipients();
}
