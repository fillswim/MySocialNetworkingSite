package com.fillolej.mysns.controllers;

import com.fillolej.mysns.dtos.UserDto;
import com.fillolej.mysns.mappers.UserMapper;
import com.fillolej.mysns.models.User;
import com.fillolej.mysns.services.UserService;
import com.fillolej.mysns.validations.ResponseErrorValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin // Чтобы не было ошибки при вызове с локального сервера
@Api(tags = "User Controller", description = "Actions when working with the users")
@Slf4j
public class UserController {

    // 400 Bad Request  - сервер не смог понять запрос из-за недействительного синтаксиса
    // 401 Unauthorized - (ошибка аутентификации)   - указанные имя пользователя и пароль не верны
    // 403 Forbidden    - (ошибка авторизации)      - у пользователя не хватает прав доступа к запрашиваемому ресурсу
    // 404 Not Found    - сервер не может найти запрошенный ресурс

    private final UserService userService;
    private final UserMapper userMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public UserController(UserService userService,
                          UserMapper userMapper,
                          ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    // Возврат всех пользователей
    @GetMapping("")
    @ApiOperation(value = "Get all users")
    @PreAuthorize("hasAnyAuthority('admins:read')")
    public List<UserDto> getAllUsers() {

        List<User> users = userService.findAllUsers();

        List<UserDto> userDtos = users.stream()
                .map(user -> userMapper.userToUserDto(user))
                .collect(Collectors.toList());

        return userDtos;
    }

    // Возврат текущего пользователя
    @GetMapping("/current")
    @ApiOperation(value = "Get current user by token")
    @PreAuthorize("hasAnyAuthority('users:read')")
    public UserDto getCurrentUser(@ApiIgnore @AuthenticationPrincipal User user) {

        log.info("IN getCurrentUser() - user: {}", user.toString());

        return userMapper.userToUserDto(user);
    }

    // Метод, возвращающий пользователя по id
    @GetMapping("/{userId}")
    @ApiOperation(value = "Get user by Id")
    @PreAuthorize("hasAnyAuthority('admins:read')")
    public UserDto getUser(@PathVariable("userId") Long userId) {

        User user = userService.getUserById(userId);

        return userMapper.userToUserDto(user);
    }

    // Метод, который будет обновлять пользователя
    @PutMapping("")
    @ApiOperation(value = "Update user")
    @PreAuthorize("hasAnyAuthority('users:write')")
    public UserDto updateUser(@Valid @RequestBody UserDto userDto,
                              BindingResult bindingResult,
                              @ApiIgnore @AuthenticationPrincipal User user) {

        // Если есть ошибки в полученном запросе
        responseErrorValidation.mapValidationService(bindingResult);

        // в ином же случае пользователь обновляется
        User userUpdated = userService.updateUser(userDto, user);

        return userMapper.userToUserDto(userUpdated);
    }

    // Метод для удаления своего аккаунта
    @DeleteMapping("/current")
    @ApiOperation(value = "Delete current user by token")
    @PreAuthorize("hasAnyAuthority('users:write')")
    public String deleteCurrentUser(@ApiIgnore @AuthenticationPrincipal User user) {

        userService.deleteUser(user);

        return "Current user was deleted";
    }

    // Метод для удаления пользователя по Id
    @DeleteMapping("/{userId}")
    @ApiOperation(value = "Delete user by Id")
    @PreAuthorize("hasAnyAuthority('admins:write')")
    public String deleteUser(@PathVariable("userId") Long userId) {

        userService.deleteUser(userId);

        return "User with id = " + userId + " has been deleted";
    }
}
