package com.elyx.common.exceptions;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ElyxNoContentException extends ElyxRootException{
	
	private static final long serialVersionUID = 1L;
	
	private String exceptionMessage;
	public ElyxNoContentException(String exceptionMessage){
		this.exceptionMessage=exceptionMessage;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
