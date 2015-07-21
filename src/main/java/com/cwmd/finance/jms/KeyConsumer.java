package com.cwmd.finance.jms;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;

import com.cwmd.finance.bank.client.BankKeysHttpClient;
import com.cwmd.finance.bank.entity.BankException;
import com.cwmd.finance.bank.entity.XmlConstants;

public class KeyConsumer implements MessageListener, Runnable{

	private static final Log log = LogFactory.getLog(KeyConsumer.class);
	
	private JmsTemplate jmsTemplate;
	
	private QueueSession session;
	private QueueConnection connection;
	
	private Queue receiveQueue;
	
	private boolean replyed = false;
	
	/**
	 * 构造方法 初始化connection session
	 * 以新的线程启动
	 * @param jmsTemplate
	 * @param receiveQueue
	 * @param sendQueue
	 */
	public KeyConsumer(JmsTemplate jmsTemplate, Queue receiveQueue){
		try {
			connection = (QueueConnection) jmsTemplate.getConnectionFactory().createConnection();
			connection.start();
			session = (QueueSession) connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			this.receiveQueue = receiveQueue;
			Thread thread = new Thread(this);
			thread.start();
		} catch (JMSException e) {
			log.error("Consumer Create Exception", e);
		}
	}
	
	/**
	 * 接收信息处理方法
	 */
	@Override
	public void onMessage(Message message) {
		try {
			String mch_no = ((MapMessage)message).getString(XmlConstants.MCH_NO);
			String type = ((MapMessage)message).getString(XmlConstants.KEY_TYPE);
			String url = ((MapMessage)message).getString(XmlConstants.URL);
			
			Queue responseQueue = (Queue) message.getJMSReplyTo();
			if (responseQueue != null) {
				QueueSender sender = session.createSender(responseQueue);
				
				BytesMessage responseMsg = session.createBytesMessage();
				responseMsg.setJMSCorrelationID(message.getJMSMessageID());
				byte[] responseByte = BankKeysHttpClient.getBankKeys(url, mch_no, type);
				responseMsg.writeBytes(responseByte);
				sender.send(responseMsg);
				
				session.commit();
			} else {
				log.info("服务端回复队列为空");
			}

		} catch (JMSException e) {
			log.error("JMS异常", e);
			try {
				session.rollback();
			} catch (JMSException e1) {
				log.error("回滚异常", e1);
			}
		} catch (BankException e) {
			log.error("银行访问异常", e);
			try {
				session.rollback();
			} catch (JMSException e1) {
				log.error("回滚异常", e1);
			}
		} finally {
			//replyed = true;
		}
	}
	
	/**
	 * 处理接收消息
	 * @throws JMSException
	 * @throws InterruptedException
	 */
	private void receiveMessage() 
			throws JMSException, InterruptedException{
		
		QueueReceiver recevier = session.createReceiver(receiveQueue);
		
		recevier.setMessageListener(this);
		
		while (!replyed)
			Thread.sleep(1000);
		
		connection.stop();
		recevier.close();
		session.close();
		connection.close();
	}

	@Override
	public void run() {
		try {
			receiveMessage();
		} catch (JMSException e) {
			log.error("Consumer Execute Exception", e);
		} catch (InterruptedException e) {
			log.error("Consumer Thread Execute Exception", e);
		}
	}
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

}
