package com.cwmd.finance.bank.encryption;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cwmd.finance.bank.entity.XmlConstants;

/**
 * 密钥传输交换接口加密解密密钥
 * 发送RSA公钥和DES密钥,接收DES密钥
 */
public class DESCoder {

	public static final Log log = LogFactory.getLog(DESCoder.class);
	
	private static final char[] CODES = { '0', '1', '2', '3', '4', '5', 
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' }; 
	
	/**
	 * 对密钥进行DES加密。加密成功后的byte[] 直接传输给客户方。
	 * 
	 * @param key_in : Base64编码的密钥明文
	 * @param mch_no : 商户编号
	 * @return : DES加密后的密钥
	 */
	public byte[] encrypt(byte[] key_byte, String mch_no) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String key_str = mch_no + sdf.format(new Date());
		SecretKey key = null;
		try {
			//key = makeDESKey(ConvertBase64.decode(key_str));
			key = makeDESKey(asc2bin(key_str));
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
			cipher = Cipher.getInstance(XmlConstants.ENCRYPT_DES);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			result = cipher.doFinal(key_byte);
		} catch (Exception e) {
			log.error("加密失败。", e);
		}

		return result;
	}
	
	
	
	
	/**
	 * 对密钥进行DES解密。解密成功后的就是对方的密钥。
	 * 
	 * @param key_in : DES加密后的密钥
	 * @param mch_no : 商户编号
	 * @return : Base64编码的密钥明文
	 */
	public byte[] decrypt(byte[] key_in, String mch_no) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String key_str = mch_no + sdf.format(new Date());
		SecretKey key = null;
		try {
			//key = makeDESKey(ConvertBase64.decode(key_str));
			key = makeDESKey(asc2bin(key_str));
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
			cipher = Cipher.getInstance(XmlConstants.ENCRYPT_DES);
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(key_in);
		} catch (Exception e) {
			log.error("解密失败。", e);
		}

		return result;
	}
	
	
	
	
	
	/**
	 * 生成DES密钥
	 * 
	 * @param keybyte
	 * @return
	 * @throws Exception
	 */
	private static SecretKey makeDESKey(byte[] keybyte) throws Exception {
		DESKeySpec deskeyspec = new DESKeySpec(keybyte);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(XmlConstants.ENCRYPT_DES);
		return keyfactory.generateSecret(deskeyspec);
	}

	
	
	
	
	/**
	 * 将16进制字符串转换成16进制数字数组
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] asc2bin(String hexString) {
		byte[] hexbyte = hexString.getBytes();
		byte[] bitmap = new byte[hexbyte.length / 2];
		for (int i = 0; i < bitmap.length; i++) {
			hexbyte[i * 2] -= hexbyte[i * 2] > '9' ? 7 : 0;
			hexbyte[i * 2 + 1] -= hexbyte[i * 2 + 1] > '9' ? 7 : 0;
			bitmap[i] = (byte) ((hexbyte[i * 2] << 4 & 0xf0) | (hexbyte[i * 2 + 1] & 0x0f));
		}
		return bitmap;
	}
	
	/**
	 * 将16进制数字数组转换成16进制字符串
	 * @param bitmap
	 * @return
	 */
	public static String bin2asc(byte[] bitmap){
		StringBuffer buffer = new StringBuffer(bitmap.length * 2);
		for(int i = 0; i < bitmap.length; i++){
			buffer.append(CODES[(bitmap[i] >>> 4) & 0x0f]); 
			buffer.append(CODES[bitmap[i] & 0x0f]); 
		}
		return buffer.toString();
	}

}
