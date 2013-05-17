package ru.arkuzmin.msg.producer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {

	private static final String QUEUE = "TAXI1.TAXIPARK1.IN";
	private static final String REPLY = "TAXIPARK1.TAXI1.IN";
	
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	public static void main(String[] args) throws JMSException {
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();
			
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		Destination dest = session.createQueue(QUEUE);
		Destination reply = session.createQueue(REPLY);
		MessageProducer producer = session.createProducer(dest);
		
		TextMessage message = session.createTextMessage("");
		message.setJMSReplyTo(reply);
		message.setJMSCorrelationID(UUID.randomUUID().toString());

		Map<String, String> properties = getStatusParams();
		
		for (String propName : properties.keySet()) {
			String propValue = properties.get(propName);
			message.setStringProperty(propName, propValue);
		}

		producer.send(message);
		connection.close();
		System.out.println("Message sent: " + message);
	}
	
	
	private static Map<String, String> getOrderParams() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("action", "order");
		return params;
	}
	
	private static Map<String, String> getStatusParams() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("action", "status");
		return params;
	}
}
