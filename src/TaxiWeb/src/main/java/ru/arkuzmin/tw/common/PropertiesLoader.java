package ru.arkuzmin.tw.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesLoader {
	
	private static final Logger logger = CommonUtils.getLogger();
	
	public static Properties loadProperties(String propName) {
		Properties properties = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream is = classLoader.getResourceAsStream(propName);
			properties = new Properties();
			properties.load(is);
		} catch (IOException e) {
			logger.error("Error", e);
		}
		return properties;
	}
}
