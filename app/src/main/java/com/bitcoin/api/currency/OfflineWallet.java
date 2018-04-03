package com.bitcoin.api.currency;

public interface OfflineWallet {
  /**
   * Generate a private key compatible with the wallet.
   */
  String generatePrivateKey();

  /**
   * Generate public key from a private one.
   */
  String generatePublicKey(String privateKey);

}
