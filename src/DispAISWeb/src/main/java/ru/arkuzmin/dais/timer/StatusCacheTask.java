package ru.arkuzmin.dais.timer;

import java.util.LinkedHashMap;
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


public class StatusCacheTask extends TimerTask {

	private static final Logger logger = CommonUtils.getLogger();
	private static final Properties mqProps = PropertiesLoader.loadProperties("mq.properties");
	
	private final Object dest;
	private final String correlationID;
	private final int seconds;
	
	public StatusCacheTask(Object dest, String correlationID, int seconds) {
		this.dest = dest;
		this.correlationID = correlationID;
		this.seconds = seconds;
	}
	
	
	@Override
	public void run() {
		ApplicationDAO appDao = new ApplicationDAO();
		String orderDetailGuid = appDao.getOrderDetailGuid(correlationID);
		
		OrderDetailsDAO odDao = new OrderDetailsDAO();
		String status = odDao.checkStatus(orderDetailGuid);
		
		// Нужно еще кэшировать
		if (!"free".equals(status)) {
			Map<String, String> properties = new LinkedHashMap<String, String>();
			properties.put(MsgProps.ACTION_PROP, "status");
			
			try {
				MsgSender.sendMessage(dest, null, properties, mqProps.getProperty("dispTaxiQueue"), correlationID);
				StatusCache.cacheStatus(seconds, dest, correlationID);
			} catch (JMSException e) {
				logger.error("Error", e);
			}
		}
	}
}
