package com.fillolej.mysns.adapter.resource;

import com.fillolej.mysns.adapter.resource.request.LoginRequest;
import com.fillolej.mysns.adapter.resource.request.SignupRequest;
import com.fillolej.mysns.adapter.resource.response.JWTTokenSuccessResponse;
import com.fillolej.mysns.adapter.resource.response.MessageResponse;
import com.fillolej.mysns.adapter.resource.security.JWTTokenProvider;
import com.fillolej.mysns.application.impl.UserService;
import com.fillolej.mysns.common.validation.ResponseErrorValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
@Api(tags = "Authentication Controller")
@Slf4j
public class AuthController {

//    @Value("${jwt.token-prefix}")
//    private String tokenPrefix;

    private JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ResponseErrorValidation responseErrorValidation; // Обработчик ошибок во входящем запросе
    private final UserService userService;

    @Autowired
    public AuthController(JWTTokenProvider jwtTokenProvider,
                          AuthenticationManager authenticationManager,
                          ResponseErrorValidation responseErrorValidation,
                          UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.responseErrorValidation = responseErrorValidation;
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ApiOperation(value = "User registration")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest,
                                            BindingResult bindingResult) {

        // Если есть ошибки во входящем запросе, то возвращаются ошибки
        responseErrorValidation.mapValidationService(bindingResult);

        userService.createUser(signupRequest);

        // Отправляется ответ об успешном сохранении нового пользователя
        String message = "User has been registered successfully!";
        return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
    }

    // Метод аутентификации на сайте
    @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "User authentication")
    public ResponseEntity<JWTTokenSuccessResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                                                    BindingResult bindingResult) {

        // Если есть ошибки во входящем запросе, то возвращаются ошибки
        responseErrorValidation.mapValidationService(bindingResult);

        String email = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Получение аутентификации
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        // Аутентификация сохраняется в SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Генерируется токен
        String token = jwtTokenProvider.generateToken(authentication);

        return new ResponseEntity<>(new JWTTokenSuccessResponse(true, token), HttpStatus.OK);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "User logout")
    public void logoutUser(HttpServletRequest request, HttpServletResponse response) {

        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
