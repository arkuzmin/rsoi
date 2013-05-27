package ru.arkuzmin.tw.listener;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.BadMessageException;
import ru.arkuzmin.common.MQUtils;
import ru.arkuzmin.common.MsgProps;
import ru.arkuzmin.common.MsgSender;
import ru.arkuzmin.common.PropertiesLoader;
import ru.arkuzmin.tw.common.CommonUtils;
import ru.arkuzmin.tw.common.Globals;
import ru.arkuzmin.tw.dao.TaxiDAO;
import ru.arkuzmin.tw.timer.OrderMaker;

public class TaxiparkAISListener implements MessageListener {

	private static final Logger logger = CommonUtils.getLogger();
	
	private static final Properties mqProperties = PropertiesLoader.loadProperties("mq.properties");
	
	private static String queueName;
	


	@Override
	public void onMessage(Message msg) {
		TextMessage txtMsg = null;

		try {
			if (msg instanceof TextMessage) {
				txtMsg = (TextMessage) msg;
				logger.debug("[TaxiparkAISListener] received message: " + MQUtils.getMsgForLog(txtMsg));

				String action = txtMsg.getStringProperty(MsgProps.ACTION);

				// Получение текущего статуса такси
				if (MsgProps.STATUS.equals(action)) {
					
					TaxiDAO dao = new TaxiDAO();
					String currentStatus = dao.checkStatus();
					String coordinates = dao.getCoordinates();
					
					Map<String, String> props = new LinkedHashMap<String, String>();
					props.put(MsgProps.ACTION, MsgProps.REPLY);
					props.put(MsgProps.STATUS, currentStatus);
					props.put(MsgProps.COORDINATES, coordinates);
					props.put(MsgProps.TAXI_QUEUE, mqProperties.getProperty("taxiTaxiparkInQueue"));
					MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, mqProperties.getProperty("taxiTaxiparkInQueue"), txtMsg.getJMSCorrelationID());
					
				// Постановка новой задачи таксисту	
				} else if  (MsgProps.ORDER.equals(action)) {
					
					TaxiDAO dao = new TaxiDAO();
					String currentStatus = dao.checkStatus();
					
					// Если таксист свободен, то можем сделать заказ
					if (Globals.TaxiStatuses.free.name().equals(currentStatus)) {
						dao.changeStatus(Globals.TaxiStatuses.busy.name());
						
						Map<String, String> props = new LinkedHashMap<String, String>();
						
						props.put(MsgProps.ACTION, MsgProps.CONFIRM);
						props.put(MsgProps.TAXI_QUEUE, mqProperties.getProperty("taxiTaxiparkInQueue"));
						props.put(MsgProps.STATUS, MsgProps.SUCCESS);
						props.put(MsgProps.DESCRIPTION, "taxi successfully ordered");
						MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, null, txtMsg.getJMSCorrelationID());
						
						// Считаем, что заказ завершен через 2 минуты
						OrderMaker.completeOrder(12, 10, txtMsg.getJMSReplyTo(), txtMsg.getJMSCorrelationID());
						
					// Иначе - отказ
					} else {
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION, MsgProps.CONFIRM);
						props.put(MsgProps.STATUS, MsgProps.FAILED);
						props.put(MsgProps.DESCRIPTION, "taxi is busy now");
						MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props,  mqProperties.getProperty("taxiTaxiparkInQueue"), txtMsg.getJMSCorrelationID());
					}
					
					
				} else {
					throw new BadMessageException("Incorrect message for the TAXI system, field action = " + action);
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
	
	@SuppressWarnings("unused")
	private String getQueueName() {
		if (queueName == null) {
			queueName = mqProperties.getProperty("taxiTaxiparkInQueue");
		}
		return queueName;
	}

}
