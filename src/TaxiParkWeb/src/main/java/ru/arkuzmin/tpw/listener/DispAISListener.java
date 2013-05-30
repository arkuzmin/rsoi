package ru.arkuzmin.tpw.listener;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

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
import ru.arkuzmin.tpw.common.CommonUtils;
import ru.arkuzmin.tpw.dao.CarsDAO;
import ru.arkuzmin.tpw.dao.RouterDAO;
import ru.arkuzmin.tpw.dao.TaxiReplyDAO;
import ru.arkuzmin.tpw.dto.Car;
import ru.arkuzmin.tpw.dto.Order;

public class DispAISListener implements MessageListener {

	private static final Logger logger = CommonUtils.getLogger();
	private static Properties mqProps = PropertiesLoader.loadProperties("mq.properties");
	
	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) msg;
				logger.debug("[DispAISListener] received message: " + MQUtils.getMsgForLog(txtMsg));
				
				String action = txtMsg.getStringProperty(MsgProps.ACTION);
				String status = txtMsg.getStringProperty(MsgProps.STATUS);
				
				// Подтверждаем заказ от пользователя
				if (MsgProps.ORDER.equals(action) && MsgProps.CONFIRM.equals(status)) {
					String taxiQueue = txtMsg.getStringProperty(MsgProps.TAXI_QUEUE);
					
					String dispCorrId = txtMsg.getJMSCorrelationID();
					
					RouterDAO dao = new RouterDAO();
					String orderStatus = dao.checkStatusByDisp(dispCorrId);
					String taxiCorrId = dao.getTaxiCorrId(dispCorrId);
					
					// Заказ еще не принят в обработку таксистом
					if (MsgProps.NOT_ASSIGNED.equals(orderStatus)) {
						
						Order order = dao.selectOrderDetailsByDisp(dispCorrId);
						
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION, MsgProps.ORDER);
						props.put(MsgProps.ADDRESS, order.getAddress());
						props.put(MsgProps.DELIVERY_TIME, order.getDeliveryTime());
						props.put(MsgProps.MIN_PRICE, order.getMinPrice());
						props.put(MsgProps.KM_PRICE, order.getKmPrice());
						props.put(MsgProps.COMFORT_CLASS, order.getComfortClass());
						props.put(MsgProps.FULL_NAME, order.getFullName());
						
						MsgSender.sendMessage(taxiQueue, null, props, mqProps.getProperty("taxiReplyQueue"), taxiCorrId);
						
					}	
					
				// Поступил заказ от пользователя, проверяем свободных таксистов
				} else 	if (MsgProps.ORDER.equals(action) && MsgProps.STATUS.equals(status)) {
					
					String address = txtMsg.getStringProperty(MsgProps.ADDRESS);
					String deliveryTime = txtMsg.getStringProperty(MsgProps.DELIVERY_TIME);
					String minPrice = txtMsg.getStringProperty(MsgProps.MIN_PRICE);
					String kmPrice = txtMsg.getStringProperty(MsgProps.KM_PRICE);
					String comfortClass = txtMsg.getStringProperty(MsgProps.COMFORT_CLASS);
					
					String taxiCorrID = UUID.randomUUID().toString();
					String orderGuid = UUID.randomUUID().toString();
					
					Order order = new Order();
					order.setOrderGuid(orderGuid);
					order.setAddress(address);
					order.setDeliveryTime(deliveryTime);
					order.setKmPrice(kmPrice);
					order.setMinPrice(minPrice);
					order.setComfortClass(comfortClass);
					
					RouterDAO routerDAO = new RouterDAO();
					routerDAO.addRoute(txtMsg.getJMSCorrelationID(), taxiCorrID, order);
					
					TaxiReplyDAO trDAO = new TaxiReplyDAO();
					trDAO.clearReply(txtMsg.getJMSCorrelationID());
					
					
					CarsDAO carsDao = new CarsDAO();
					List<Car> cars = carsDao.selectCars(minPrice, kmPrice, comfortClass);
					
					// Нет автомобилей, удовлетворяющих условиям поиска
					if (cars == null || cars.isEmpty()) {
						
						routerDAO.changeStatusByTaxi(taxiCorrID, MsgProps.CANCELED);
						
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION, MsgProps.CONFIRM);
						props.put(MsgProps.STATUS, MsgProps.FAILED);
						props.put(MsgProps.TAXIPARK_GUID, mqProps.getProperty("dispInQueue"));
						props.put(MsgProps.HAS_APPROPRIATE, MsgProps.NO);
						props.put(MsgProps.DESCRIPTION, "no taxi cars available with this search conditions");
						MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, mqProps.getProperty("dispInQueue"), txtMsg.getJMSCorrelationID());
					
					// Опрашиваем все найденные автомобили
					} else {
						for (Car car : cars) {
							Map<String, String> props = new LinkedHashMap<String, String>();
							props.put(MsgProps.ACTION, MsgProps.STATUS);
							MsgSender.sendMessage(car.getQueueID(), null, props, mqProps.getProperty("taxiReplyQueue"), taxiCorrID);
						}
					}	
				// Отменяем заказ
				} else if (MsgProps.CANCEL.equals(action)) {
					String taxiQueue = txtMsg.getStringProperty(MsgProps.TAXI_QUEUE);
					String dispCorrId = txtMsg.getJMSCorrelationID();
					
					RouterDAO dao = new RouterDAO();
					Map<String, String> route = dao.getRouteByDisp(dispCorrId);
					String taxiCorrId = (new LinkedList<String>(route.keySet())).get(0);
					
					Map<String, String> props = new LinkedHashMap<String, String>();
					props.put(MsgProps.ACTION, MsgProps.CANCEL);
					
					// Отправляет запрос на отмену таксистам
					MsgSender.sendMessage(taxiQueue, null, props, mqProps.getProperty("taxiReplyQueue"), taxiCorrId);
					
				} else {
					throw new BadMessageException("Incorrect message for the TAXI system, field action = " + action);
				}
				
			}
		} catch (JMSException e) {
			logger.error("Error", e);
		} catch (BadMessageException e) {
			logger.error("Error", e);
		}
	}
}
