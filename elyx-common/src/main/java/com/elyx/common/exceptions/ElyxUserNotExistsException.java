package com.elyx.common.exceptions;


public class ElyxUserNotExistsException extends ElyxAuthenticationException{

	public ElyxUserNotExistsException(String exceptionMessage) {
		this.exceptionMessage=exceptionMessage;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exceptionMessage;
	String getExceptionMessage() {
		return exceptionMessage;
	}

	void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

}
