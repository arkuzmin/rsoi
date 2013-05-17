package ru.arkuzmin.tw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ru.arkuzmin.tw.common.CommonUtils;
import ru.arkuzmin.tw.common.SQLUtils;

public class TaxiDAO {

	private static final Logger logger = CommonUtils.getLogger();
	
	private static final String CHECK_STATUS = "select status from car";
	
	private static final String CHANGE_STATUS = "update car set status = ?";
	
	/**
	 * Возвращает текущий статус автомобиля.
	 * @return
	 */
	public String chechStatus() {
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
