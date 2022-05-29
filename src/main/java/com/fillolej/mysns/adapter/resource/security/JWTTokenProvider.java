package com.fillolej.mysns.adapter.resource.security;

import com.fillolej.mysns.application.impl.UserService;
import com.fillolej.mysns.domain.model.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

// Класс будет создавать токен
@Component
@Slf4j
public class JWTTokenProvider {

    @Value("${jwt.expiration-time}")
    private Long validityMilliseconds;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    private final UserService userService;

    @Autowired
    public JWTTokenProvider(UserService userService) {
        this.userService = userService;
    }

    // Шифрование секретного ключа
    @PostConstruct
    protected void init() {
        secretKey = Base64
                .getEncoder()
                .encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Получение токена из HttpServletRequest запроса (HttpServletRequest => token)
    public String getTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader(header);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {

            String token = bearerToken.split(" ")[1];
            return token;
        }

        return null;
    }

    // Создание токена (Authentication => token)
    public String generateToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        // Время истечения токена
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + validityMilliseconds);

        // claimsMap будет передаваться в токен
        Claims claims = Jwts
                .claims()
                .setSubject(user.getUsername());

        claims.put("firstname", user.getFirstname());
        claims.put("lastname", user.getLastname());

        String token = Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        return tokenPrefix + token;
    }

    // Проверка на валидность токена (token => boolean)
    public boolean validateToken(String token) {

        try {

            Jws<Claims> claimsJws = Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            // true (валиден), если дата окончания действия токена после чем дата сейчас
            Date expiration = claimsJws
                    .getBody()
                    .getExpiration();

            // Если время окончания токена после даты на данный момент
            if (expiration.after(new Date())) {
                log.info("IN validateToken() - token is valid");
                return true;
            } else {
                log.info("IN validateToken() - token isn't valid");
                return false;
            }

        } catch (JwtException | IllegalArgumentException exception) {

            log.warn("IN validateToken() - token isn't valid, {}", exception.getMessage());
            throw  new JwtAuthenticationException("Jwt token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    // Получение userId из токена (token => userId)
    public String getUsernameFromToken(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        log.info("IN getUsernameFromToken() - username: {}", username);

        return username;
    }

    // Метод получения аутентификации из токена
    public Authentication getAuthentication(String token) {

        // Получается username из токена
        String username = getUsernameFromToken(token);

        // По username получается User из БД
        User user = (User) userService.loadUserByUsername(username);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());

        if (authentication != null) {
            log.info("IN getAuthentication() - authentication has been got");
        } else {
            log.info("IN getAuthentication() - authentication hasn't been got");
        }

        return authentication;
    }
}
