package ru.arkuzmin.tpw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.ConnectionFactory;
import ru.arkuzmin.common.SQLUtils;
import ru.arkuzmin.tpw.common.CommonUtils;
import ru.arkuzmin.tpw.dto.Car;

/**
 * Доступ к автомобилям такси.
 * 
 * @author ArKuzmin
 * 
 */
public class CarsDAO {

	private static final Logger logger = CommonUtils.getLogger();

	private static final String WHERE_FILTER = "WHERE_FILTER";

	private static final String GET_CARS_COUNT = "select count(*) from cars";
	
	private static final String SELECT_CARS = "select car_guid, car_mark, min_price, km_price, comfort_class, queue_id from cars where "
			+ WHERE_FILTER;

	private static final String GET_QUEUE_BY_GUID = "select queue_id from cars where car_guid = ?";

	public int getCarsCount() {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_CARS_COUNT);
			
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}
		
		return result;
	}
	
	public List<Car> selectCars(String minPrice, String kmPrice,
			String comfortClass) {

		List<Car> result = new LinkedList<Car>();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			String sql = SELECT_CARS;
			sql = sql.replaceAll(WHERE_FILTER,
					createWhereClause(minPrice, kmPrice, comfortClass));

			stmt = conn.prepareStatement(sql);

			int paramIndex = 1;
			paramIndex = setWhereClauseParams(stmt, paramIndex, minPrice,
					kmPrice, comfortClass);

			rs = stmt.executeQuery();

			while (rs.next()) {
				Car car = new Car();
				int index = 1;
				car.setCarGuid(rs.getString(index++));
				car.setCarMark(rs.getString(index++));
				car.setMinPrice(rs.getDouble(index++));
				car.setKmPrice(rs.getDouble(index++));
				car.setComfortClass(rs.getString(index++));
				car.setQueueID(rs.getString(index++));
				result.add(car);
			}

		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}

		return result;
	}

	public String getQueueID (String carGuid) {

		String result = null;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_QUEUE_BY_GUID);

			int paramIndex = 1;
			stmt.setString(paramIndex++, carGuid);

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

	private int setWhereClauseParams(PreparedStatement stmt, int paramIndex,
			String minPrice, String kmPrice, String comfortClass)
			throws SQLException {

		if (minPrice != null && !"".equals(minPrice)) {
			double price = Double.parseDouble(minPrice);
			stmt.setDouble(paramIndex++, price);
		}

		if (kmPrice != null && !"".equals(kmPrice)) {
			double price = Double.parseDouble(kmPrice);
			stmt.setDouble(paramIndex++, price);
		}

		if (comfortClass != null && !"".equals(comfortClass)) {
			stmt.setString(paramIndex++, comfortClass);
		}

		return paramIndex;
	}

	private String createWhereClause(String minPrice, String kmPrice,
			String comfortClass) {
		StringBuilder sb = new StringBuilder("");

		sb.append(" car_status = 'free' ");

		if (minPrice != null && !"".equals(minPrice)) {
			sb.append(" and min_price <= ? ");
		}

		if (kmPrice != null && !"".equals(kmPrice)) {
			sb.append(" and km_price <= ? ");
		}

		if (comfortClass != null && !"".equals(comfortClass)) {
			sb.append(" and comfort_class = ? ");
		}

		return sb.toString();
	}

}
