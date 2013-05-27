package ru.arkuzmin.tw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.ConnectionFactory;
import ru.arkuzmin.common.SQLUtils;
import ru.arkuzmin.tw.common.CommonUtils;

public class TaxiDAO {

	private static final Logger logger = CommonUtils.getLogger();
	
	private static final String CHECK_STATUS = "select status from car";
	
	private static final String CHANGE_STATUS = "update car set status = ?";
	
	private static final String GET_COORDINATES = "select coordinates from car";
	
	private static final String SET_COORDINATES = "update car set coordinates = ?";
	
	private static final String GET_GUID = "select car_guid from car";
	
	/**
	 * Возвращает текущий статус автомобиля.
	 * @return
	 */
	public String checkStatus() {
		String result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(CHECK_STATUS);
			
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
	
	/**
	 * Возвращает текущий статус автомобиля.
	 * @return
	 */
	public String getTaxiGuid() {
		String result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_GUID);
			
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
	
	/**
	 * Обновление текущих координат автомобиля.
	 * @param newStatus
	 * @return
	 */
	public boolean setCoordinates(String coordinates) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(SET_COORDINATES);
			
			stmt.setString(1, coordinates);
			
			result = stmt.executeUpdate() == 1 ? true : false;
			
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		return result;
	}
	
	/**
	 * Возвращает текущие координаты автомобиля.
	 * @return
	 */
	public String getCoordinates() {
		String result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_COORDINATES);
			
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
	
	/**
	 * Смена текущего статуса автомобиля.
	 * @param newStatus
	 * @return
	 */
	public boolean changeStatus(String newStatus) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(CHANGE_STATUS);
			
			stmt.setString(1, newStatus);
			
			result = stmt.executeUpdate() == 1 ? true : false;
			
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		return result;
	}
}
