package com.elyx.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ElyxExceptionHandler {


	@ExceptionHandler(ElyxUserDatExistsException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CONFLICT)// 409
	public ElyxExceptionResponse elyxUserDatExistsException(
			ElyxUserDatExistsException dataAlreadyExistsException) {
		String errorMessage = dataAlreadyExistsException.getExceptionMessage();
		ElyxExceptionResponse exceptionResponse = new ElyxExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.CONFLICT);
		exceptionResponse.setException(errorMessage);
		return exceptionResponse;
	}
	
	@ExceptionHandler(ElyxInvalidRequestException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ElyxExceptionResponse elyxInvalidRequestException(
			ElyxInvalidRequestException invalidRequestException) {
		String errorMessage = invalidRequestException.getExceptionMessage();
		ElyxExceptionResponse exceptionResponse = new ElyxExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
		exceptionResponse.setException(errorMessage);
		return exceptionResponse;
	} 
	
	@ExceptionHandler(ElyxUserNotExistsException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ElyxExceptionResponse elyxUserNotExistsException(
			ElyxUserNotExistsException userNotExistsException) {
		String errorMessage = userNotExistsException.getExceptionMessage();
		ElyxExceptionResponse exceptionResponse = new ElyxExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.UNAUTHORIZED);
		exceptionResponse.setException(errorMessage);
		return exceptionResponse;
	}
	
	@ExceptionHandler(ElyxOTPNotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public ElyxExceptionResponse elyxOTPNotFoundException(
			ElyxOTPNotFoundException oTPNotFoundException) {
		String errorMessage = oTPNotFoundException.getExceptionMessage();
		ElyxExceptionResponse exceptionResponse = new ElyxExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.FORBIDDEN);
		exceptionResponse.setException(errorMessage);
		return exceptionResponse;
	}
	
	@ExceptionHandler(ElyxBadCredentialsException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ElyxExceptionResponse elyxBadCredentialsException(ElyxBadCredentialsException badCredentialsException){
		String errorMessage = badCredentialsException.getExceptionMessage();
		ElyxExceptionResponse exceptionResponse = new ElyxExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.UNAUTHORIZED);
		exceptionResponse.setException(errorMessage);
		return exceptionResponse;
	}
	
	@ExceptionHandler(ElyxCaseSharingException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.MULTI_STATUS)
	public ElyxExceptionResponse elyxCaseSharingStatus(ElyxCaseSharingException elyxCaseSharingStatus){
		String errorMessage = elyxCaseSharingStatus.getExceptionMessage();
		ElyxExceptionResponse exceptionResponse = new ElyxExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.MULTI_STATUS);
		exceptionResponse.setException(errorMessage);
		return exceptionResponse;
	}
}
