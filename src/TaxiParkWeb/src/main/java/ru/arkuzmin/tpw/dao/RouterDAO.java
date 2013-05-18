package ru.arkuzmin.tpw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.ConnectionFactory;
import ru.arkuzmin.common.SQLUtils;
import ru.arkuzmin.tpw.common.CommonUtils;
import ru.arkuzmin.tpw.dto.Order;

/**
 * Доступ к таблице маршрутизации.
 * @author ArKuzmin
 *
 */
public class RouterDAO {

	private static final Logger logger = CommonUtils.getLogger();

	private static final String ADD_ROUTE = "insert into router (disp_corrid, taxi_corrid, order_guid, order_status) values (?,?,?,?)";

	private static final String GET_ROUTE_BY_DISP = "select taxi_corrid, order_guid from router where disp_corrid = ? ";

	private static final String GET_ROUTE_BY_TAXI = "select disp_corrid, order_guid from router where taxi_corrid = ?";

	private static final String CHECK_STATUS_BY_DISP = "select order_status from router where disp_corrid = ?";
	
	private static final String CHECK_STATUS_BY_TAXI = "select order_status from router where taxi_corrid = ?";
	
	private static final String CHANGE_STATUS_BY_DISP = "update router set order_status = ? where disp_corrid = ?";
	
	private static final String CHANGE_STATUS_BY_TAXI = "update router set order_status = ? where taxi_corrid = ?";
	
	/**
	 * Возвращает маршрут.
	 * @param sql
	 * @param param
	 * @return
	 */
	private Map<String, String> getRoute(String sql, String param) {
		Map<String, String> result = new LinkedHashMap<String, String>();

		if (param == null || "".equals(param)) {
			return null;
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, param);

			rs = stmt.executeQuery();

			if (rs.next()) {
				result.put(rs.getString(1), rs.getString(2));
			}

		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}

		return result;
	}

	
	private String checkStatus(String sql, String param) {
		String result = null;

		if (param == null || "".equals(param)) {
			return result;
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, param);

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
	
	private boolean changeStatus(String sql, String id, String status) {
		
		boolean result = false;

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(sql);
			int paramIndex = 1;
			stmt.setString(paramIndex++, status);
			stmt.setString(paramIndex++, id);
			

			result = stmt.executeUpdate() == 1 ? true: false;

		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}

		return result;
	}
	
	public Map<String, String> getRouteByTaxi(String taxiCorrId) {
		return getRoute(GET_ROUTE_BY_TAXI, taxiCorrId);
	}

	public Map<String, String> getRouteByDisp(String dispCorrId) {
		return getRoute(GET_ROUTE_BY_DISP, dispCorrId);
	}

	public String checkStatusByDisp(String dispCorrId) {
		return checkStatus(CHECK_STATUS_BY_DISP, dispCorrId);
	}
	
	public String checkStatusByTaxi(String taxiCorrId) {
		return checkStatus(CHECK_STATUS_BY_TAXI, taxiCorrId);
	}
	
	public boolean changeStatusByTaxi(String taxiCorrId, String status) {
		return changeStatus(CHANGE_STATUS_BY_TAXI, taxiCorrId, status);
	}
	
	public boolean changeStatusByDisp(String dispCorrId, String status) {
		return changeStatus(CHANGE_STATUS_BY_DISP, dispCorrId, status);
	}
	
	
	public Order selectOrderDetailsByTaxi(String taxiCorrId) {
		Order order = null;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_ROUTE_BY_TAXI);
			stmt.setString(1, taxiCorrId);
			
			rs = stmt.executeQuery();
			String orderGuid = null;
			
			if (rs.next()) {
				orderGuid = rs.getString(2);
			}
			
			OrderDAO dao = new OrderDAO();
			order = dao.selectOrder(orderGuid);
			
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}

		return order;
	}
	
	public boolean addRoute(String dispCorrID, String taxiCorrID, Order order) {
		boolean result = false;

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			
			OrderDAO dao = new OrderDAO();
			if (dao.addOrder(order, conn)) {
				stmt = conn.prepareStatement(ADD_ROUTE);
				stmt.setString(1, dispCorrID);
				stmt.setString(2, taxiCorrID);
				stmt.setString(3, order.getOrderGuid());
				stmt.setString(4, "not_assigned");

				result = stmt.executeUpdate() == 1 ? true : false;
			}
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}

		return result;
	}

}
