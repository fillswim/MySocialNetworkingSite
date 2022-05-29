package com.fillolej.mysns.domain.model.exceptions;

import com.fillolej.mysns.common.exception.ApplicationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class UserExistException extends ApplicationException {
    public UserExistException(String message) {
        super(HttpStatus.BAD_REQUEST, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}
