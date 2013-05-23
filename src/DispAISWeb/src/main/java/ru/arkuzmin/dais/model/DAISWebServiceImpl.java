package ru.arkuzmin.dais.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.MsgProps;
import ru.arkuzmin.common.MsgSender;
import ru.arkuzmin.common.PropertiesLoader;
import ru.arkuzmin.dais.common.CommonUtils;
import ru.arkuzmin.dais.dao.ApplicationDAO;
import ru.arkuzmin.dais.dao.GuestDAO;
import ru.arkuzmin.dais.dao.OrderDetailsDAO;
import ru.arkuzmin.dais.dao.TaxiparkDAO;
import ru.arkuzmin.dais.dao.UserDAO;
import ru.arkuzmin.dais.dto.Guest;
import ru.arkuzmin.dais.dto.Order;
import ru.arkuzmin.dais.dto.Status;
import ru.arkuzmin.dais.dto.User;

@WebService(endpointInterface = "ru.arkuzmin.dais.model.DAISWebService")
public class DAISWebServiceImpl implements DAISWebService {
	
	private static final Properties mqProps = PropertiesLoader.loadProperties("mq.properties");
	
	private static final Logger logger = CommonUtils.getLogger();
	
	@Override
	public boolean registerNewUser(User newUser) {
		boolean result = false;
		UserDAO dao = new UserDAO();
		result = dao.addNewUser(newUser);
		return result;
	}

	@Override
	public List<Order> getUserHistory(User user) {
		ApplicationDAO dao = new ApplicationDAO();
		List<Order> history = dao.getHistory(user);
		return history;
	}
	
	
	
	@Override
	public User authorizeUser(String login, String pwd) {
		User user = null;
		UserDAO dao = new UserDAO();
		user = dao.getUserByLogin(login, pwd);
		return user;
	}

	@Override
	public boolean addUserApplication(User user, Order order) {
		boolean result = false;
		ApplicationDAO dao = new ApplicationDAO();
		result = dao.addUserApplication(user, order);
		
		// Послылаем запрос в таксопарки
		if (result) {
			TaxiparkDAO tpDao = new TaxiparkDAO();
			Map<String, String> parks = tpDao.getTaxiparks();
			
			for (String key : parks.keySet()) {
				String queue = parks.get(key);
				
				try {
					MsgSender.sendMessage(queue, null, getTPOrderParams(order), mqProps.getProperty("dispTaxiparkQueue"), order.getOrderGUID());
				} catch (JMSException e) {
					logger.error("Error", e);
				}
			}
		}
		
		return result;
	}
	
	private static Map<String, String> getTPOrderParams(Order order) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put(MsgProps.ACTION_PROP, "order");
		params.put(MsgProps.ADDRESS_PROP, order.getAddress());
		params.put(MsgProps.DELIVERY_TIME_PROP, String.valueOf(order.getDeliveryTime()));
		params.put(MsgProps.MIN_PRICE_PROP, order.getMinPrice() == null ? null : String.valueOf(order.getMinPrice()));
		params.put(MsgProps.KM_PRICE_PROP, order.getKmPrice() == null ? null : String.valueOf(order.getKmPrice()));
		params.put(MsgProps.COMFORT_CLASS_PROP, order.getComfortClass());
		return params;
	}

	@Override
	public boolean confirmApplication(Order order, boolean confirm) {
		boolean result = false;
		ApplicationDAO dao = new ApplicationDAO();
		result = dao.confirmApplication(order, null, confirm ? "CONFIRMED" : "CANCELED");
		return result;
	}

	@Override
	@WebMethod
	public Status getStatus(String guid, String userIdentifier) {
		Status status = null;
		// Запрос от зарегистрированного пользователя
		if ("R".equals(userIdentifier)) {
			ApplicationDAO appDao = new ApplicationDAO();
			String appGuid = appDao.getUserApplicationGuid(guid);
			String orderDetailGuid = appDao.getOrderDetailGuid(appGuid);
			
			OrderDetailsDAO odDao = new OrderDetailsDAO();
			Order order = odDao.getOrderByGuid(orderDetailGuid);
			
			status = new Status();
			status.setOrder(order);
			
		// Запрос от гостя
		} else {
			ApplicationDAO appDao = new ApplicationDAO();
			String appGuid = appDao.getGuestApplicationGuid(guid);
			String orderDetailGuid = appDao.getOrderDetailGuid(appGuid);
			
			OrderDetailsDAO odDao = new OrderDetailsDAO();
			Order order = odDao.getOrderByGuid(orderDetailGuid);
			
			status = new Status();
			status.setOrder(order);	
		}
		return status;
	}

	@Override
	public String addGuestApplication(Order order) {
		
		boolean result = false;
		
		ApplicationDAO dao = new ApplicationDAO();
		GuestDAO guestDAO = new GuestDAO();
		Guest guest = guestDAO.addNewGuest();
		result = dao.addGuestApplication(guest, order);
		
		// Послылаем запрос в таксопарки
		if (result) {
			TaxiparkDAO tpDao = new TaxiparkDAO();
			Map<String, String> parks = tpDao.getTaxiparks();
			
			for (String key : parks.keySet()) {
				String queue = parks.get(key);
				
				try {
					MsgSender.sendMessage(queue, null, getTPOrderParams(order), mqProps.getProperty("dispTaxiparkQueue"), order.getOrderGUID());
				} catch (JMSException e) {
					logger.error("Error", e);
				}
			}
		}
		
		return guest.getCode();
	}

}
