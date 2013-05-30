package ru.arkuzmin.dais.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.ConnectionFactory;
import ru.arkuzmin.common.SQLUtils;
import ru.arkuzmin.dais.common.CommonUtils;

public class TaxiparkReplyDAO {
	
	private static final Logger logger = CommonUtils.getLogger();
	
	private static final String ADD_NEW_REPLY = "insert into taxipark_reply (application_guid, taxipark_guid, reply) values (?, ?, ?)";
	
	private static final String GET_REPLY_COUNT = "select count(*) from taxipark_reply where application_guid = ? and reply = ?";
	
	private static final String GET_TAXIPARK_GUID = "select taxipark_guid from taxipark_reply where application_guid = ? and reply = ?";
	
	private static final String UPDATE_REPLY_COUNT = "update taxipark_reply set reply_count = ? where application_guid = ? and taxipark_guid = ? ";
	
	private static final String GET_REPLY_RESEND_COUNT = "select reply_count from taxipark_reply where application_guid = ? and taxipark_guid = ?";
	
	private static final String SELECT_BUSY_TAXIPAKRS = "select taxipark_guid from taxipark_reply where application_guid = ? and reply = ?";
		
	public List<String> selectBusyTaxiparks(String application_guid, String reply) {
		List<String> result = new LinkedList<String>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(SELECT_BUSY_TAXIPAKRS);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, application_guid);
			stmt.setString(paramIndex++, reply);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.add(rs.getString(1));
			}
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}
		
		return result;
	}
	
	
	
	public int getReplyResendCount(String application_guid, String taxipark_guid) {
		int result = -1;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_REPLY_RESEND_COUNT);
			
			int paramInd = 1; 
			stmt.setString(paramInd++, application_guid);
			stmt.setString(paramInd++, taxipark_guid);
			
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
	
	
	public boolean updateReplyCount (int replyCount, String application_guid, String taxipark_guid) {
		boolean result = false;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(UPDATE_REPLY_COUNT);
			
			int paramInd = 1;
			stmt.setString(paramInd++, application_guid);
			stmt.setString(paramInd++, taxipark_guid);
			
			result = stmt.executeUpdate() == 1 ? true: false;
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		
		
		return result;
	}
	
	
	public List<String> getTaxiparkGuid (String application_guid, String reply) {
		List<String> result = new LinkedList<String>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_TAXIPARK_GUID);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, application_guid);
			stmt.setString(paramIndex++, reply);
			
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.add(rs.getString(1));
			}
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}
		
		return result;
	}
	
	public int getReplyCount (String application_guid, String reply) {
		int result = -1;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_REPLY_COUNT);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, application_guid);
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
	
	
	public boolean addNewReply(String application_guid, String taxipark_guid, String reply) {
		boolean result = false;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(ADD_NEW_REPLY);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, application_guid);
			stmt.setString(paramIndex++, taxipark_guid);
			stmt.setString(paramIndex++, reply);
			
			result = stmt.executeUpdate() == 1 ? true : false;
			
		} catch (SQLException e) {
			logger.error("Error",e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		
		
		return result;
	}
	
}
