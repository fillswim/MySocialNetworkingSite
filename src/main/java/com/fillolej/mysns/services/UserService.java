package com.fillolej.mysns.services;

import com.fillolej.mysns.dtos.UserDto;
import com.fillolej.mysns.models.User;
import com.fillolej.mysns.payloads.request.SignupRequest;
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

