package ru.arkuzmin.tw.timer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimerTask;

import javax.jms.Destination;
import javax.jms.JMSException;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.MsgProps;
import ru.arkuzmin.common.MsgSender;
import ru.arkuzmin.tw.common.CommonUtils;
import ru.arkuzmin.tw.common.Globals;
import ru.arkuzmin.tw.dao.TaxiDAO;

public class OrderTask extends TimerTask {

	private static final Logger logger = CommonUtils.getLogger();
	private static final int N = 10;

	private final Destination dest;
	private final String correlationID;
	private final int n;
	private final int sec;

	public OrderTask(Destination dest, String correlationID, int n, int sec) {
		this.dest = dest;
		this.correlationID = correlationID;
		this.n = n;
		this.sec = sec;
	}

	@Override
	public void run() {
		TaxiDAO dao = new TaxiDAO();
		
		if (n <= 0) {
			dao.changeStatus(Globals.TaxiStatuses.free.name());
			Map<String, String> properties = new LinkedHashMap<String, String>();
			properties.put(MsgProps.ACTION, "confirm");
			properties.put(MsgProps.STATUS, "completed");
			properties.put(MsgProps.DESCRIPTION,
					"order successfully completed");
			
			try {
				MsgSender.sendMessage(dest, null, properties, null, correlationID);
			} catch (JMSException e) {
				logger.error("Error", e);
			}
		} else {
			dao.changeStatus("Completed " + (N-n) * 100 + "%. " + n*sec + " seconds left...");
			OrderMaker.completeOrder(sec, n-1, dest, correlationID);
		}
	}
}
