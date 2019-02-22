package com.elyx.common.exceptions;


public class ElyxOTPNotFoundException  extends ElyxAuthenticationException{

	public ElyxOTPNotFoundException(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exceptionMessage;


	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

}
