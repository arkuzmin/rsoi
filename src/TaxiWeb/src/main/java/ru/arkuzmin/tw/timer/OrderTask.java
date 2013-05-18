package ru.arkuzmin.tw.timer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimerTask;

import javax.jms.Destination;
import javax.jms.JMSException;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.MsgSender;
import ru.arkuzmin.tw.common.CommonUtils;
import ru.arkuzmin.tw.common.Globals;
import ru.arkuzmin.tw.dao.TaxiDAO;


public class OrderTask extends TimerTask {

	private static final Logger logger = CommonUtils.getLogger();
	
	private static final String STATUS_PROP = "status";
	private static final String ACTION_PROP = "action";
	private static final String DESCRIPTION_PROP = "description";
	
	private final Destination dest;
	private final String correlationID;
	
	public OrderTask(Destination dest, String correlationID) {
		this.dest = dest;
		this.correlationID = correlationID;
	}
	
	@Override
	public void run() {
		TaxiDAO dao = new TaxiDAO();
		String currentStatus = dao.checkStatus();
		
		if (Globals.TaxiStatuses.busy.name().equals(currentStatus)) {
			dao.changeStatus(Globals.TaxiStatuses.free.name());
		}
		
		Map<String, String> properties = new LinkedHashMap<String, String>();
		properties.put(ACTION_PROP, "confirm");
		properties.put(STATUS_PROP, "completed");
		properties.put(DESCRIPTION_PROP, "order successfully completed");
		
		try {
			MsgSender.sendMessage(dest, null, properties, null, correlationID);
		} catch (JMSException e) {
			logger.error("Error", e);
		}
	}
}
