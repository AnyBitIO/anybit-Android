package com.bitcoin.common.crypto;


import com.bitcoin.common.ByteUtilities;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class Aes {
  private static final Logger LOGGER = LoggerFactory.getLogger(Aes.class);
  private static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
  private static final String RANDOM_NUMBER_ALGORITHM_PROVIDER = "SUN";

  /**
   * Generate a random IV.
   */
  public static byte[] generateIv() {
    SecureRandom secureRandom;
    try {
      secureRandom =
          SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM, RANDOM_NUMBER_ALGORITHM_PROVIDER);
    } catch (Exception e) {
      LOGGER.error(null, e);
      secureRandom = new SecureRandom();
    }

    byte[] iv = new byte[16];
    secureRandom.nextBytes(iv);
    return iv;
  }

  /**
   * Generate a key from a password and salt.
   */
  public static byte[] generateKey(String password, byte[] salt) {
    try {
      PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, 50, 256);
      SecretKeyFactory keyFactory = SecretKeyFactory
          .getInstance("PBEWithSHA256And256BitAES-CBC-BC", new BouncyCastleProvider());
      SecretKeySpec secretKey =
          new SecretKeySpec(keyFactory.generateSecret(pbeKeySpec).getEncoded(), "AES");

      return secretKey.getEncoded();

    } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
      LOGGER.error(null, e);
      return new byte[0];
    }
  }

  /**
   * Encrypt data using the provided key and IV data.
   */
  public static String encrypt(byte[] key, byte[] iv, String encString) {
    try {
      return Aes.transform(key, iv, encString, true);
    } catch (Exception e) {
      LOGGER.error(null, e);
      return "";
    }
  }

  /**
   * Decrypt data using the provided key and IV data.
   */
  public static String decrypt(byte[] key, byte[] iv, String encString) {
    try {
      return Aes.transform(key, iv, encString, false);
    } catch (Exception e) {
      LOGGER.error(null, e);
      return "";
    }
  }

  private static String transform(byte[] key, byte[] iv, String encString, boolean encrypt)
      throws Exception {

    // setup cipher parameters with key and IV
    KeyParameter keyParam = new KeyParameter(key);
    CipherParameters params = new ParametersWithIV(keyParam, iv);

    // setup AES cipher in CBC mode with PKCS7 padding
    BlockCipherPadding padding = new PKCS7Padding();
    BufferedBlockCipher cipher =
        new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()), padding);
    cipher.reset();
    cipher.init(encrypt, params);

    // create a temporary buffer to decode into (it'll include padding)
    byte[] encData = ByteUtilities.toByteArray(encString);
    byte[] buf = new byte[cipher.getOutputSize(encData.length)];
    int len = cipher.processBytes(encData, 0, encData.length, buf, 0);
    len += cipher.doFinal(buf, len);

    // remove padding
    byte[] out = new byte[len];
    System.arraycopy(buf, 0, out, 0, len);

    // return string representation of decoded bytes
    return ByteUtilities.toHexString(out);
  }
}
