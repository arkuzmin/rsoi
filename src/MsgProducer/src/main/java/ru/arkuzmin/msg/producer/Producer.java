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

import ru.arkuzmin.common.MsgProps;

public class Producer {

	private static final String QUEUE = "TAXI1.TAXIPARK1.IN";
	private static final String REPLY = "TAXIPARK1.TAXI1.IN";
	
	private static final String DISP_TAXIPARK_QUEUE = "TAXIPARK1.DISP.IN";
	private static final String DISP_TAXIPARK_REPLY = "DISP.TAXIPARK.IN";
	
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	public static void main(String[] args) throws JMSException {
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();
			
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		Destination dest = session.createQueue(DISP_TAXIPARK_QUEUE);
		Destination reply = session.createQueue(DISP_TAXIPARK_REPLY);
		MessageProducer producer = session.createProducer(dest);
		
		TextMessage message = session.createTextMessage("");
		message.setJMSReplyTo(reply);
		message.setJMSCorrelationID(UUID.randomUUID().toString());

		Map<String, String> properties = getTPOrderParams();
		
		for (String propName : properties.keySet()) {
			String propValue = properties.get(propName);
			message.setStringProperty(propName, propValue);
		}

		producer.send(message);
		connection.close();
		System.out.println("Message sent: " + message);
	}
	
	private static Map<String, String> getTPOrderParams() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put(MsgProps.ACTION_PROP, "order");
		params.put(MsgProps.ADDRESS_PROP, "artekovskaya 2-2-360");
		params.put(MsgProps.DELIVERY_TIME_PROP, "18-05-2013 15:55");
		params.put(MsgProps.MIN_PRICE_PROP, "90");
		params.put(MsgProps.KM_PRICE_PROP, "90");
		return params;
	}
	
	private static Map<String, String> getOrderParams() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put(MsgProps.ACTION_PROP, "order");
		return params;
	}
	
	private static Map<String, String> getStatusParams() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put(MsgProps.ACTION_PROP, "status");
		return params;
	}
}
