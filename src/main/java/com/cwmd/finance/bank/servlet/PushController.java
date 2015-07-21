package com.cwmd.finance.bank.servlet;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cwmd.finance.bank.entity.BankException;
import com.cwmd.finance.bank.entity.XmlConstants;
import com.cwmd.finance.jms.Sender;
import com.cwmd.finance.service.AutoProfitShareService;

/**
 * Created by CWMD . 
 * @author: chaoyang.ren  
 * @date:2015年7月14日  
 * @time:上午10:59:23   
 * @email:chaoyang.ren@foxmail.com  
 * @version: 1.0
 */
@Controller
public class PushController {
	private static final Log log = LogFactory.getLog(PushController.class);
	@Autowired
	private Sender sender;
	
	@Autowired
	private AutoProfitShareService autoProfitShareService;
	
	@RequestMapping(value = "/pdc", method = RequestMethod.GET)
	public void push(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String xml = request.getParameter(XmlConstants.REQ_PARAM_XML);
		String signature = request.getParameter(XmlConstants.REQ_PARAM_SIGNATURE);
		
		autoProfitShareService.autoSharingProfit();
		
		ServletOutputStream sos = null;
		
		if (xml != null && !"".equals(xml) && signature != null
				&& !"".equals(signature)) {

			sos = response.getOutputStream();
			try {
				sender.sendMessage(xml, signature);
				if (sender.getByteData() != null
						&& sender.getByteData().length > 0) {
					sos.write(sender.getByteData());
				}
				sos.flush();
			} catch (BankException e) {
				log.error(e.getMessage(), e);
				sos.flush();
			} finally {
				try {
					if (sos != null) {
						sos.close();
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}

		}
	}
}

