package com.cwmd.finance.bank.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cwmd.finance.bank.entity.XmlConstants;

/**
 * 加密报文,解密报文使用
 */
public class DESedeCoder {

	public static final Log log = LogFactory.getLog(DESedeCoder.class);
	
    /**
     * 加密
     * @param key_in
     * @param key_str
     * @return
     */
	public static byte[] encrypt(byte[] key_byte, String key_str){
		SecretKey key = null;
		
		try{
			key = makeDESedeKey(ConvertBase64.decode(key_str));
			//key = makeDESedeKey(DESCoder.asc2bin(key_str));
		}catch(Exception e){
			log.error("密钥生成失败", e);
		}
		if (key == null) {
			log.error("密钥为空");
			return null;
		}
		
		Cipher cipher;
		byte[] result = null;
		try {
			cipher = Cipher.getInstance(XmlConstants.ENCRYPT_DESEDE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			result = cipher.doFinal(key_byte);
		} catch (Exception e) {
			log.error("加密失败", e);
		}

		return result;
	}
	
	/**
	 * 解密
	 * @param key_in
	 * @param key_str
	 * @return
	 */
	public static byte[] decrypt(byte[] key_in, String key_str){
		SecretKey key = null;
		
		try {
			key = makeDESedeKey(ConvertBase64.decode(key_str));
			//key = makeDESedeKey(DESCoder.asc2bin(key_str));
		} catch (Exception e) {
			log.error("密钥生成失败", e);
		}
		if (key == null) {
			log.error("密钥为空");
			return null;
		}

		Cipher cipher;
		byte[] result = null;
		try {
			cipher = Cipher.getInstance(XmlConstants.ENCRYPT_DESEDE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(key_in);
		} catch (Exception e) {
			log.error("解密失败", e);
		}

		return result;
	}
	
	/**
	 * 生成DESede密钥
	 * @param keybyte
	 * @return
	 * @throws Exception
	 */
	private static SecretKey makeDESedeKey(byte[] keybyte) throws Exception{
		DESedeKeySpec keySpec = new DESedeKeySpec(keybyte);
		SecretKeyFactory factory = SecretKeyFactory.getInstance(XmlConstants.ENCRYPT_DESEDE);
		return factory.generateSecret(keySpec);
	}
	
	/**
	 * 生成DESede密钥
	 * @return
	 * @throws Exception
	 */
	public static SecretKey generateDESedeKeys() throws Exception{
		KeyGenerator generator = KeyGenerator.getInstance(XmlConstants.ENCRYPT_DESEDE);
		// 密钥的长度
		generator.init(112);
		return generator.generateKey();
	}
}
