package com.cwmd.finance.bank.encryption;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ConvertBase64 {

	public static final Log log = LogFactory.getLog(ConvertBase64.class);
	
	public static final BASE64Decoder decoder = new BASE64Decoder();
	public static final BASE64Encoder encoder = new BASE64Encoder();
	
	/**
	 * Base64将字符串转为byte
	 * @param key
	 * @return
	 */
	public static byte[] decode(String key){
		
		try {
			if(key != null && !"".equals(key.trim()))
				return decoder.decodeBuffer(key);
			else
				return null;
		} catch (IOException e) {
			log.error("Base64解密失败", e);
		}
		return null;
	}
	
	/**
	 * Base64将byte转为字符串
	 * @param result
	 * @return
	 */
	public static String encode(byte[] result){
		if(result != null && result.length > 0){
			return encoder.encodeBuffer(result);
		}
		return null;
	}
	
	/**
	 * Base64加密，转byte为byte
	 * @param data
	 * @return
	 */
	public static byte[] encodeBase64(byte[] data){
		if(data != null && data.length > 0){
			return Base64.encodeBase64(data);
		}
		return null;
	}
	
	/**
	 * Base64解密，转byte为byte
	 * @param data
	 * @return
	 */
	public static byte[] decodeBase64(byte[] data){
		if(data != null && data.length > 0){
			return Base64.decodeBase64(data);
		}
		return null;
	}
}
