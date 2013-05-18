package ru.arkuzmin.common;

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

public class MsgSender {

	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	
	private static void sendToQueue(Connection connection, Object destination, String msgToSend, Map<String, String> properties, Object replyTo, String correlationID) throws JMSException {
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Destination dest = null;
		Destination repTo = null;
		
		if (destination instanceof String) {
			dest = session.createQueue((String)destination);
		} else if (destination instanceof Destination) {
			dest = (Destination)destination;
		}
		
		if (replyTo instanceof String) {
			repTo = session.createQueue((String)replyTo);
		} else if (destination instanceof Destination) {
			repTo = (Destination)replyTo;
		}
		
		MessageProducer producer = session.createProducer(dest);
		TextMessage message = session.createTextMessage(msgToSend);
		
		if (replyTo != null) {
			message.setJMSReplyTo(repTo);
		}
		
		if (correlationID != null) {
			message.setJMSCorrelationID(correlationID);
		}
		
		for (String propName : properties.keySet()) {
			String propValue = properties.get(propName);
			message.setStringProperty(propName, propValue);
		}
		
		producer.send(message);
	}
	
	public static void sendMessage(Object queue, String msg, Map<String, String> properties, Object replyTo, String correlationID) throws JMSException {

		String msgToSend = msg == null ? "" : msg;	
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		sendToQueue(connection, queue, msgToSend, properties, replyTo, correlationID);
		
		connection.close();
	}
}
