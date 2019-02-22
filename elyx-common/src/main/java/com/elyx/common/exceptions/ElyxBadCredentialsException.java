package com.elyx.common.exceptions;


public class ElyxBadCredentialsException  extends ElyxAuthenticationException{

	private static final long serialVersionUID = 1L;

	private String exceptionMessage;
	public ElyxBadCredentialsException(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	/**
	 * 
	 */
	String getExceptionMessage() {
		return exceptionMessage;
	}

	void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
