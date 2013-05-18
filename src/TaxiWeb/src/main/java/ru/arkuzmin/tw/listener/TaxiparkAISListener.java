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

				String action = txtMsg.getStringProperty("action");

				// Получение текущего статуса такси
				if (MsgProps.STATUS_PROP.equals(action)) {
					
					TaxiDAO dao = new TaxiDAO();
					String currentStatus = dao.checkStatus();
					String coordinates = dao.getCoordinates();
					
					Map<String, String> props = new LinkedHashMap<String, String>();
					props.put(MsgProps.ACTION_PROP, "reply");
					props.put(MsgProps.STATUS_PROP, currentStatus);
					props.put(MsgProps.COORDINATES_PROP, coordinates);
					MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, mqProperties.getProperty("taxiTaxiparkInQueue"), txtMsg.getJMSCorrelationID());
					
				// Постановка новой задачи таксисту	
				} else if  (MsgProps.ORDER_PROP.equals(action)) {
					
					TaxiDAO dao = new TaxiDAO();
					String currentStatus = dao.checkStatus();
					
					// Если таксист свободен, то можем сделать заказ
					if (Globals.TaxiStatuses.free.name().equals(currentStatus)) {
						dao.changeStatus(Globals.TaxiStatuses.busy.name());
						
						Map<String, String> props = new LinkedHashMap<String, String>();
						String taxiGuid = dao.getTaxiGuid();
						
						props.put(MsgProps.ACTION_PROP, "confirm");
						props.put(MsgProps.TAXI_GUID, taxiGuid);
						props.put(MsgProps.STATUS_PROP, "success");
						props.put(MsgProps.DESCRIPTION_PROP, "taxi successfully ordered");
						MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, null, txtMsg.getJMSCorrelationID());
						
						// Считаем, что заказ завершен через 2 минуты
						OrderMaker.completeOrder(120, txtMsg.getJMSReplyTo(), txtMsg.getJMSCorrelationID());
						
					// Иначе - отказ
					} else {
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION_PROP, "confirm");
						props.put(MsgProps.STATUS_PROP, "failed");
						props.put(MsgProps.DESCRIPTION_PROP, "taxi is already ordered");
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
