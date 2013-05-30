package ru.arkuzmin.tpw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.ConnectionFactory;
import ru.arkuzmin.common.SQLUtils;
import ru.arkuzmin.tpw.common.CommonUtils;

public class TaxiReplyDAO {

	private static final Logger logger = CommonUtils.getLogger();
	
	private static final String ADD_REPLY = "insert into taxi_reply (order_guid, taxi_guid, reply) values (?,?,?)";
	
	private static final String GET_REPLY_COUNT = "select count(reply) from taxi_reply where order_guid = ? and taxi_guid = ? and reply = ?";
	
	private static final String CLEAR_REPLY = "delete from taxi_reply where order_guid = ?";
	
	
	public boolean clearReply (String order_guid) {
	boolean result = false;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(CLEAR_REPLY);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, order_guid);
			
			result = stmt.executeUpdate() == 1 ? true : false;
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		
		return result;
	}
	
	public int getReplyCount(String order_guid, String taxi_guid, String reply) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_REPLY_COUNT);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, order_guid);
			stmt.setString(paramIndex++, taxi_guid);
			stmt.setString(paramIndex++, reply);
			
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
	
	public boolean addReply(String order_guid, String taxi_guid, String reply) {
		boolean result = false;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(ADD_REPLY);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, order_guid);
			stmt.setString(paramIndex++, taxi_guid);
			stmt.setString(paramIndex++, reply);
			
			result = stmt.executeUpdate() == 1 ? true : false;
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		
		return result;
	}
	
}
