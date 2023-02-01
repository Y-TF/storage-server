package com.ytf.storageserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
		return ResponseEntity.badRequest()
			.body(ErrorResponse.from(e));
	}

	@ExceptionHandler(ImageFileNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleImageFileNotFoundException(final ImageFileNotFoundException e) {
		log.warn(e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(ErrorResponse.from(e));
	}
}
