package ru.arkuzmin.dais.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.ConnectionFactory;
import ru.arkuzmin.common.SQLUtils;
import ru.arkuzmin.dais.common.CommonUtils;
import ru.arkuzmin.dais.dto.Order;

/**
 * Доступ к таблице с описанием заказа.
 * @author ArKuzmin
 *
 */
public class OrderDetailsDAO {
	
	private static final Logger logger = CommonUtils.getLogger();
	
	private static final String GET_ORDER_BY_GUID = "select address, delivery_time, km_price, min_price, comfort_class, order_status, status_description, coordinates from order_details where order_detail_guid = ?";
	
	/**
	 * Запрос на добавление информации по заказу.
	 */
	private static final String ADD_ORDER_DETAILS = 
		"insert into rsoi_disp.order_details (order_detail_guid, address, delivery_time, km_price, min_price, comfort_class, order_status, coordinates) " +
		"values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String UPDATE_STATUS = 
		"update order_details set order_status = ?, status_description = ?, coordinates = ? where order_detail_guid = ?";
	
	private static final String CHECK_STATUS = 
		"select order_status from order_details where order_detail_guid = ?";
	
	
	public Order getOrderByGuid(String guid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Order result = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_ORDER_BY_GUID);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, guid);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				result = new Order();
				int index = 1;
				result.setAddress(rs.getString(index++));
				result.setDeliveryTime(rs.getTimestamp(index++));
				result.setKmPrice(rs.getDouble(index++));
				result.setMinPrice(rs.getDouble(index++));
				result.setComfortClass(rs.getString(index++));
				result.setOrderStatus(rs.getString(index++));
				result.setOrderStatusDescription(rs.getString(index++));
				result.setCoordinates(rs.getString(index++));
			}
			
		} catch (SQLException e) {
			logger.error("Error", e);
			
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}
		
		return result;
	}
	
	public String checkStatus(String orderDetailGuid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(CHECK_STATUS);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, orderDetailGuid);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getString(1);
			}
			
		} catch (SQLException e) {
			logger.error("Error", e);
			
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}
		
		return result;
	}
	
	public boolean updateStatus(String status, String description, String coordinates, String orderDetailGuid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean result = false;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(UPDATE_STATUS);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, status);
			stmt.setString(paramIndex++, description);
			stmt.setString(paramIndex++, coordinates);
			stmt.setString(paramIndex++, orderDetailGuid);
			
			result = stmt.executeUpdate() == 1 ? true : false;	
			
		} catch (SQLException e) {
			logger.error("Error", e);
			
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		
		return result;
	}
	
	/**
	 * Добавление информации по заказу.
	 * @param order - заказ
	 * @return
	 */
	public boolean addOrderDetails(Connection conn, Order order) {
		PreparedStatement stmt = null;
		boolean result = false;
		
		if (order == null) {
			return result;
		}
		
		try {
			stmt = conn.prepareStatement(ADD_ORDER_DETAILS);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, order.getOrderDetailsGUID());
			stmt.setString(paramIndex++, order.getAddress());
			stmt.setTimestamp(paramIndex++, new Timestamp(order.getDeliveryTime().getTime()));
			if (order.getKmPrice() == null) {
				stmt.setNull(paramIndex++, Types.DOUBLE);
			} else {
				stmt.setDouble(paramIndex++, order.getKmPrice());
			}
			
			if (order.getMinPrice() == null) {
				stmt.setNull(paramIndex++, Types.DOUBLE);
			} else {
				stmt.setDouble(paramIndex++, order.getMinPrice());
			}
			
			if (order.getComfortClass() == null) {
				stmt.setNull(paramIndex++, Types.VARCHAR);
			} else {
				stmt.setString(paramIndex++, order.getComfortClass());
			}
			
			stmt.setString(paramIndex++, order.getOrderStatus());
			
			if (order.getCoordinates() == null || "".equals(order.getCoordinates())) {
				stmt.setNull(paramIndex++, Types.VARCHAR);
			} else {
				stmt.setString(paramIndex++, order.getCoordinates());
			}
			
			result = stmt.executeUpdate() == 1 ? true : false;	
			
		} catch (SQLException e) {
			logger.error("Error", e);
			
		} finally {
			SQLUtils.closeSQLObjects(null, stmt, null);
		}
		
		return result;
	}
}
