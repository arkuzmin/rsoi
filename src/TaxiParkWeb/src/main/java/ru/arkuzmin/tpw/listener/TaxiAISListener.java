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

import ru.arkuzmin.common.MsgProps;
import ru.arkuzmin.common.MsgSender;
import ru.arkuzmin.common.PropertiesLoader;
import ru.arkuzmin.tpw.common.CommonUtils;
import ru.arkuzmin.tpw.dao.CarsDAO;
import ru.arkuzmin.tpw.dao.RouterDAO;
import ru.arkuzmin.tpw.dto.Order;

public class TaxiAISListener implements MessageListener {

	private static final Logger logger = CommonUtils.getLogger();
	private static Properties mqProps = PropertiesLoader.loadProperties("mq.properties");
	
	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) msg;
				
				String action = txtMsg.getStringProperty(MsgProps.ACTION);
				
				// Получили ответ на запрос статуса
				if ("reply".equals(action)) {
					String status = txtMsg.getStringProperty(MsgProps.STATUS);
					String taxiCorrId = txtMsg.getJMSCorrelationID();
					
					// Таксист свободен, делаем заказ
					if ("free".equals(status)) {
						RouterDAO dao = new RouterDAO();
						String orderStatus = dao.checkStatusByTaxi(taxiCorrId);
						// Заказ еще не принят в обработку таксистом
						if ("not_assigned".equals(orderStatus)) {
							
							Order order = dao.selectOrderDetailsByTaxi(taxiCorrId);
							
							Map<String, String> props = new LinkedHashMap<String, String>();
							props.put(MsgProps.ACTION, "order");
							props.put(MsgProps.ADDRESS, order.getAddress());
							props.put(MsgProps.DELIVERY_TIME, order.getDeliveryTime());
							props.put(MsgProps.MIN_PRICE, order.getMinPrice());
							props.put(MsgProps.KM_PRICE, order.getKmPrice());
							props.put(MsgProps.COMFORT_CLASS, order.getComfortClass());
							props.put(MsgProps.FULL_NAME, order.getFullName());
							
							MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, mqProps.getProperty("taxiReplyQueue"), txtMsg.getJMSCorrelationID());
							
						}
					}
				// Пришло подтверждение от таксиста	
				} else if ("confirm".equals(action)) {
					String status = txtMsg.getStringProperty(MsgProps.STATUS);
					String taxiCorrId = txtMsg.getJMSCorrelationID();
					
					// Таксист успешно принял заказ
					if ("success".equals(status)) {
						
						RouterDAO dao = new RouterDAO();
						dao.changeStatusByTaxi(taxiCorrId, "assigned");
						
						Map<String, String> route = dao.getRouteByTaxi(taxiCorrId);
						String dispCorrId = (new LinkedList<String>(route.keySet())).get(0);
						
						String taxiGuid = txtMsg.getStringProperty(MsgProps.TAXI_GUID);
						CarsDAO carDao = new CarsDAO();
						String taxiQueue = carDao.getQueueID(taxiGuid);
						
						// Сообщаем об успешном заказе в диспетчерскую систему
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION, "confirm");
						props.put(MsgProps.STATUS, "success");
						props.put(MsgProps.DESCRIPTION, "taxi successfully ordered");
						props.put(MsgProps.TAXI_QUEUE, taxiQueue);
						
						MsgSender.sendMessage(mqProps.getProperty("dispReplyQueue"), null, props, mqProps.getProperty("taxiReplyQueue"), dispCorrId);
					
						// Заказ успешно завершен
					} else if ("completed".equals(status)) {
						
						RouterDAO dao = new RouterDAO();
						Map<String, String> route = dao.getRouteByTaxi(taxiCorrId);
						String dispCorrId = (new LinkedList<String>(route.keySet())).get(0);
						
						// Сообщаем об успешном завершении заказа в диспетчерскую систему
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION, "confirm");
						props.put(MsgProps.STATUS, "completed");
						props.put(MsgProps.DESCRIPTION, "order successfully completed");
						
						MsgSender.sendMessage(mqProps.getProperty("dispReplyQueue"), null, props, mqProps.getProperty("taxiReplyQueue"), dispCorrId);
					}
				}
				
			} 
		} catch (JMSException e) {
			logger.error("Error", e);
		}
	}
}
