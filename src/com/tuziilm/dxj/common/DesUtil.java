package com.tuziilm.dxj.common;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DesUtil {
//	private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
//	private static final String key = "yiranhan";
//
//
//	/**
//	 * 加密
//	 * @param data
//	 * @return
//	 * @throws Exception
//	 */
//	public static String encode(String data){
//		try {
//			return encode(data.getBytes());
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return "";
//	}
//
//	private static String encode(byte[] data) throws Exception {
//		try {
//			DESKeySpec dks = new DESKeySpec(key.getBytes());
//
//			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//			// key的长度不能够小于8位字节
//			Key secretKey = keyFactory.generateSecret(dks);
//			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
//			IvParameterSpec iv = new IvParameterSpec(key.getBytes());
//			AlgorithmParameterSpec paramSpec = iv;
//			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
//
//			byte[] bytes = cipher.doFinal(data);
//
//			return new String(Base64.encode(bytes));
//		} catch (Exception e) {
//			throw new Exception(e);
//		}
//	}
//
//	private static byte[] decode(byte[] data) throws Exception {
//		try {
//			SecureRandom sr = new SecureRandom();
//			DESKeySpec dks = new DESKeySpec(key.getBytes());
//			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//			// key的长度不能够小于8位字节
//			Key secretKey = keyFactory.generateSecret(dks);
//			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
//			IvParameterSpec iv = new IvParameterSpec(key.getBytes());
//			AlgorithmParameterSpec paramSpec = iv;
//			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
//			return cipher.doFinal(data);
//		} catch (Exception e) {
//			throw new Exception(e);
//		}
//	}
//
//	/**
//	 * 解密
//	 * @param data
//	 * @return
//	 */
//	public static String decode(String data) {
//		byte[] datas;
//		try {
//			if (System.getProperty("os.name") != null
//					&& (System.getProperty("os.name").equalsIgnoreCase("sunos") || System
//							.getProperty("os.name").equalsIgnoreCase("linux"))) {
//				datas = decode(Base64.decode(data));
//			} else {
//				datas = decode(Base64.decode(data));
//			}
//			return new String(datas);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "";
//	}
	/** 加密、解密key. */
	private static final String PASSWORD_CRYPT_KEY = "Areyoukiddingme?";
	/** 加密算法,可用 DES,DESede,Blowfish. */
	private final static String ALGORITHM = "DES";
	public static void main(String[] args) throws Exception {
		String md5Password = "656c862ab0fa5e05a873e1d4aa90d3be";
		String str1 = "A0893D27739D8176E13637C6F00FB37EF8050BE24A255C56A6C4FE1F93D6D465EA3B04AD858143B2";
		String str = DesUtil.encrypt(md5Password);
		System.out.println("str: " + str);
		str = DesUtil.decrypt(str1);
		System.out.println("str: " + str);
	}

	/**
	 * 对数据进行DES加密.
	 * @param data 待进行DES加密的数据
	 * @return 返回经过DES加密后的数据
	 * @throws Exception
	 * Creation date: 2007-7-31 - 下午12:06:24
	 */
	public final static String decrypt(String data) throws Exception {
		return new String(decrypt(hex2byte(data.getBytes()),
				PASSWORD_CRYPT_KEY.getBytes()));
	}
	/**
	 * 对用DES加密过的数据进行解密.
	 * @param data DES加密数据
	 * @return 返回解密后的数据
	 * @throws Exception
	 * Creation date: 2007-7-31 - 下午12:07:54
	 */
	public final static String encrypt(String data) throws Exception  {
		return byte2hex(encrypt(data.getBytes(), PASSWORD_CRYPT_KEY
				.getBytes()));
	}

	/**
	 * 用指定的key对数据进行DES加密.
	 * @param data 待加密的数据
	 * @param key DES加密的key
	 * @return 返回DES加密后的数据
	 * @throws Exception
	 * Creation date: 2007-7-31 - 下午12:09:03
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(data);
	}
	/**
	 * 用指定的key对数据进行DES解密.
	 * @param data 待解密的数据
	 * @param key DES解密的key
	 * @return 返回DES解密后的数据
	 * @throws Exception
	 * Creation date: 2007-7-31 - 下午12:10:34
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(data);
	}
	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}
}
