package it.unimib.disco.essere.WekaNose.exception;

import java.util.logging.Logger;


public class NotValidConditionException extends Exception {
	/** serial version */
	private static final long serialVersionUID = 1L;
	
	/** logger */
	private static Logger LOGGER = Logger.getLogger(NotValidConditionException.class.getName());
	
	/** The error message print by DFCM4J */
	//private String message;
	
	/** Custom error message */
	private String m_message;
	
	public NotValidConditionException(String message){
		this.m_message = "Not valid condition :" + message;
		LOGGER.severe(m_message);
	}
	
	@Override
	public String getMessage(){
		return this.m_message;
	}
}
