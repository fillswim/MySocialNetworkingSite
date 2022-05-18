package com.fillolej.mysns.unit.config;

import com.fillolej.mysns.models.Role;
import com.fillolej.mysns.models.Status;
import com.fillolej.mysns.models.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@TestConfiguration
public class SpringSecurityConfigTest {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {

        User user = new User();

        user.setId(1L);
        user.setUsername("admin");
        user.setFirstname("AdminFirstname");
        user.setLastname("AdminLastname");
        user.setEmail("admin@mail.ru");
        user.setBiography(null);
        user.setPassword("$2a$10$r5tZGzprBSg0nZOwQ3Gj8.n9BtWbHCQgaC603vODTV7iXEpSGSCsu");
        user.setRole(Role.ROLE_ADMIN);
        user.setStatus(Status.ACTIVE);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setAuthorities(getAuthoritiesFromRole(Role.ROLE_ADMIN));

        return new InMemoryUserDetailsManager(Arrays.asList(user));
    }

    private Set<SimpleGrantedAuthority> getAuthoritiesFromRole(Role role) {

        Set<SimpleGrantedAuthority> authorities = role.getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        return authorities;
    }

}
