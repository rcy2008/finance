package com.cwmd.finance.bank.encryption;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cwmd.finance.bank.entity.XmlConstants;

public class MD5RSACoder {

	private static final Log log = LogFactory.getLog(MD5RSACoder.class);
	
	
	/**
	 * 根据value值生成PublicKey
	 * @param keyValue
	 * @return
	 */
	public static PublicKey makePublicKey(String keyValue){
		try {
			if(keyValue == null){
				return null;
			}else{
				X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
						ConvertBase64.decode(keyValue));
				KeyFactory keyFactory = KeyFactory.getInstance(XmlConstants.ENCRYPT_RSA);
				return keyFactory.generatePublic(keySpec);
			}
		} catch (InvalidKeySpecException e) {
			log.error("无效加密方法", e);
		} catch (NoSuchAlgorithmException e) {
			log.error("无此加密算法", e);
		}
		return null;
	}
	
	/**
	 * 根据value值生成PrivateKey
	 * @param keyValue
	 * @return
	 */
	public static PrivateKey makePrivateKey(String keyValue){
		try {
			if(keyValue == null){
				return null;
			}else{
				PKCS8EncodedKeySpec keySpec =new PKCS8EncodedKeySpec(
						ConvertBase64.decode(keyValue)); 
				KeyFactory keyFactory = KeyFactory.getInstance(XmlConstants.ENCRYPT_RSA);
				return keyFactory.generatePrivate(keySpec);
			}
		} catch (InvalidKeySpecException e) {
			log.error("无效加密方法", e);
		} catch (NoSuchAlgorithmException e) {
			log.error("无此加密算法", e);
		}
		return null;
	}
	
	/**
	 * 根据内容生成签名
	 * @param info
	 * @param privateKey
	 * @return
	 */
	public static byte[] generateSignature(byte[] info, PrivateKey privateKey){
		try {
			if(info == null || privateKey == null){
				return null;
			}
			Signature signature = Signature.getInstance(XmlConstants.ENCRYPT_MD5_RSA);
			signature.initSign(privateKey);
			signature.update(info);
			return signature.sign();
		} catch (NoSuchAlgorithmException e) {
			log.error("无此加密算法", e);
		} catch (InvalidKeyException e) {
			log.error("无效加密方法", e);
		} catch (SignatureException e) {
			log.error("签名错误", e);
		} 
		return null;
	}
	
	/**
	 * 判断签名是否相同
	 * @param info
	 * @param publicKey 
	 * @param sign
	 * @return
	 */
	public static Boolean isSignatureEffective(byte[] info, 
			PublicKey publicKey, byte[] sign){
		
		try {
			if(info == null || publicKey == null){
				return false;
			}
			Signature signature = Signature
					.getInstance(XmlConstants.ENCRYPT_MD5_RSA);
			signature.initVerify(publicKey); 
			signature.update(info); 
			if(signature.verify(sign)){
				return true;
			}else{
				return false;
			}
			
		} catch (NoSuchAlgorithmException e) {
			log.error("无此加密算法", e);
		} catch (SignatureException e) {
			log.error("签名错误", e);
		} catch (InvalidKeyException e) {
			log.error("无效加密方法", e);
		} 
		
		return false;
	}
	
	/**
	 * 生成RSA密钥，公钥发送到银行
	 * @return
	 */
	public static KeyPair generateKeyPair(){
		try {
			KeyPairGenerator keyGen = KeyPairGenerator
					.getInstance(XmlConstants.ENCRYPT_RSA);
//			SecureRandom secrand = new SecureRandom();
//			secrand.setSeed("NAN4400000127".getBytes());
//			keyGen.initialize(1024, secrand);
			keyGen.initialize(1024);
			return keyGen.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			log.error("无此加密算法", e);
		}
		
		return null;
	}
	
}
