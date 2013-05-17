package ru.arkuzmin.tw.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import ru.arkuzmin.tw.common.CommonUtils;
import ru.arkuzmin.tw.common.PropertiesLoader;

public class ConnectionFactory {
	
	private static final Logger logger = CommonUtils.getLogger();
	
	private Properties properties;

	private static ConnectionFactory connectionFactory = null;

	private ConnectionFactory() {
		try {
			properties = PropertiesLoader.loadProperties("db.properties");
			Class.forName(properties.getProperty("driverClassName"));
			
		} catch (ClassNotFoundException e) {
			logger.error("Error", e);
		}
	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(properties.getProperty("connectionURL"), properties.getProperty("user"), properties.getProperty("pwd"));
		return conn;
	}

	public static ConnectionFactory getInstance() {
		if (connectionFactory == null) {
			connectionFactory = new ConnectionFactory();
		}
		return connectionFactory;
	}
}
