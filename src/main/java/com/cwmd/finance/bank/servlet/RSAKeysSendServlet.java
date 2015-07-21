package com.cwmd.finance.bank.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cwmd.finance.jms.KeySender;

/**
 * 生成RSA公钥与DESede密钥，银行查询获取
 * URL地址暂为: /merchant/KeyTransfer
 */
public class RSAKeysSendServlet extends HttpServlet{

	private static final long serialVersionUID = -5910196528935326365L;
	
	private static final Log log = LogFactory.getLog(RSAKeysSendServlet.class);

	private KeySender keySender;
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException{
		ServletContext servletContext = servletConfig.getServletContext();
		
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		keySender = (KeySender) context.getBean("keySender");
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		this.doPost(request, response);
	}
	
	/**
	 * 提供URL供银行查询南储密钥
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		ServletOutputStream sos = null;
		
		try{
			String type = request.getParameter("type");
			
			log.info("银行访问地址:" + request.getRemoteHost());
			
			sos = response.getOutputStream();
			sos.write("000000".getBytes());
			
			if(type != null && !"".equals(type)){
				if(type.equals("des")){
					keySender.sendMessage("des");
					if(keySender.getByteData() != null
							&& keySender.getByteData().length > 0){
						sos.write(keySender.getByteData());
					}
				}else if(type.equals("pub")){
					keySender.sendMessage("pub");
					if(keySender.getByteData() != null
							&& keySender.getByteData().length > 0){
						sos.write(keySender.getByteData());
					}
				}
			}else{
				keySender.sendMessage("pub");
				if(keySender.getByteData() != null
						&& keySender.getByteData().length > 0){
					sos.write(keySender.getByteData());
				}
			}
			sos.flush();
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}finally{
			try{
				if(sos != null){
					sos.close();
				}
			}catch(Exception e){
				log.error(e.getMessage(), e);
			}
		}
		
	}

	
}
