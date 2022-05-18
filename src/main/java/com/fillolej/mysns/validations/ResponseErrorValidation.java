package com.fillolej.mysns.validations;

import com.fillolej.mysns.exceptions.ResponseErrorsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

// Будет обрабатывать ошибки, которые будут приходить на сервер
// Будет возвращать Map с ошибками, или же ничего не будет возвращать
@Service
@Slf4j
public class ResponseErrorValidation {

    // BindingResult - содержит все ошибки от получаемых запросов
    public void mapValidationService(BindingResult bindingResult) {

        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {

            log.error("IN mapValidationService() - bindingResult has errors!");

            // Если есть ошибки во всём входящем объекте
            if (!CollectionUtils.isEmpty(bindingResult.getAllErrors())) {
                for (ObjectError objectError : bindingResult.getAllErrors()) {
                    errors.put(objectError.getCode(), objectError.getDefaultMessage());
                }
            }

            // и если есть ошибки в полях объекта, приходящего в запросе
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new ResponseErrorsException(errors.toString());
        }
    }
}
