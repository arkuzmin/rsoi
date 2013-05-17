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
import ru.arkuzmin.tw.common.Globals;
import ru.arkuzmin.tw.common.PropertiesLoader;
import ru.arkuzmin.tw.dao.TaxiDAO;
import ru.arkuzmin.tw.sender.MsgSender;
import ru.arkuzmin.tw.timer.OrderMaker;

public class TaxiparkAISListener implements MessageListener {

	private static final Logger logger = CommonUtils.getLogger();
	
	private static final Properties mqProperties = PropertiesLoader.loadProperties("mq.properties");
	
	private static String queueName;
	
	private static final String STATUS_PROP = "status";
	private static final String ORDER_PROP = "order";
	private static final String ACTION_PROP = "action";
	private static final String COORDINATES_PROP = "coordinates";
	private static final String DESCRIPTION_PROP = "description";

	@Override
	public void onMessage(Message msg) {
		TextMessage txtMsg = null;

		try {
			if (msg instanceof TextMessage) {
				txtMsg = (TextMessage) msg;

				String action = txtMsg.getStringProperty("action");

				// Получение текущего статуса такси
				if (STATUS_PROP.equals(action)) {
					
					TaxiDAO dao = new TaxiDAO();
					String currentStatus = dao.checkStatus();
					String coordinates = dao.getCoordinates();
					
					Map<String, String> props = new LinkedHashMap<String, String>();
					props.put(ACTION_PROP, "reply");
					props.put(STATUS_PROP, currentStatus);
					props.put(COORDINATES_PROP, coordinates);
					MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, null, txtMsg.getJMSCorrelationID());
					
				// Постановка новой задачи таксисту	
				} else if  (ORDER_PROP.equals(action)) {
					
					TaxiDAO dao = new TaxiDAO();
					String currentStatus = dao.checkStatus();
					
					// Если таксист свободен, то можем сделать заказ
					if (Globals.TaxiStatuses.free.name().equals(currentStatus)) {
						dao.changeStatus(Globals.TaxiStatuses.busy.name());
						
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(ACTION_PROP, "confirm");
						props.put(STATUS_PROP, "success");
						props.put(DESCRIPTION_PROP, "taxi successfully ordered");
						MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, null, txtMsg.getJMSCorrelationID());
						
						// Считаем, что заказ завершен через 2 минуты
						OrderMaker.completeOrder(60, txtMsg.getJMSReplyTo(), txtMsg.getJMSCorrelationID());
						
					// Иначе - отказ
					} else {
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(ACTION_PROP, "confirm");
						props.put(STATUS_PROP, "failed");
						props.put(DESCRIPTION_PROP, "taxi is already ordered");
						MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, null, txtMsg.getJMSCorrelationID());
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
