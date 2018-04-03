package com.bitcoin.api.validation;

import java.math.BigDecimal;

/**
 * Configuration for rate limiting of transactions.
 */
public interface ValidatorConfiguration {
  /**
   * Return 0 if not applicable.
   */
  BigDecimal getMaxAmountPerHour();

  /**
   * Return 0 if not applicable.
   */
  BigDecimal getMaxAmountPerDay();

  /**
   * Return 0 if not applicable.
   */
  BigDecimal getMaxAmountPerTransaction();
}
