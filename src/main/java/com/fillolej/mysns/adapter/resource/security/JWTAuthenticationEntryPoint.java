package com.fillolej.mysns.adapter.resource.security;

import com.fillolej.mysns.adapter.resource.response.InvalidLoginResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${jwt.context-type}")
    private String contextType;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Создается объект для ответа на неудачную попытку аутентификации
        InvalidLoginResponse invalidLoginResponse = new InvalidLoginResponse();

        // Из объекта неудачной аутентификации создается json
        String jsonInvalidLoginResponse = new Gson().toJson(invalidLoginResponse);

        // Создается HttpServletResponse
        response.setContentType(contextType);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().println(jsonInvalidLoginResponse);
    }
}
