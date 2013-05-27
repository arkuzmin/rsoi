package ru.arkuzmin.common;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.TextMessage;

public class MQUtils {

	private static final String NEW_LINE = "\n";
	
	public static String getMsgForLog(TextMessage msg) {
		StringBuilder result = new StringBuilder("");
		
		
		try {
			result.append(NEW_LINE)
			      .append("[CorrelationID]: ").append(msg.getJMSCorrelationID()).append(NEW_LINE)
				  .append("[ReplyTo]:       ").append(msg.getJMSReplyTo()).append(NEW_LINE);
			
			@SuppressWarnings("unchecked")
			Enumeration<String> props = msg.getPropertyNames();
			while (props.hasMoreElements()) {
				String prop = props.nextElement();
				result.append("[").append(prop).append("]:").append(msg.getStringProperty(prop)).append(NEW_LINE);
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
