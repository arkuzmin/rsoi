package ru.arkuzmin.tw.listener;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import ru.arkuzmin.tw.common.BadMessageException;
import ru.arkuzmin.tw.common.CommonUtils;
import ru.arkuzmin.tw.common.PropertiesLoader;
import ru.arkuzmin.tw.sender.MsgSender;

public class TaxiparkAISListener implements MessageListener {

	private static final Logger logger = CommonUtils.getLogger();
	
	private static final Properties mqProperties = PropertiesLoader.loadProperties("mq.properties");
	
	private static String queueName;
	
	private static final String STATUS_PROP = "STATUS";

	@Override
	public void onMessage(Message msg) {
		TextMessage txtMsg = null;

		try {
			if (msg instanceof TextMessage) {
				txtMsg = (TextMessage) msg;

				String msgType = txtMsg.getStringProperty("msgType");

				if (STATUS_PROP.equals(msgType)) {
					//TODO добавить получение статуса и координат из БД
					Map<String, String> props = new LinkedHashMap<String, String>();
					props.put("status", "free");
					props.put("coordinates", "coordinates");
					MsgSender.sendMessage(getQueueName(), null, props);
					
				} else {
					throw new BadMessageException("Incorrect message for the TAXI system, field msgType = " + msgType);
				}
				
			} else {
				logger.error("The incoming message is not of type TextMessage!");
			}

		} catch (JMSException e) {
			logger.error("Error", e);
		} catch (BadMessageException e) {
			logger.error("Error", e);
		}

	}
	
	private String getQueueName() {
		if (queueName == null) {
			queueName = mqProperties.getProperty("taxiTaxiparkInQueue");
		}
		return queueName;
	}

}
