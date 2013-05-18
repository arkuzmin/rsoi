package ru.arkuzmin.tpw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.ConnectionFactory;
import ru.arkuzmin.common.SQLUtils;
import ru.arkuzmin.tpw.common.CommonUtils;
import ru.arkuzmin.tpw.dto.Order;

public class OrderDAO {

	private static final Logger logger = CommonUtils.getLogger();
	
	private static final String ADD_ORDER = "insert into orders (order_guid, client_fullname, address, delivery_time, min_price, km_price, comfort_class) values (?, ?, ? ,?, ?, ?, ?)";
	
	private static final String SELECT_ORDER = "select order_guid, client_fullname, address, delivery_time, min_price, km_price, comfort_class from orders where order_guid = ?";
	
	public Order selectOrder (String orderGuid) {
		Order order = null;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(SELECT_ORDER);
			int paramIndex = 1;
			
			stmt.setString(paramIndex++, orderGuid);
			
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				order = new Order();
				int index = 1;
				order.setOrderGuid(rs.getString(index++));
				order.setFullName(rs.getString(index++));
				order.setAddress(rs.getString(index++));
				order.setDeliveryTime(rs.getString(index++));
				order.setMinPrice(rs.getString(index++));
				order.setKmPrice(rs.getString(index++));
				order.setComfortClass(rs.getString(index++));
			}

		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}
		
		return order;
	}
	
	public boolean addOrder(Order order, Connection conn) {
		boolean result = false;
		
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(ADD_ORDER);
			int paramIndex = 1;
			
			stmt.setString(paramIndex++, order.getOrderGuid());
			stmt.setString(paramIndex++, order.getFullName());
			stmt.setString(paramIndex++, order.getAddress());
			stmt.setString(paramIndex++, order.getDeliveryTime());
			stmt.setString(paramIndex++, order.getMinPrice());
			stmt.setString(paramIndex++, order.getKmPrice());
			stmt.setString(paramIndex++, order.getComfortClass());
			
			result = stmt.executeUpdate() == 1 ? true : false;

		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(null, stmt, null);
		}

		return result;
	}
	
}
