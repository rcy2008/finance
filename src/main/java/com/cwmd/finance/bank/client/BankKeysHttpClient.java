package com.cwmd.finance.bank.client;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cwmd.finance.bank.encryption.DESCoder;
import com.cwmd.finance.bank.entity.BankException;
import com.cwmd.finance.bank.entity.XmlConstants;

public class BankKeysHttpClient {

	private static final Log log = LogFactory.getLog(BankKeysHttpClient.class);
	
	/**
	 * 获取银行DES密钥
	 * @param url 获取密钥URL
	 * @param mch_no 商户编号
	 * @param type 密钥类型
	 * @return
	 */
	public static byte[] getBankKeys(String url, String mch_no, 
			String type) throws BankException{
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.addParameter("type", type);
		//设置连接超时时间
		client.getHttpConnectionManager().getParams()
			.setConnectionTimeout(XmlConstants.BANK_CONNECTION_TIME_OUT);
		//设置读取超时时间
		client.getHttpConnectionManager().getParams()
			.setSoTimeout(XmlConstants.BANK_READ_TIME_OUT);
		try{
			
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.error("获取DES密钥网络错误：" + statusCode);
				throw new BankException("HTTP访问异常" + statusCode);
			} else{
				byte[] data = method.getResponseBody();
				byte[] tmp = new byte[6];
				System.arraycopy(data, 0, tmp, 0, tmp.length);
				String return_code = new String(tmp);
				if("000000".equals(return_code)){
					tmp = new byte[data.length - 6];
					System.arraycopy(data, 6, tmp, 0, tmp.length);
					
					DESCoder coder = new DESCoder();
					//使用约定密钥对传输的密钥进行解密
					return coder.decrypt(tmp, mch_no);
				} else {
					tmp = new byte[data.length - 6];
					System.arraycopy(data, 6, tmp, 0, tmp.length);
					String errMsg = new String(tmp, XmlConstants.GBK_ENCODING);
					//错误信息编码为GBK
					log.error("返回错误码：" + return_code + ",错误信息:"
							+ errMsg);
					throw new BankException(errMsg);
				}
			}
		}catch(HttpException e){
			log.error("HTTP访问异常", e);
			throw new BankException("HTTP访问异常");
		}catch(IOException e){
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
	
}
