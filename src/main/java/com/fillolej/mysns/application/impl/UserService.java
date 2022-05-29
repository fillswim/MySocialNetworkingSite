package com.fillolej.mysns.application.impl;

import com.fillolej.mysns.application.dtos.UserDto;
import com.fillolej.mysns.domain.model.exceptions.UserExistException;
import com.fillolej.mysns.adapter.resource.mappers.UserMapper;
import com.fillolej.mysns.domain.model.Role;
import com.fillolej.mysns.domain.model.Status;
import com.fillolej.mysns.domain.model.User;
import com.fillolej.mysns.adapter.resource.request.SignupRequest;
import com.fillolej.mysns.domain.model.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // Загрузка пользователя из БД по имени, но в нашем случае по email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = getByUsername(username);

        Set<SimpleGrantedAuthority> authorities = getAuthoritiesFromUser(user);
        Status status = user.getStatus();

        user.setAccountNonExpired(status.equals(Status.ACTIVE));
        user.setAccountNonLocked(status.equals(Status.ACTIVE));
        user.setCredentialsNonExpired(status.equals(Status.ACTIVE));
        user.setEnabled(status.equals(Status.ACTIVE));

        // Добавление SimpleGrantedAuthority User-у
        user.setAuthorities(authorities);

        return user;
    }

    // Конвертирует Permissions в SimpleGrantedAuthorities (User => SimpleGrantedAuthorities)
    private Set<SimpleGrantedAuthority> getAuthoritiesFromUser(User user) {

        Set<SimpleGrantedAuthority> authorities = user.getRole().getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        return authorities;
    }

    private User getByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " hasn't been found"));

        return user;
    }

    // Создание пользователя из входящего запроса
    public User createUser(SignupRequest request) {

        User user = new User();

        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER); // Все пользователя при регистрации получают роль - User
        user.setStatus(Status.ACTIVE); // Все пользователя при регистрации получают статус - ACTIVE

        // Проверка на наличие пользователя в БД
        Optional<User> byUsername = userRepository.findByUsername(request.getUsername());
        Optional<User> byEmail = userRepository.findByEmail(request.getEmail());

        // UserExistException - собственное исключение, если пользователь уже имеется в базе
        if (byUsername.isPresent() || byEmail.isPresent()) {
            throw new UserExistException("User with username: " + request.getUsername() + " or email: " +
                    request.getEmail() + " already exists in the database");
        }

        return userRepository.save(user);
    }

    // Получение пользователя по Id
    public User getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + userId + " hasn't been found"));

        return user;
    }

    // Найти все пользователей
    public List<User> findAllUsers() {

        List<User> users = userRepository.findAll();

        return users;
    }

    // Обновление данных пользователя
    public User updateUser(UserDto userDto, User user) {

        userMapper.updateUserFromUserDto(userDto, user);

        return userRepository.save(user);
    }

    public void deleteUser(User user) {

        userRepository.delete(user);
    }

    public void deleteUser(Long userId) {

        userRepository.deleteById(userId);
    }
}
