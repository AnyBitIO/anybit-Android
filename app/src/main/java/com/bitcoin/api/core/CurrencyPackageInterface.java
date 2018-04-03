package com.bitcoin.api.core;


import com.bitcoin.api.currency.CurrencyConfiguration;
import com.bitcoin.api.currency.Monitor;
import com.bitcoin.api.currency.Wallet;

public interface CurrencyPackageInterface {
  CurrencyConfiguration getConfiguration();

  void setConfiguration(CurrencyConfiguration configuration);

  Wallet getWallet();

  void setWallet(Wallet wallet);

  Monitor getMonitor();

  void setMonitor(Monitor monitor);
}
