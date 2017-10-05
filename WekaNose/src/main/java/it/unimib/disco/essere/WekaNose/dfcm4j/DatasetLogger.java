package it.unimib.disco.essere.WekaNose.dfcm4j;

import java.sql.Timestamp;
import java.util.logging.Logger;

public class DatasetLogger {

	private static Logger LOGGER = Logger.getLogger(DatasetLogger.class.getName());
	private static DatasetLogger instance = null;
	private String loggedMessages = "";

	private DatasetLogger() {
		// Exists only to defeat instantiation.
	}
	
	public static DatasetLogger getInstance() {
		if(instance == null) {
			instance = new DatasetLogger();
		}
		return instance;
	}

	public void severe(String s, Object o) {
		LOGGER.severe(s);
		log(s, "SEVERE", o);
	}

	public void warning(String s, Object o) {
		LOGGER.warning(s);
		log(s, "WARNING", o);
	}

	public void info(String s, Object o) {
		LOGGER.info(s);
		log(s, "INFO", o);
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
