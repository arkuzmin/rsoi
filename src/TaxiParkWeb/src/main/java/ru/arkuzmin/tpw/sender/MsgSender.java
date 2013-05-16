package ru.arkuzmin.tpw.sender;

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

import ru.arkuzmin.tpw.common.CommonUtils;

public class MsgSender {
	private static final Logger logger = CommonUtils.getLogger();

	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	public static void sendMessage(String queue, String msg) throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(queue);
		
		MessageProducer producer = session.createProducer(destination);
		TextMessage message = session.createTextMessage(msg);
		
		producer.send(message);
		connection.close();
		logger.debug("Message sent! " + message.getText());
	}
}
