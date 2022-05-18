package com.fillolej.mysns.unit.controllers;

import com.fillolej.mysns.MySocialNetworkingSiteApplication;
import com.fillolej.mysns.dtos.UserDto;
import com.fillolej.mysns.AbstractRestControllerTest;
import com.fillolej.mysns.unit.config.SpringSecurityConfigTest;
import com.fillolej.mysns.models.Role;
import com.fillolej.mysns.models.Status;
import com.fillolej.mysns.models.User;
import com.fillolej.mysns.repositories.UserRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SpringSecurityConfigTest.class, MySocialNetworkingSiteApplication.class}
)
@AutoConfigureMockMvc
public class UserControllerTest extends AbstractRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        User admin = new User();

        admin.setId(1L);
        admin.setUsername("admin");
        admin.setFirstname("AdminFirstname");
        admin.setLastname("AdminLastname");
        admin.setEmail("admin@mail.ru");
        admin.setBiography(null);
        admin.setPassword("$2a$10$r5tZGzprBSg0nZOwQ3Gj8.n9BtWbHCQgaC603vODTV7iXEpSGSCsu");
        admin.setRole(Role.ROLE_ADMIN);
        admin.setStatus(Status.ACTIVE);
        admin.setAccountNonExpired(true);
        admin.setAccountNonLocked(true);
        admin.setCredentialsNonExpired(true);
        admin.setEnabled(true);
        admin.setAuthorities(getAuthoritiesFromRole(Role.ROLE_ADMIN));


        User user1 = new User();

        user1.setId(1L);
        user1.setUsername("userMock");
        user1.setFirstname("userMockFirstname");
        user1.setLastname("userMockLastname");
        user1.setEmail("userMock@mail.ru");
        user1.setBiography(null);

        given(userRepository.findByUsername("admin")).willReturn(Optional.of(admin));
        given(userRepository.findById(1l)).willReturn(Optional.of(user1));

    }

    private Set<SimpleGrantedAuthority> getAuthoritiesFromRole(Role role) {

        Set<SimpleGrantedAuthority> authorities = role.getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        return authorities;
    }

    @Test
    @WithUserDetails("admin")
    void getUser() throws Exception {

        UserDto userDto = UserDto.builder()
                .id(1l)
                .username("userMock")
                .firstname("userMockFirstname")
                .lastname("userMockLastname")
                .biography(null)
                .build();

        mockMvc.perform(get("/api/users/" + 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(userDto)));
    }
}
