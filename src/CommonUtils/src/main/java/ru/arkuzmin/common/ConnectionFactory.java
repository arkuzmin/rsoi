package ru.arkuzmin.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	
	
	private Properties properties;

	private static ConnectionFactory connectionFactory = null;

	private ConnectionFactory() {
		try {
			properties = PropertiesLoader.loadProperties("db.properties");
			Class.forName(properties.getProperty("driverClassName"));
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
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
