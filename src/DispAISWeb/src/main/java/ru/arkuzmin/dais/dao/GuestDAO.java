package ru.arkuzmin.dais.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.log4j.Logger;

import ru.arkuzmin.common.ConnectionFactory;
import ru.arkuzmin.common.SQLUtils;
import ru.arkuzmin.dais.common.CommonUtils;
import ru.arkuzmin.dais.dto.Guest;


/**
 * Класс для доступа к таблице незарегистрированных пользователей.
 * @author ArKuzmin
 *
 */
public class GuestDAO {
	
	private static final Logger logger = CommonUtils.getLogger();
	
	/**
	 * Запрос на добавление нового незарегистрированного пользователя.
	 */
	private static final String ADD_GUEST = 
		"insert into rsoi_disp.guest (guest_guid, guest_code) " +
		"values (?,?)";
	
	
	private static final String GET_GUID_BY_CODE = 
		"select guest_guid from guest where guest_code = ?";
	
	
	public String getGuidByCode(String code) {
		String result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_GUID_BY_CODE);
			
			stmt.setString(1, code);
			
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
	 * Добавление нового незарегистрированного пользователя.
	 * @return
	 */
	public Guest addNewGuest() {
		Connection conn = null;
		PreparedStatement stmt = null;
		Guest guest = null;
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(ADD_GUEST);
			
			String code = UUID.randomUUID().toString();
			String guid = UUID.randomUUID().toString();
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, guid);
			stmt.setString(paramIndex++, code);
			
			if (stmt.executeUpdate() == 1) {
				guest = new Guest();
				guest.setGuid(guid);
				guest.setCode(code);
			}
			
		} catch (SQLException e) {
			logger.error("Error accessing DB", e);
			guest = null;
			
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		return guest;
	}
}
