package com.bitcoin.common;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.SP800SecureRandomBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public class DeterministicRng {
  private static final Logger LOGGER = LoggerFactory.getLogger(DeterministicRng.class);
  private static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
  private static final String RANDOM_NUMBER_ALGORITHM_PROVIDER = "SUN";

  /**
   * Generates a deterministic SecureRandom based on the userKey and serverKey.
   */
  public static SecureRandom getSecureRandom(byte[] userKey, byte[] serverKey) {
    SecureRandom secureRandom;
    try {
      secureRandom =
          SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM, RANDOM_NUMBER_ALGORITHM_PROVIDER);
    } catch (Exception e) {
      LOGGER.error(null, e);
      secureRandom = new SecureRandom();
    }

    byte[] userSeed = new byte[Math.max(userKey.length, serverKey.length)];

    // XOR the key parts to get our seed, repeating them if they lengths
    // don't match
    for (int i = 0; i < userSeed.length; i++) {
      userSeed[i] = (byte) (userKey[i % userKey.length] ^ serverKey[i % serverKey.length]);
    }

    // Set up out private key variables
    secureRandom.setSeed(userSeed);

    final SecureRandom finalSecureRandom = secureRandom;
    SP800SecureRandomBuilder sp800SecureRandomBuilder =
        new SP800SecureRandomBuilder(i -> new EntropySource() {
          @Override
          public boolean isPredictionResistant() {
            return true;
          }

          @Override
          public byte[] getEntropy() {
            byte[] entropy = new byte[(i + 7) / 8];
            finalSecureRandom.nextBytes(entropy);
            return entropy;
          }

          @Override
          public int entropySize() {
            return i;
          }
        });
    sp800SecureRandomBuilder.setPersonalizationString(userKey);
    secureRandom = sp800SecureRandomBuilder.buildHash(new SHA512Digest(), serverKey, true);
    return secureRandom;
  }
}
