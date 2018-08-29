/**
 *
 */
package com.utility;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

/**
 * @author USER
 *
 */
public class AESCryptoUtils extends TestCase implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(AESCryptoUtils.class);
	private static SecretKeySpec secretKey;
	private static final String CONST_UTF_8 = "UTF-8";
	private static final String CONST_SHA_1 = "SHA-1";
	private static final String CONST_AES = "AES";
	private static final String CONST_CIPHER_PADDING = "AES/ECB/PKCS5Padding";
	@SuppressWarnings("unused")
	private static final String CONST_MESSAGE_DIGEST_SHA_512 = "SHA-512";
	private byte[] key;

	/**
	 *
	 */
	public AESCryptoUtils(String secret) {
		super();
		setKey(secret);
	}

	/**
	 *
	 * @param myKey
	 */
	@Ignore
	private void setKey(String myKey) {
		MessageDigest md = null;

		try {
			String salt = "JustASimpleSaltString!234#$%&";
			String full = salt + myKey;
			byte[] data = full.getBytes(CONST_UTF_8);
			md = MessageDigest.getInstance(CONST_SHA_1);
			byte[] hash = md.digest(data);
			key = Arrays.copyOf(hash, 16);
			secretKey = new SecretKeySpec(key, CONST_AES);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 *
	 * @param strToEncrypt
	 * @return
	 */
	@Ignore
	public String encrypt(String strToEncrypt) {
		String rv = "";

		try {
			Cipher cipher = Cipher.getInstance(CONST_CIPHER_PADDING);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			rv = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes(CONST_UTF_8)));

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return rv;
	}

	/**
	 *
	 * @param strToDecrypt
	 * @return
	 */
	@Ignore
	public String decrypt(String strToDecrypt) {
		String rv = "";

		try {
			Cipher cipher = Cipher.getInstance(CONST_CIPHER_PADDING);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			rv = String.valueOf(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return rv;
	}

	public static void main(String[] args) {
		String secretKey = "test!@#$1234";
		String origString = "testing";
		AESCryptoUtils aes = new AESCryptoUtils(secretKey);
		String encString = aes.encrypt(origString);
		String decString = aes.decrypt(encString);

		logger.info("encString : {}", encString);
		logger.info("decString : ", decString);

	}

}
