package com.ytf.storageserver.exception;

public record ErrorResponse(String message) {
	public static ErrorResponse from(final RuntimeException e) {
		return new ErrorResponse(e.getMessage());
	}
}
