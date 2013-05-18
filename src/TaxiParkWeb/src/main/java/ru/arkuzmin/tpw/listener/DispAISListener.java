package ru.arkuzmin.tpw.listener;

import java.util.LinkedHashMap;
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
import ru.arkuzmin.common.MsgProps;
import ru.arkuzmin.common.MsgSender;
import ru.arkuzmin.common.PropertiesLoader;
import ru.arkuzmin.tpw.common.CommonUtils;
import ru.arkuzmin.tpw.dao.CarsDAO;
import ru.arkuzmin.tpw.dao.RouterDAO;
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
				
				String action = txtMsg.getStringProperty(MsgProps.ACTION_PROP);
				
				// Поступил заказ от пользователя
				if (MsgProps.ORDER_PROP.equals(action)) {
					
					String address = txtMsg.getStringProperty(MsgProps.ADDRESS_PROP);
					String deliveryTime = txtMsg.getStringProperty(MsgProps.DELIVERY_TIME_PROP);
					String minPrice = txtMsg.getStringProperty(MsgProps.MIN_PRICE_PROP);
					String kmPrice = txtMsg.getStringProperty(MsgProps.KM_PRICE_PROP);
					String comfortClass = txtMsg.getStringProperty(MsgProps.COMFORT_CLASS_PROP);
					
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
					
					
					CarsDAO carsDao = new CarsDAO();
					List<Car> cars = carsDao.selectCars(minPrice, kmPrice, comfortClass);
					
					// Нет автомобилей, удовлетворяющих условиям поиска
					if (cars == null || cars.isEmpty()) {
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION_PROP, "confirm");
						props.put(MsgProps.STATUS_PROP, "failed");
						props.put(MsgProps.DESCRIPTION_PROP, "no taxi cars available with this search conditions");
						MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, null, txtMsg.getJMSCorrelationID());
					
					// Опрашиваем все найденные автомобили
					} else {
						for (Car car : cars) {
							Map<String, String> props = new LinkedHashMap<String, String>();
							props.put(MsgProps.ACTION_PROP, "status");
							MsgSender.sendMessage(car.getQueueID(), null, props, mqProps.getProperty("taxiReplyQueue"), taxiCorrID);
						}
					}	
					
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
