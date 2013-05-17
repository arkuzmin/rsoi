package ru.arkuzmin.tw.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class SQLUtils {
	
	private static final Logger logger = CommonUtils.getLogger();
	
	public static void closeSQLObjects (Connection conn, PreparedStatement stmt, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Error", e);
			}
		}
		
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("Error", e);

			}
		}
		
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Error", e);
			}
		}
	}
}
