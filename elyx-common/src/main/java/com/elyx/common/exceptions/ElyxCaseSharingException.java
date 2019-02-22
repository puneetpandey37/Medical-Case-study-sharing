package com.elyx.common.exceptions;

public class ElyxCaseSharingException extends ElyxRootException{
	
	/**
	 * 
	 */
	private String exceptionMessage;
	private static final long serialVersionUID = 1L;
	public ElyxCaseSharingException(String exceptionMessage){
		this.exceptionMessage=exceptionMessage;
	}
	@Override
	String getExceptionMessage() {
		return exceptionMessage;
	}

	@Override
	void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

}
