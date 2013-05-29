package ru.arkuzmin.tpw.listener;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.MQUtils;
import ru.arkuzmin.common.MsgProps;
import ru.arkuzmin.common.MsgSender;
import ru.arkuzmin.common.PropertiesLoader;
import ru.arkuzmin.tpw.common.CommonUtils;
import ru.arkuzmin.tpw.dao.RouterDAO;

public class TaxiAISListener implements MessageListener {

	private static final Logger logger = CommonUtils.getLogger();
	private static Properties mqProps = PropertiesLoader.loadProperties("mq.properties");
	
	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) msg;
				logger.debug("[TaxiAISListener] received message: " + MQUtils.getMsgForLog(txtMsg));
				
				String action = txtMsg.getStringProperty(MsgProps.ACTION);
				
				// Получили ответ на запрос статуса
				if (MsgProps.REPLY.equals(action)) {
					String status = txtMsg.getStringProperty(MsgProps.STATUS);
					String taxiCorrId = txtMsg.getJMSCorrelationID();
					
					// Таксист свободен, уведомляем диспетчерскую АИС
					if (MsgProps.FREE.equals(status)) {
						RouterDAO dao = new RouterDAO();
						String taxiQueue = txtMsg.getStringProperty(MsgProps.TAXI_QUEUE);
						String dispCorrId = dao.getDispCorrId(taxiCorrId);
							
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION, MsgProps.CONFIRM);
						props.put(MsgProps.STATUS, MsgProps.STATUS);
						props.put(MsgProps.HAS_APPROPRIATE, MsgProps.YES);
						props.put(MsgProps.HAS_FREE, MsgProps.YES);
						props.put(MsgProps.TAXI_QUEUE, taxiQueue);
						
						MsgSender.sendMessage(mqProps.getProperty("dispReplyQueue"), null, props, mqProps.getProperty("dispInQueue"), dispCorrId);
							
					}

				// Пришло подтверждение от таксиста	
				} else if (MsgProps.CONFIRM.equals(action)) {
					String status = txtMsg.getStringProperty(MsgProps.STATUS);
					String taxiCorrId = txtMsg.getJMSCorrelationID();
					
					// Таксист успешно принял заказ
					if (MsgProps.SUCCESS.equals(status)) {
						
						RouterDAO dao = new RouterDAO();
						dao.changeStatusByTaxi(taxiCorrId, MsgProps.ASSIGNED);
						
						Map<String, String> route = dao.getRouteByTaxi(taxiCorrId);
						String dispCorrId = (new LinkedList<String>(route.keySet())).get(0);
						
						String taxiQueue = txtMsg.getStringProperty(MsgProps.TAXI_QUEUE);
						String taxiparkQueue = mqProps.getProperty("dispInQueue");
						
						// Сообщаем об успешном заказе в диспетчерскую систему
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION, MsgProps.CONFIRM);
						props.put(MsgProps.STATUS, MsgProps.SUCCESS);
						props.put(MsgProps.DESCRIPTION, "taxi successfully ordered");
						props.put(MsgProps.TAXI_QUEUE, taxiQueue);
						props.put(MsgProps.TAXIPARK_QUEUE, taxiparkQueue);
						
						MsgSender.sendMessage(mqProps.getProperty("dispReplyQueue"), null, props, mqProps.getProperty("dispInQueue"), dispCorrId);
					
						// Заказ успешно завершен
					} else if (MsgProps.COMPLETED.equals(status)) {
						
						RouterDAO dao = new RouterDAO();
						Map<String, String> route = dao.getRouteByTaxi(taxiCorrId);
						String dispCorrId = (new LinkedList<String>(route.keySet())).get(0);
						dao.changeStatusByTaxi(taxiCorrId, status);
						
						// Сообщаем об успешном завершении заказа в диспетчерскую систему
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION, MsgProps.CONFIRM);
						props.put(MsgProps.STATUS, MsgProps.COMPLETED);
						props.put(MsgProps.DESCRIPTION, "order successfully completed");
						
						MsgSender.sendMessage(mqProps.getProperty("dispReplyQueue"), null, props, mqProps.getProperty("dispInQueue"), dispCorrId);
						// Заказ успешно отменен
					} else if (MsgProps.CANCELED.equals(status)) {
						RouterDAO dao = new RouterDAO();
						Map<String, String> route = dao.getRouteByTaxi(taxiCorrId);
						String dispCorrId = (new LinkedList<String>(route.keySet())).get(0);
						dao.changeStatusByTaxi(taxiCorrId, status);
						

						// Сообщаем об успешной  отмене заказа в диспетчерскую систему
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION, MsgProps.CONFIRM);
						props.put(MsgProps.STATUS, MsgProps.CANCELED);
						props.put(MsgProps.DESCRIPTION, "order successfully canceled");
						
						MsgSender.sendMessage(mqProps.getProperty("dispReplyQueue"), null, props, mqProps.getProperty("dispInQueue"), dispCorrId);
						
					}
				}
				
			} 
		} catch (JMSException e) {
			logger.error("Error", e);
		}
	}
}
