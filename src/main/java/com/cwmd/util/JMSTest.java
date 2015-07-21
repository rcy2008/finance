/*package com.cwmd.util;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cwmd.ApplicationConfig;
import com.cwmd.finance.bank.entity.BankException;
import com.cwmd.finance.jms.Consumer;
import com.cwmd.finance.jms.Sender;


*//**
 * Created by CWMD . 
 * @author: chaoyang.ren  
 * @date:2015年7月13日  
 * @time:下午1:44:46   
 * @email:chaoyang.ren@foxmail.com  
 * @version: 1.0
 *//*
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes=ApplicationConfig.class)
public class JMSTest {
	@Autowired
	private Sender sender;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private ActiveMQQueue financeSendQueue;

	@Test
	public void testSend() throws BankException{
		sender.sendMessage("<contextName>neoFortune</contextName><type>12</type>", "x");
	}
	
	@Test
	public void testConsumer() throws BankException{
		Consumer consumer = new Consumer(jmsTemplate, financeSendQueue);
	}
	
}

*/