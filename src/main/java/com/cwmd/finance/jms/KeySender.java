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
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;

import com.cwmd.finance.bank.entity.BankException;
import com.cwmd.finance.bank.entity.XmlConstants;

public class KeySender implements MessageListener{

	private static final Log log = LogFactory.getLog(KeySender.class);
	
	private JmsTemplate jmsTemplate;
	private Queue sendQueue;
	private Queue replyQueue;
	
	private boolean replyed = false;
	
	//返回字节消息
	private byte[] byteData;
	
	public void sendMessage(String type) throws BankException{
		
		QueueConnection connection = null;
		QueueSession session = null;
		QueueSender sender = null;
		
		try {
			connection = (QueueConnection) jmsTemplate.getConnectionFactory().createConnection();
			connection.start();
			session = (QueueSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			sender = session.createSender(sendQueue);
			
			MapMessage msg = session.createMapMessage();
			msg.setString(XmlConstants.KEY_TYPE, type);
			//设置回复队列
			msg.setJMSReplyTo(replyQueue); 
			//设置超时时间
			sender.setTimeToLive(XmlConstants.BANK_TRADE_TIMEOUT);
			sender.send(msg);
			
			// 接收回复信息
			String filter = "JMSCorrelationID='" + msg.getJMSMessageID() + "'";
			QueueReceiver reply = session.createReceiver(replyQueue, filter);
			// 异步方式接收回复
			reply.setMessageListener(this);
			replyed = false;
			int timeOut = 0;
			while (!replyed){
				Thread.sleep(1000);
				timeOut = timeOut + 1000;
				if(timeOut > XmlConstants.BANK_TRADE_TIMEOUT){
					replyed = true;
					byteData = null;
					throw new BankException("获取密钥失败");
				}
			}
		} catch (JMSException e) {
			log.error("Sender Create Exception", e);
			throw new BankException("本地网络异常");
		} catch (InterruptedException e) {
			log.error("Sender Thread Exception", e);
			throw new BankException("获取密钥失败");
		}finally{
			try {
				if(connection != null){
					connection.stop();
				}
				if(sender != null){
					sender.close();
				}
				if(session != null){
					session.close();
				}
				if(connection != null){
					connection.close();
				}
			} catch (JMSException e) {
				log.error("Sender Destroy Exception", e);
				throw new BankException("本地网络异常");
			}
			
		}
		
	}
	
	@Override
	public void onMessage(Message message) {
		try{
			if (message instanceof BytesMessage) {
				BytesMessage byteMsg = (BytesMessage) message;
				byteData = new byte[(int) byteMsg.getBodyLength()];
				byteMsg.readBytes(byteData);
			}else if(message instanceof TextMessage){
				TextMessage textMsg = (TextMessage) message;
				log.info(textMsg.getText());
			}
		}catch(JMSException e){
			log.error("反馈消息错误", e);
		}finally{
			replyed = true;
		}
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}

	public Queue getSendQueue() {
		return sendQueue;
	}

	public void setSendQueue(Queue sendQueue) {
		this.sendQueue = sendQueue;
	}

	public Queue getReplyQueue() {
		return replyQueue;
	}

	public void setReplyQueue(Queue replyQueue) {
		this.replyQueue = replyQueue;
	}

}
