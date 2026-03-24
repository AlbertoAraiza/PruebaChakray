package com.mx.PruebaChakray.core;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEcnryption {
	private final static String ALGORITHM = "AES";
	private final static String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private final static String SALT = "Password1";
	private final static String PROTOCOL = "PBKDF2WithHmacSHA256";
	private final static String ENCODING = "UTF-8";
	private final static int ITERATION_COUNT = 65536;
	private final static int KEY_LENGHT = 256;

	public static String encrypt(String plainText, String password) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(PROTOCOL);
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), ITERATION_COUNT, KEY_LENGHT);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKeySpec keySpec = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);

		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);

		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		//System.out.println("plain text: " + plainText);
		byte[] encrypted = cipher.doFinal(plainText.getBytes(ENCODING));
		byte[] encrypedData = new byte[iv.length + encrypted.length];
		System.arraycopy(iv, 0, encrypedData, 0, iv.length);
		System.arraycopy(encrypted, 0, encrypedData, iv.length, encrypted.length);

		return Base64.getEncoder().encodeToString(encrypedData);
	}

	public static String decrypt(String encryptedText, String password) throws Exception {
		byte[] decoded = Base64.getDecoder().decode(encryptedText);
		byte[] iv = new byte[16];
		System.arraycopy(decoded, 0, iv, 0, iv.length);

		SecretKeyFactory factory = SecretKeyFactory.getInstance(PROTOCOL);
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), ITERATION_COUNT, KEY_LENGHT);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKeySpec keySpec = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);

		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
		byte[] encrypted = new byte[decoded.length - 16];
		System.arraycopy(decoded, 16, encrypted, 0, encrypted.length);
		byte[] decrypted = cipher.doFinal(encrypted);
		return new String(decrypted, ENCODING);
	}
}
