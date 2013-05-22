package ru.arkuzmin.dais.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.ConnectionFactory;
import ru.arkuzmin.common.SQLUtils;
import ru.arkuzmin.dais.common.CommonUtils;

public class TaxiparkDAO {
	private static final Logger logger = CommonUtils.getLogger();
	
	private static final String GET_TAXIPARKS = "select taxipark_guid, taxipark_queue from taxiparks";
	
	public Map<String, String> getTaxiparks() {
		Map<String, String> result = new LinkedHashMap<String, String>();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_TAXIPARKS);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				result.put(rs.getString(1), rs.getString(2));
			}
			
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs);
		}
		
		
		return result;
	}
}
