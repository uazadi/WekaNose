package it.unimib.disco.essere.WekaNose.exceptions;

import java.util.logging.Logger;


public class WorkspaceException extends Exception {
	
	/** serial version */
	private static final long serialVersionUID = 1L;
	
	/** logger */
	private static Logger LOGGER = Logger.getLogger(WorkspaceException.class.getName());
	
	/** The error message print by DFCM4J */
	//private String message;
	
	/** Custom error message */
	private String m_message;
	
	public WorkspaceException(String message){
		this.m_message = "Unable to create or modify the workspace, please make sure that the program is run with admin privileges";
		LOGGER.severe(m_message);
	}
	
	@Override
	public String getMessage(){
		return this.m_message;
	}
}
