package com.cwmd.finance.bank.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cwmd.finance.bank.encryption.ConvertBase64;
import com.cwmd.finance.bank.encryption.DESedeCoder;
import com.cwmd.finance.bank.encryption.MD5RSACoder;
import com.cwmd.finance.bank.entity.BankException;
import com.cwmd.finance.bank.entity.XmlConstants;

public class NCHttpClient {

	private static final Log log = LogFactory.getLog(NCHttpClient.class);

	/**
	 * 报文发送
	 * @param xmlContent 加密后发送报文
	 * @param url 交易URL
	 * @param signature 报文签名
	 * @return 返回报文
	 */
	public static byte[] getHttpResponse(String xmlContent, String url, 
			String signature) throws BankException{
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		//设置连接超时时间
		client.getHttpConnectionManager().getParams()
			.setConnectionTimeout(XmlConstants.BANK_CONNECTION_TIME_OUT);
		//设置读取超时时间
		client.getHttpConnectionManager().getParams()
			.setSoTimeout(XmlConstants.BANK_READ_TIME_OUT);
		try{
			//设置xml与signature参数
			method.addParameter(XmlConstants.REQ_PARAM_XML, 
					xmlContent);
			method.addParameter(XmlConstants.REQ_PARAM_SIGNATURE, 
					signature);
			
			int statusCode = client.executeMethod(method);
			if(statusCode != HttpStatus.SC_OK){
				log.error("报文发送出错: " + statusCode);
				throw new BankException("HTTP访问异常" + statusCode);
			}else{
				
				InputStream in = method.getResponseBodyAsStream();
				return InputStreamToByte(in);
				
			}
		} catch (HttpException e) {
			log.error("HTTP访问异常", e);
			throw new BankException("HTTP访问异常");
		} catch (IOException e) {
			log.error("IO异常", e);
			throw new BankException("IO异常");
		}finally{
			if(method != null){
				method.releaseConnection();
			}
			if(client != null){
				client.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
	}
	
	/**
	 * 接收银行直接推送报文
	 * @param content 报文内容
	 * @param signature 签名
	 * @param bankDesCode 银行DESede密钥
	 * @param bankRsaCode 银行RSA公钥
	 * @return 签名正确返回解密后的报文 签名不正确返回null
	 */
	public static String getReceiveRequest(String content, String signature,
			String bankDesCode, String bankRsaCode){
		try{
			//报文解密
			byte[] report = DESedeCoder.decrypt(ConvertBase64.decode(content), bankDesCode);
			//签名正确
			if(MD5RSACoder.isSignatureEffective(new String(report).getBytes(XmlConstants.UTF8_ENCODING), 
					MD5RSACoder.makePublicKey(bankRsaCode), ConvertBase64.decode(signature))){
				return new String(report);
			}else{
				log.error("签名错误");
			}
		}catch(UnsupportedEncodingException e){
			log.error("不支持编码方式错误");
		}
		
		return null;
	}
	
	/**
	 * InputStream转为byte数组
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static byte[] InputStreamToByte(InputStream in) throws IOException {   
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();   
		int ch;   
		while ((ch = in.read()) != -1) {   
			bytestream.write(ch);   
	    }   
		byte imgdata[] = bytestream.toByteArray();   
	    bytestream.close();   
	    return imgdata;   
	}  
	
}
