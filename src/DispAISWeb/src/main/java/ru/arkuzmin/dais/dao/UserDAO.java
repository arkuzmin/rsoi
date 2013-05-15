package ru.arkuzmin.dais.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

import org.apache.log4j.Logger;

import ru.arkuzmin.dais.common.CommonUtils;
import ru.arkuzmin.dais.common.SQLUtils;
import ru.arkuzmin.dais.dto.User;

/**
 * ДАО класс для таблицы пользователей.
 * @author ArKuzmin
 *
 */
public class UserDAO {
	
	private static final Logger logger = CommonUtils.getLogger();
	
	/**
	 * Запрос на добавление нового пользователя.
	 */
	private static final String ADD_NEW_USER = 
		"insert into rsoi_disp.user (user_guid, user_login, user_pwd, user_name, user_lname, user_mname) " +
		"values (?, ?, ?, ?, ?, ?)";
	
	private static final String GET_USER = 
		"select user_guid, user_name, user_login, user_pwd, user_lname, user_mname from rsoi_disp.user " +
		"where user_login = ? and user_pwd = ? ";
	
	/**
	 * Возвращает пользователя по логину и паролю.
	 * @param login - логин пользователя
	 * @param pwd - пароль пользователя
	 * @return
	 */
	public User getUserByLogin(String login, String pwd) {
		User user = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		if (login == null || "".equals(login) || pwd == null || "".equals(pwd)) {
			return null;
		}
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(GET_USER);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, login);
			stmt.setString(paramIndex++, pwd);
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				user = new User();
				int index = 1;
				user.setGuid(rs.getString(index++));
				user.setName(rs.getString(index++));
				user.setLogin(rs.getString(index++));
				user.setPwd(rs.getString(index++));
				user.setLname(rs.getString(index++));
				user.setMname(rs.getString(index++));
			}
		} catch (SQLException e) {
			logger.error("Error", e);
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, rs); 
		}
		return user;
	}
	
	/**
	 * Добавляет нового пользователя.
	 * @param newUser - новый пользователь
	 * @return true - если добавление успешно, false - иначе
	 */
	public boolean addNewUser(User newUser) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean result = false;

		if (newUser == null || 
			newUser.getLogin() == null || "".equals(newUser.getLogin()) || 
			newUser.getPwd() == null || "".equals(newUser.getPwd()) || 
			newUser.getName() == null || "".equals(newUser.getName())) {
			return result;
		}
		
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.prepareStatement(ADD_NEW_USER);
			
			int paramIndex = 1;
			stmt.setString(paramIndex++, UUID.randomUUID().toString());
			stmt.setString(paramIndex++, newUser.getLogin());
			stmt.setString(paramIndex++, newUser.getPwd());
			stmt.setString(paramIndex++, newUser.getName());
			if (newUser.getLname() == null || "".equals(newUser.getLname())) {
				stmt.setNull(paramIndex++, Types.VARCHAR);
			} else {
				stmt.setString(paramIndex++, newUser.getLname());
			}
			
			if (newUser.getMname() == null || "".equals(newUser.getMname())) {
				stmt.setNull(paramIndex++, Types.VARCHAR);
			} else {
				stmt.setString(paramIndex++, newUser.getMname());
			}
			
			result = stmt.executeUpdate() == 1 ? true : false;
			
		} catch (SQLException e) {
			logger.error("Error accessing DB", e);
			result = false;
			
		} finally {
			SQLUtils.closeSQLObjects(conn, stmt, null);
		}
		return result;
	}
}
