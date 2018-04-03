package com.bitcoin.api.currency;

public interface EthereumTransactionConfiguration {
  long getGasPrice();

  long getSimpleTxGas();

  long getContractGas();

  long getWeiMultiplier();
}
