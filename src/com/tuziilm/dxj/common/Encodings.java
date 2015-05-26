package com.tuziilm.dxj.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;

/**
 * 编码相关工具类
 * @author <a href="tuziilm@163.com">tuziilm</a>
 *
 */
public final class Encodings {
	/**
	 * 转换成Unicode
	 * @param input
	 * @return
	 */
	public static String unicode(String input){
		if(input==null || input.isEmpty()){
			return "";
		}
		try {
			char[] data=Hex.encodeHex(input.getBytes("UNICODE"), false);
			return new String(data,4,data.length-4);
		} catch (UnsupportedEncodingException e) {
			//never happen
		}
		return "";
	}
}
