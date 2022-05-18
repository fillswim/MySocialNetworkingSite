package com.fillolej.mysns.unit.mappers;

import com.fillolej.mysns.dtos.UserDto;
import com.fillolej.mysns.mappers.UserMapperImpl;
import com.fillolej.mysns.models.Role;
import com.fillolej.mysns.models.Status;
import com.fillolej.mysns.models.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserMapperTest {

    private final UserMapperImpl mapper = new UserMapperImpl();

    @Test
    @Order(1)
    void userDtoToUser() {

        User user = new User();

        user.setId(1L);
        user.setUsername("username");
        user.setFirstname("Firstname");
        user.setLastname("Lastname");
        user.setBiography("Biography");

        UserDto userDto = mapper.userToUserDto(user);

        assertEquals(1l, userDto.getId());
        assertEquals("username", userDto.getUsername());
        assertEquals("Firstname", userDto.getFirstname());
        assertEquals("Lastname", userDto.getLastname());
        assertEquals("Biography", userDto.getBiography());

    }

    @Test
    @Order(2)
    void userToUserDto() {

        UserDto userDto = UserDto.builder()
                .id(1l)
                .username("username")
                .firstname("Firstname")
                .lastname("Lastname")
                .biography("Biography")
                .build();

        User user = mapper.userDtoToUser(userDto);

        assertEquals(1l, user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("Firstname", user.getFirstname());
        assertEquals("Lastname", user.getLastname());
        assertEquals("Biography", user.getBiography());

    }

    @Test
    @Order(3)
    void updateUserFromUserDto() {

        User user = new User();
        user.setId(1l);
        user.setUsername("user");
        user.setFirstname("UserFirstname");
        user.setLastname("UserLastname");
        user.setEmail("user@mail.ru");
        user.setBiography(null);
        user.setPassword("password");
        user.setRole(Role.ROLE_USER);
        user.setStatus(Status.ACTIVE);

        UserDto userDto = UserDto.builder()
                .id(1l)
                .username("user")
                .firstname("UserFirstnameNew")
                .lastname("UserLastnameNew")
                .biography("Biography")
                .build();

        mapper.updateUserFromUserDto(userDto, user);

        assertEquals(1l, user.getId());
        assertEquals("user", user.getUsername());
        assertEquals("UserFirstnameNew", user.getFirstname());
        assertEquals("UserLastnameNew", user.getLastname());
        assertEquals("user@mail.ru", user.getEmail());
        assertEquals("Biography", user.getBiography());
        assertEquals("password", user.getPassword());
        assertEquals("ROLE_USER", user.getRole().toString());
        assertEquals("ACTIVE", user.getStatus().toString());

    }
}