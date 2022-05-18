package com.fillolej.mysns.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class ResponseErrorsException extends ApplicationException {
    public ResponseErrorsException(String message) {
        super(HttpStatus.BAD_REQUEST, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}


