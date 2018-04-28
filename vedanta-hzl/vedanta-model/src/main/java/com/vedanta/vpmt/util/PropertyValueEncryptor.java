package com.vedanta.vpmt.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;

public class PropertyValueEncryptor {


  static PooledPBEStringEncryptor encryptor = null;
  private static final String ALGORITHM = "PBEWITHMD5ANDDES";
  private static final String P_FIELD = "aff5bae9d8e0c8b4f20de6de17c1dd87";

  static {
    encryptor = new PooledPBEStringEncryptor();
    encryptor.setPoolSize(4);
    encryptor.setPassword(P_FIELD);
    encryptor.setAlgorithm(ALGORITHM);
  }

  public static String encrypt(String input) {
    return encryptor.encrypt(input);
  }

  public static String decrypt(String encryptedMessage) {
    return encryptor.decrypt(encryptedMessage);
  }
}
