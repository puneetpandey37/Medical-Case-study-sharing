package com.elyx.common.exceptions;

public abstract class ElyxRootException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	abstract String getExceptionMessage();
	abstract void setExceptionMessage(String exceptionMessage);
	
}
