package com.fillolej.mysns.exception_handlings;

import com.fillolej.mysns.exceptions.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class AppGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorAttributes errorAttributes;

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> appException(ApplicationException exception, WebRequest request) {

        log.error("Application Exception", exception);

        Map<String, Object> body = errorAttributes.getErrorAttributes(request, exception.getOptions());
        HttpStatus status = exception.getStatus();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());

        return ResponseEntity.status(status).body(body);
    }

    // Обработчик для @PreAuthorize
    @ExceptionHandler()
    public ResponseEntity<String> appException(AccessDeniedException exception) {

        log.error("AccessDeniedException has been thrown");

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

}
