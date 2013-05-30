package ru.arkuzmin.dais.timer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimerTask;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.MsgProps;
import ru.arkuzmin.common.MsgSender;
import ru.arkuzmin.common.PropertiesLoader;
import ru.arkuzmin.dais.common.CommonUtils;
import ru.arkuzmin.dais.dao.ApplicationDAO;
import ru.arkuzmin.dais.dao.OrderDetailsDAO;
import ru.arkuzmin.dais.dto.Order;

public class ResendOrderTask extends TimerTask {

	private static final Logger logger = CommonUtils.getLogger();
	private static final Properties mqProps = PropertiesLoader.loadProperties("mq.properties");
	
	private final List<String> taxiparks;
	private final String correlationID;
	private final int seconds;
	
	public ResendOrderTask(List<String> taxiparks, String correlationID, int seconds) {
		this.taxiparks = taxiparks;
		this.correlationID = correlationID;
		this.seconds = seconds;
	}
	
	
	@Override
	public void run() {
		ApplicationDAO appDao = new ApplicationDAO();
		String orderDetailGuid = appDao.getOrderDetailGuid(correlationID);
		
		OrderDetailsDAO odDao = new OrderDetailsDAO();
		String status = odDao.checkStatus(orderDetailGuid);
		Order order = odDao.getOrderByGuid(orderDetailGuid);
		
		// Заказа еще не принят в обработку
		if (MsgProps.BUSY.equals(status)) {
			Map<String, String> params = new LinkedHashMap<String, String>();
			params.put(MsgProps.ACTION, MsgProps.ORDER);
			params.put(MsgProps.STATUS, MsgProps.STATUS);
			params.put(MsgProps.ADDRESS, order.getAddress());
			params.put(MsgProps.DELIVERY_TIME, String.valueOf(order.getDeliveryTime()));
			params.put(MsgProps.MIN_PRICE, order.getMinPrice() == null ? null : String.valueOf(order.getMinPrice()));
			params.put(MsgProps.KM_PRICE, order.getKmPrice() == null ? null : String.valueOf(order.getKmPrice()));
			params.put(MsgProps.COMFORT_CLASS, order.getComfortClass());

			try {
				for (String dest: taxiparks) {
					MsgSender.sendMessage(dest, null, params, mqProps.getProperty("dispTaxiparkQueue"), correlationID);
				}
				ResendOrder.resendOrder(taxiparks, correlationID, seconds);
			} catch (JMSException e) {
				logger.error("Error", e);
			}
		}
	}
}