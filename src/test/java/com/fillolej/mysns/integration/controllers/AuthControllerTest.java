package com.fillolej.mysns.integration.controllers;

import com.fillolej.mysns.AbstractRestControllerTest;
import com.fillolej.mysns.adapter.resource.request.LoginRequest;
import com.fillolej.mysns.adapter.resource.request.SignupRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest extends AbstractRestControllerTest {

    @Test
    @Order(11)
    void registerUser_200Ok() throws Exception {

        SignupRequest signupRequest = SignupRequest.builder()
                .username("testUser")
                .firstname("TestUserFirstname")
                .lastname("TestUserLastname")
                .email("testuser@mail.ru")
                .password("password")
                .confirmPassword("password")
                .build();

        String signupRequestJson = new Gson().toJson(signupRequest);

        mockMvc.perform(post("/api/auth/signup")
                        .content(signupRequestJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User has been registered successfully!")));
    }

    @Test
    @Order(12)
    void authenticateUser_200Ok() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .username("user")
                .password("password")
                .build();

        String loginRequestJson = new Gson().toJson(loginRequest);

        mockMvc.perform(post("/api/auth/signin")
                        .content(loginRequestJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.token", containsString("Bearer ")));
    }

    @Test
    @Order(13)
    void authenticateUser_403Forbidden_badUsername() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .username("badUser")
                .password("password")
                .build();

        String loginRequestJson = new Gson().toJson(loginRequest);

        mockMvc.perform(post("/api/auth/signin")
                        .content(loginRequestJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.username", is("Invalid Username")))
                .andExpect(jsonPath("$.password", is("Invalid password")));
    }

    @Test
    @Order(14)
    void authenticateUser_403Forbidden_badPassword() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .username("user")
                .password("badpassword")
                .build();

        String loginRequestJson = new Gson().toJson(loginRequest);

        mockMvc.perform(post("/api/auth/signin")
                        .content(loginRequestJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.username", is("Invalid Username")))
                .andExpect(jsonPath("$.password", is("Invalid password")));
    }

}