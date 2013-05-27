package ru.arkuzmin.dais.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.BadMessageException;
import ru.arkuzmin.common.MsgProps;
import ru.arkuzmin.dais.common.CommonUtils;
import ru.arkuzmin.dais.dao.ApplicationDAO;
import ru.arkuzmin.dais.dao.OrderDetailsDAO;

public class TaxiAISListener implements MessageListener {

	private static final Logger logger = CommonUtils.getLogger();

	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) msg;

				String action = txtMsg.getStringProperty(MsgProps.ACTION);

				// Поступил ответ от таксиста
				if ("reply".equals(action)) {
					String status = txtMsg.getStringProperty(MsgProps.STATUS);
					if ("free".equals(status)) {
						status = "successfully completed!";
					}
					String coordinates = txtMsg.getStringProperty(MsgProps.COORDINATES);
					String applicationGuid = txtMsg.getJMSCorrelationID();
						
					ApplicationDAO appDao = new ApplicationDAO();
					String orderDetailGuid = appDao.getOrderDetailGuid(applicationGuid);
						
					OrderDetailsDAO odDao = new OrderDetailsDAO();
					odDao.updateStatus(status, coordinates, orderDetailGuid);
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
