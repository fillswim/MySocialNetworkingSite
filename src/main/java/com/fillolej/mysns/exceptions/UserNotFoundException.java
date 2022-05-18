package com.fillolej.mysns.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApplicationException{

    public UserNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}
