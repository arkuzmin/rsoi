package ru.arkuzmin.dais.listener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import ru.arkuzmin.dais.common.CommonUtils;
import ru.arkuzmin.dais.dao.ApplicationDAO;
import ru.arkuzmin.dais.dao.OrderDetailsDAO;
import ru.arkuzmin.dais.dao.TaxiparkDAO;
import ru.arkuzmin.dais.dao.TaxiparkReplyDAO;
import ru.arkuzmin.dais.dto.Order;
import ru.arkuzmin.dais.timer.ResendOrder;
import ru.arkuzmin.dais.timer.StatusCache;

public class TaxiparkAISListener implements MessageListener {

	private static final Logger logger = CommonUtils.getLogger();
	private static Properties mqProps = PropertiesLoader.loadProperties("mq.properties");
	
	@Override
	public void onMessage(Message msg) {
		try {
			if (msg instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) msg;
				logger.debug("[TaxiparkAISListener] received message: " + MQUtils.getMsgForLog(txtMsg));

				String action = txtMsg.getStringProperty(MsgProps.ACTION);

				// Поступил ответ от таксопарка
				if (MsgProps.CONFIRM.equals(action)) {
					String status = txtMsg.getStringProperty(MsgProps.STATUS);
					
					// Если пришел статус от таксопарка
					if (MsgProps.STATUS.equals(status)) {
						String hasFree = txtMsg.getStringProperty(MsgProps.HAS_FREE);
						String hasAppropriate = txtMsg.getStringProperty(MsgProps.HAS_APPROPRIATE);
						String taxiQueue = txtMsg.getStringProperty(MsgProps.TAXI_QUEUE);
						
						Map<String, String> props = new LinkedHashMap<String, String>();
						props.put(MsgProps.ACTION, MsgProps.ORDER);
						props.put(MsgProps.STATUS, MsgProps.CONFIRM);
						props.put(MsgProps.TAXI_QUEUE, taxiQueue);
						
						// Если в этом таксопарке есть подходящие и свободные таксисты
						if (MsgProps.YES.equals(hasAppropriate) && MsgProps.YES.equals(hasFree)) {
							MsgSender.sendMessage(txtMsg.getJMSReplyTo(), null, props, mqProps.getProperty("dispTaxiparkQueue"), txtMsg.getJMSCorrelationID());
						}
					}
					
					// Нет свободных автомобилей, удовлетворяющих условия поиска
					if (MsgProps.FAILED.equals(status)) {
						
						String application_guid = txtMsg.getJMSCorrelationID();
						String taxipark_guid = txtMsg.getStringProperty(MsgProps.TAXIPARK_GUID);
						String hasFree = txtMsg.getStringProperty(MsgProps.HAS_FREE);
						String hasAppropriate = txtMsg.getStringProperty(MsgProps.HAS_APPROPRIATE);
						
						// Есть подходящие таксисты, но пока нет свободных
						if (MsgProps.YES.equals(hasAppropriate) && MsgProps.NO.equals(hasFree)) {
							TaxiparkDAO tpDAO = new TaxiparkDAO();
							TaxiparkReplyDAO tprDAO = new TaxiparkReplyDAO();
							
							boolean hasReplied = tprDAO.getReplyResendCount(application_guid, taxipark_guid) != -1 ? true : false;
							
							if (!hasReplied) {
							
								tprDAO.addNewReply(application_guid, taxipark_guid, MsgProps.BUSY);
								
								int taxiparkCount = tpDAO.getTaxiparkCount();
								int replyCountB = tprDAO.getReplyCount(application_guid, MsgProps.BUSY);
								int replyCountN = tprDAO.getReplyCount(application_guid, MsgProps.NO_APPROPRIATE);
								int replyCount = replyCountB + replyCountN;
								
								// Если ответили уже все таксисты
								if (taxiparkCount == replyCount && replyCountB != 0) {
									Order order = new Order();
									order.setOrderGUID(application_guid);
									
									ApplicationDAO dao = new ApplicationDAO();
									dao.confirmApplication(order, null, null, MsgProps.BUSY);
									
									OrderDetailsDAO odDAO = new OrderDetailsDAO();
									String orderDetailGuid = dao.getOrderDetailGuid(application_guid);
									odDAO.updateStatus(MsgProps.BUSY, "All cars are busy now, please wait", null, orderDetailGuid);
									
									// Получаем список занятых, удовлетворяющих условиям поиска таксистов
									List<String> busyTP = tprDAO.selectBusyTaxiparks(application_guid, MsgProps.BUSY);
									
									// Запускаем задачу по формированию заказов этим таксопаркам, до тех пор, пока заказ не будет принят в обработку
									ResendOrder.resendOrder(busyTP, application_guid, 30);
								}
							}
							
						// Нет подходящих таксистов
						} else if (MsgProps.NO.equals(hasAppropriate)) {
							TaxiparkDAO tpDAO = new TaxiparkDAO();
							TaxiparkReplyDAO tprDAO = new TaxiparkReplyDAO();
							tprDAO.addNewReply(application_guid, taxipark_guid, MsgProps.NO_APPROPRIATE);
							
							int taxiparkCount = tpDAO.getTaxiparkCount();
							int replyCountN = tprDAO.getReplyCount(application_guid, MsgProps.NO_APPROPRIATE);
							int replyCountB = tprDAO.getReplyCount(application_guid, MsgProps.BUSY);
							int replyCount = replyCountN + replyCountB;
							
							// Если ни в одном таксопарке нет удовлетворяющих клиента такси
							if (taxiparkCount == replyCountN) {
								Order order = new Order();
								order.setOrderGUID(application_guid);
								
								ApplicationDAO dao = new ApplicationDAO();
								dao.confirmApplication(order, null, null, MsgProps.CANCELED);
								
								OrderDetailsDAO odDAO = new OrderDetailsDAO();
								String orderDetailGuid = dao.getOrderDetailGuid(application_guid);
								odDAO.updateStatus(MsgProps.CANCELED, "There are no appropriate cars, please, try another filter", null, orderDetailGuid);
							
							} else if (taxiparkCount == replyCount && replyCountB != 0) {
									Order order = new Order();
									order.setOrderGUID(application_guid);
									
									ApplicationDAO dao = new ApplicationDAO();
									dao.confirmApplication(order, null, null, MsgProps.BUSY);
									
									OrderDetailsDAO odDAO = new OrderDetailsDAO();
									String orderDetailGuid = dao.getOrderDetailGuid(application_guid);
									odDAO.updateStatus(MsgProps.BUSY, "All cars are busy now, please wait", null, orderDetailGuid);
									
									// Получаем список занятых, удовлетворяющих условиям поиска таксистов
									List<String> busyTP = tprDAO.selectBusyTaxiparks(application_guid, MsgProps.BUSY);
									
									// Запускаем задачу по формированию заказов этим таксопаркам, до тех пор, пока заказ не будет принят в обработку
									ResendOrder.resendOrder(busyTP, application_guid, 30);
								}
						}
						
					// Заказ успешно принят в обработку таксистом
					} else if (MsgProps.SUCCESS.equals(status)) {
						
						String application_guid = txtMsg.getJMSCorrelationID();
						String taxi_queue = txtMsg.getStringProperty(MsgProps.TAXI_QUEUE);
						String taxipark_queue = txtMsg.getStringProperty(MsgProps.TAXIPARK_QUEUE);
						
						Order order = new Order();
						order.setOrderGUID(application_guid);
						
						ApplicationDAO dao = new ApplicationDAO();
						dao.confirmApplication(order, taxipark_queue, taxi_queue, "CONFIRMED");
						
						// Запускаем задачу автоматического кэширования статуса заказа
						StatusCache.cacheStatus(5, taxi_queue, application_guid);
						
					// Заказ успешно завершен таксистом
					} else if (MsgProps.COMPLETED.equals(status)) {
						
						String application_guid = txtMsg.getJMSCorrelationID();
						String taxipark_queue = txtMsg.getStringProperty(MsgProps.TAXIPARK_QUEUE);
						String taxi_queue = txtMsg.getStringProperty(MsgProps.TAXI_QUEUE);
						
						Order order = new Order();
						order.setOrderGUID(application_guid);
						
						ApplicationDAO dao = new ApplicationDAO();
						dao.confirmApplication(order, taxipark_queue, taxi_queue, MsgProps.COMPLETED);
						
					// Заказ успешно отменен
					} else if (MsgProps.CANCELED.equals(status)) {
						String applicationGuid = txtMsg.getJMSCorrelationID();
						Order order = new Order();
						order.setOrderGUID(applicationGuid);
						
						ApplicationDAO dao = new ApplicationDAO();
						dao.confirmApplication(order, null, null, MsgProps.CANCELED);
						String odGuid = dao.getOrderDetailGuid(applicationGuid);
						OrderDetailsDAO odDAO = new OrderDetailsDAO();
						odDAO.updateStatus(MsgProps.CANCELED, "Your order was successfully canceled", "unknown", odGuid);
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
