package ru.arkuzmin.dais.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import ru.arkuzmin.dais.common.CommonUtils;
import ru.arkuzmin.dais.common.SQLUtils;
import ru.arkuzmin.dais.dto.Order;
import ru.arkuzmin.dais.dto.User;

/**
 * Доступ к таблице заявок.
 * @author ArKuzmin
 *
 */
public class ApplicationDAO {

	private static final Logger logger = CommonUtils.getLogger();
	
	/**
	 * Запрос на добавление новой заявки от пользователя.
	 */
	private static final String ADD_APPLICATION = 
		"insert into rsoi_disp.application (application_guid, application_st, requester_guid, order_detail_guid, user_identifier) " +
		"values (?, ?, ?, ?, ?)";
	
	/**
	 * Запрос на получение истории пользователя.
	 */
	private static final String GET_HISTORY = 
		"select distinct o.address, o.delivery_time, o.km_price, o.min_price, o.comfort_class, o.order_status, a.application_dt, a.application_st " +
		"from rsoi_disp.application a, rsoi_disp.order_details o " +
		"where a.requester_guid = ? and a.order_detail_guid = o.order_detail_guid order by a.application_dt desc";
	
	/**
	 * Запрос на подтверждение или отмену заявки.
	 */
	private static final String CONFIRM_APPLICATION = 
		"update rsoi_disp.application set application_st = ?, taxi_queue = ?  where application_guid = ?";

	
	
	
	/**
	 * Выполняет подтверждение или отмену заявки.
	 * @param order - заявка
	 * @param confirm - подтверждение или отмена
	 * @return
	 */
	public boolean confirmApplication(Order order, String taxi_queue, String status) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean result = false;
		
		if (order == null) {
			return result;
		}
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(CONFIRM_APPLICATION);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, status);
			stmt.setString(paramIndex++, taxi_queue == null ? "unknown" : taxi_queue);
			stmt.setString(paramIndex++, order.getOrderGUID());
			
			result = stmt.executeUpdate() == 1 ? true : false;
			
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		
		return result;
	}
	
	/**
	 * Возвращает историю заказов.
	 * @param user - пользователь
	 * @return
	 */
	public List<Order> getHistory(User user) {
		List<Order> history = new LinkedList<Order>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		if (user == null) {
			return null;
		}
	
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_HISTORY);
				
			int paramIndex = 1;
			stmt.setString(paramIndex++, user.getGuid());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int index = 1;
				Order order = new Order();
				order.setAddress(rs.getString(index++));
				order.setDeliveryTime(rs.getTimestamp(index++));
				order.setKmPrice(rs.getDouble(index++));
				order.setMinPrice(rs.getDouble(index++));
				order.setComfortClass(rs.getString(index++));
				order.setOrderStatus(rs.getString(index++));
				order.setOrderDt(rs.getTimestamp(index++));
				
				history.add(order);
			}
			
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}
		
		return history;
	}
	
	/**
	 * Добавление нововой заявки от пользователя.
	 * @param user - пользователь
	 * @param order - заказ
	 * @return
	 */
	public boolean addUserApplication(User user, Order order) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean result = false;
		
		if (user == null || order == null) {
			return result;
		}
	
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			OrderDetailsDAO odDAO = new OrderDetailsDAO();
			order.setOrderGUID(UUID.randomUUID().toString());
			order.setOrderDetailsGUID(UUID.randomUUID().toString());
			order.setRequesterGUID(user.getGuid());
			order.setOrderStatus("UNKNOWN");
			boolean aod = odDAO.addOrderDetails(conn, order);
			boolean aua = false;
			
			if (aod) {
				
				stmt = conn.prepareStatement(ADD_APPLICATION);
				
				int paramIndex = 1;
				stmt.setString(paramIndex++, order.getOrderGUID());
				stmt.setString(paramIndex++, "NOT_CONFIRMED");
				stmt.setString(paramIndex++, order.getRequesterGUID());
				stmt.setString(paramIndex++, order.getOrderDetailsGUID());
				stmt.setString(paramIndex++, "R");
				
				aua = stmt.executeUpdate() == 1 ? true : false;
			}
			
			result = aod && aua;
			
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		
		return result;
	}
}
