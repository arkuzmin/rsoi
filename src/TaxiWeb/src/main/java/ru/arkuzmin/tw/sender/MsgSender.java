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

	public static void sendMessage(String queue, String msg, Map<String, String> properties) throws JMSException {

		String msgToSend = msg == null ? "" : msg;	
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(queue);
		
		MessageProducer producer = session.createProducer(destination);
		TextMessage message = session.createTextMessage(msgToSend);
		
		for (String propName : properties.keySet()) {
			String propValue = properties.get(propName);
			message.setStringProperty(propName, propValue);
		}
		
		producer.send(message);
		connection.close();
		logger.debug("Message sent! " + message.getText());
	}
}
