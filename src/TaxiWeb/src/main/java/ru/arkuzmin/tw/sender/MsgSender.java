package ru.arkuzmin.tw.sender;

import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import ru.arkuzmin.tw.common.CommonUtils;

public class MsgSender {
	private static final Logger logger = CommonUtils.getLogger();

	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	
	private static void sendToQueue(Connection connection, Object destination, String msgToSend, Map<String, String> properties, Destination replyTo, String correlationID) throws JMSException {
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Destination dest = null;
		
		if (destination instanceof String) {
			dest = session.createQueue((String)destination);
		} else if (destination instanceof Destination) {
			dest = (Destination)destination;
		}
		
		MessageProducer producer = session.createProducer(dest);
		TextMessage message = session.createTextMessage(msgToSend);
		
		if (replyTo != null) {
			message.setJMSReplyTo(replyTo);
		}
		
		if (correlationID != null) {
			message.setJMSCorrelationID(correlationID);
		}
		
		for (String propName : properties.keySet()) {
			String propValue = properties.get(propName);
			message.setStringProperty(propName, propValue);
		}
		
		producer.send(message);
		logger.debug("Message sent! " + message.getText());
	}
	
	public static void sendMessage(Destination destination, String msg, Map<String, String> properties, Destination replyTo, String correlationID) throws JMSException {
		
		String msgToSend = msg == null ? "" : msg;	
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();
	
		sendToQueue(connection, destination, msgToSend, properties, replyTo, correlationID);
		
		connection.close();
	}
	
	
	public static void sendMessage(String queue, String msg, Map<String, String> properties, Destination replyTo, String correlationID) throws JMSException {

		String msgToSend = msg == null ? "" : msg;	
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		sendToQueue(connection, queue, msgToSend, properties, replyTo, correlationID);
		
		connection.close();
	}
}
