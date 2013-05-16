package ru.arkuzmin.tpw.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import ru.arkuzmin.tpw.common.CommonUtils;

public class TaxiAISListener implements MessageListener {

	private static final Logger logger = CommonUtils.getLogger();

	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) msg;
				logger.debug("Получено сообщение: " + txtMsg.getText());
			}
		} catch (JMSException e) {
			logger.error("Error", e);
		}
	}
}
