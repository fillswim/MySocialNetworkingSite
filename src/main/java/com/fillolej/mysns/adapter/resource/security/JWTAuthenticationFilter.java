package com.fillolej.mysns.adapter.resource.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = jwtTokenProvider.getTokenFromRequest(request);

            // Если токен валиден
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

                log.info("IN doFilterInternal() - token isn't null and valid");

                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("IN doFilterInternal() - authentication isn't null and set into context");
                }

            }
        } catch (JwtAuthenticationException exception) {

            log.error("IN doFilterInternal() - Exception! Could not set authentication");

            SecurityContextHolder.clearContext();
            response.sendError(exception.getHttpStatus().value());
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }

        // Если токен валиден, то запрос пропускается через фильтр дальше
        filterChain.doFilter(request, response);
    }

}
