package com.cwmd.finance.jms;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;

import com.cwmd.finance.bank.entity.XmlConstants;

public class ConnectionConsumer implements MessageListener, Runnable{

	private static final Log log = LogFactory.getLog(ConnectionConsumer.class);
	
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
	public ConnectionConsumer(JmsTemplate jmsTemplate, Queue receiveQueue){
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
			String url = ((MapMessage)message).getString(XmlConstants.URL);
			InetAddress add = InetAddress.getByName(url);
			
			Queue responseQueue = (Queue) message.getJMSReplyTo();
			
			if (responseQueue != null) {
				QueueSender sender = session.createSender(responseQueue);
				
				TextMessage responseMsg = session.createTextMessage();
				responseMsg.setJMSCorrelationID(message.getJMSMessageID());
				
				if(add.isReachable(XmlConstants.BANK_CONNECTION_TIME_OUT)){
					responseMsg.setText("成功");
				}else{
					responseMsg.setText("失败");
				}
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
		} catch (UnknownHostException e) {
			log.error("网络异常" + e.getMessage());
		} catch (IOException e) {
			log.error("网络异常" + e.getMessage());
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
