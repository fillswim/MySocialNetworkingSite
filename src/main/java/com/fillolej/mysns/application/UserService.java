package com.fillolej.mysns.application;

import com.fillolej.mysns.application.dtos.UserDto;
import com.fillolej.mysns.domain.model.User;
import com.fillolej.mysns.adapter.resource.request.SignupRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    public User createUser(SignupRequest signupRequest);

    public User getUserById(Long userId);

    public List<User> findAllUsers();

    public User updateUser(UserDto userDto, User user);

    public void deleteUser(User user);

    public void deleteUser(Long userId);

}

