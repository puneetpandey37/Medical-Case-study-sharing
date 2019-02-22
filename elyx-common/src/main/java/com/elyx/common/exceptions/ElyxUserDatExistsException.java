package com.elyx.common.exceptions;

public class ElyxUserDatExistsException extends ElyxRootException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exceptionMessage;

	public ElyxUserDatExistsException(String exceptionMessage) {
		super();
		this.exceptionMessage = exceptionMessage;
	}

	@Override
	public String getExceptionMessage() {
		return exceptionMessage;
	}

	@Override
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

}
