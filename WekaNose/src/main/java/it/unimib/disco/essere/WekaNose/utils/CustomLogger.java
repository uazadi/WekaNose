package it.unimib.disco.essere.WekaNose.utils;

import java.sql.Timestamp;
import java.util.logging.Logger;

/**
 * This class handle the log of the system. 
 * Basically it work as a Middle Man between the classes of the system and a java.util.logging.Logger.
 * The purpose of this class is keep saved an available version of the logged messages.
 * */
public class CustomLogger {

	private static Logger LOGGER = Logger.getLogger(CustomLogger.class.getName());
	private static CustomLogger instance = null;
	private String loggedMessages = "";

	private CustomLogger() {
		// Exists only to defeat instantiation.
	}
	
	public static CustomLogger getInstance() {
		if(instance == null) {
			instance = new CustomLogger();
		}
		return instance;
	}

	/**
	 * @param message to be logged
	 * @param o that want to log a message
	 */
	public void severe(String message, Object o) {
		LOGGER.severe(message);
		log(message, "SEVERE", o);
	}

	/**
	 * @param message to be logged
	 * @param o that want to log a message
	 */
	public void warning(String message, Object o) {
		LOGGER.warning(message);
		log(message, "WARNING", o);
	}

	/**
	 * @param message to be logged
	 * @param o that want to log a message
	 */
	public void info(String message, Object o) {
		LOGGER.info(message);
		log(message, "INFO", o);
	}
	
	private void log(String message, String type, Object o) {
		String className = o.getClass().toString();
		this.loggedMessages += "[" + className.substring(className.lastIndexOf('.')) + "] "
							+ "[" + new Timestamp(System.currentTimeMillis()) + "] "
							+ type +" : " + message + ";\n";
	}
	
	
	public String getLoggedMessages() {
		return loggedMessages;
	}
}
