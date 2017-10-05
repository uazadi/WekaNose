package it.unimib.disco.essere.WekaNose.exception;

import java.util.logging.Logger;

public class DFCM4JException extends Exception {

	/** serial version */
	private static final long serialVersionUID = 1L;
	
	/** logger */
	private static Logger LOGGER = Logger.getLogger(DFCM4JException.class.getName());
	
	/** The error message print by DFCM4J */
	private Exception e;
	
	/** Custom error message */
	private String m_message;
	
	public DFCM4JException(Exception e){
		this.m_message = "DFCM4J raise an error, unable to load the project. Please check again the input values";
		this.e = e;
		e.printStackTrace();
		LOGGER.severe(m_message);
	}
	
	public Exception getException() {
		return e;
	}


	@Override
	public String getMessage(){
		return this.m_message;
	}
	
}
