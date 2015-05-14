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
//	 * ����
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
//			// key�ĳ��Ȳ��ܹ�С��8λ�ֽ�
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
//			// key�ĳ��Ȳ��ܹ�С��8λ�ֽ�
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
//	 * ����
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
	/** ���ܡ�����key. */
	private static final String PASSWORD_CRYPT_KEY = "Areyoukiddingme?";
	/** �����㷨,���� DES,DESede,Blowfish. */
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
	 * �����ݽ���DES����.
	 * @param data ������DES���ܵ�����
	 * @return ���ؾ���DES���ܺ������
	 * @throws Exception
	 * Creation date: 2007-7-31 - ����12:06:24
	 */
	public final static String decrypt(String data) throws Exception {
		return new String(decrypt(hex2byte(data.getBytes()),
				PASSWORD_CRYPT_KEY.getBytes()));
	}
	/**
	 * ����DES���ܹ������ݽ��н���.
	 * @param data DES��������
	 * @return ���ؽ��ܺ������
	 * @throws Exception
	 * Creation date: 2007-7-31 - ����12:07:54
	 */
	public final static String encrypt(String data) throws Exception  {
		return byte2hex(encrypt(data.getBytes(), PASSWORD_CRYPT_KEY
				.getBytes()));
	}

	/**
	 * ��ָ����key�����ݽ���DES����.
	 * @param data �����ܵ�����
	 * @param key DES���ܵ�key
	 * @return ����DES���ܺ������
	 * @throws Exception
	 * Creation date: 2007-7-31 - ����12:09:03
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// DES�㷨Ҫ����һ�������ε������Դ
		SecureRandom sr = new SecureRandom();
		// ��ԭʼ�ܳ����ݴ���DESKeySpec����
		DESKeySpec dks = new DESKeySpec(key);
		// ����һ���ܳ׹�����Ȼ��������DESKeySpecת����
		// һ��SecretKey����
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher����ʵ����ɼ��ܲ���
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// ���ܳ׳�ʼ��Cipher����
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		// ���ڣ���ȡ���ݲ�����
		// ��ʽִ�м��ܲ���
		return cipher.doFinal(data);
	}
	/**
	 * ��ָ����key�����ݽ���DES����.
	 * @param data �����ܵ�����
	 * @param key DES���ܵ�key
	 * @return ����DES���ܺ������
	 * @throws Exception
	 * Creation date: 2007-7-31 - ����12:10:34
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// DES�㷨Ҫ����һ�������ε������Դ
		SecureRandom sr = new SecureRandom();
		// ��ԭʼ�ܳ����ݴ���һ��DESKeySpec����
		DESKeySpec dks = new DESKeySpec(key);
		// ����һ���ܳ׹�����Ȼ��������DESKeySpec����ת����
		// һ��SecretKey����
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher����ʵ����ɽ��ܲ���
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// ���ܳ׳�ʼ��Cipher����
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		// ���ڣ���ȡ���ݲ�����
		// ��ʽִ�н��ܲ���
		return cipher.doFinal(data);
	}
	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("���Ȳ���ż��");
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
