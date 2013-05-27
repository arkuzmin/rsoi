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
import ru.arkuzmin.dais.dto.Order;
import ru.arkuzmin.dais.timer.StatusCache;

public class TaxiparkAISListener implements MessageListener {

	private static final Logger logger = CommonUtils.getLogger();
	
	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) msg;

				String action = txtMsg.getStringProperty(MsgProps.ACTION);

				// Поступил ответ от таксопарка
				if ("confirm".equals(action)) {
					String status = txtMsg.getStringProperty(MsgProps.STATUS);
					
					// Если пришел статус от таксопарка
					if (MsgProps.STATUS.equals(status)) {
						String hasFree = txtMsg.getStringProperty(MsgProps.HAS_FREE);
						String hasAppropriate = txtMsg.getStringProperty(MsgProps.HAS_APPROPRIATE);
						
						// Если в этом таксопарке есть подходящие и свободные таксисты
						if (MsgProps.YES.equals(hasAppropriate) && MsgProps.YES.equals(hasFree)) {
							
						}
						
					}
					
					
					// Нет свободных автомобилей, удовлетворяющих условия поиска
					if ("failed".equals(status)) {
						
						String application_guid = txtMsg.getJMSCorrelationID();
						
						Order order = new Order();
						order.setOrderGUID(application_guid);
						
						ApplicationDAO dao = new ApplicationDAO();
						dao.confirmApplication(order, null, "CANCELED");
						
					// Заказ успешно принят в обработку таксистом
					} else if ("success".equals(status)) {
						
						String application_guid = txtMsg.getJMSCorrelationID();
						String taxi_queue = txtMsg.getStringProperty(MsgProps.TAXI_QUEUE);
						
						Order order = new Order();
						order.setOrderGUID(application_guid);
						
						ApplicationDAO dao = new ApplicationDAO();
						dao.confirmApplication(order, taxi_queue, "CONFIRMED");
						
						// Запускаем задачу автоматического кэширования статуса заказа
						StatusCache.cacheStatus(15, taxi_queue, application_guid);
						
					// Заказ успешно завершен таксистом
					} else if ("completed".equals(status)) {
						
						String application_guid = txtMsg.getJMSCorrelationID();
						String taxi_queue = txtMsg.getStringProperty(MsgProps.TAXI_QUEUE);
						
						Order order = new Order();
						order.setOrderGUID(application_guid);
						
						ApplicationDAO dao = new ApplicationDAO();
						dao.confirmApplication(order, taxi_queue, "COMPLETED");
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
