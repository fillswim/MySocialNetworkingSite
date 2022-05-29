package com.fillolej.mysns.integration.controllers;

import com.fillolej.mysns.AbstractRestControllerTest;
import com.fillolej.mysns.application.dtos.UserDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractRestControllerTest {

    // Not Authenticate (401 Unauthorized)
    @Test
    @Order(21)
    void getAllUsers_401Unauthorized() throws Exception {

        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // Not enough rights (403 Forbidden)
    @Test
    @Order(22)
    void getAllUsers_403Forbidden() throws Exception {

        String token = getToken("user", "password");

        mockMvc.perform(get("/api/users")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$", is("Access is denied")));
    }

    // Ok
    @Test
    @Order(23)
    void getAllUsers_200Ok() throws Exception {

        String token = getToken("admin", "password");

        mockMvc.perform(get("/api/users")
                        .header("Authorization", token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(users)));
    }

    @Test
    @Order(24)
    void getCurrentUser_200Ok() throws Exception {

        String token = getToken("user", "password");

        mockMvc.perform(get("/api/users/current")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(userDto)));
    }

    @Test
    @Order(25)
    void getUser_200Ok() throws Exception {

        String token = getToken("admin", "password");

        Long id = 1L;

        mockMvc.perform(get("/api/users/" + id)
                        .header("Authorization", token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(userDto)));
    }

    @Test
    @Order(26)
    void updateUser_200Ok() throws Exception {

        String token = getToken("admin", "password");

        UserDto userDtoNew = UserDto.builder()
                .firstname("UserNewFirstname")
                .lastname("UserNewLastname")
                .biography("NewBiography")
                .build();

        String userDtoNewJson = new Gson().toJson(userDtoNew);

        mockMvc.perform(put("/api/users")
                        .content(userDtoNewJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(userDtoNewJson));
    }

    @Test
    @Order(27)
    void deleteCurrentUser_200Ok() throws Exception {

        String token = getToken("user", "password");

        mockMvc.perform(delete("/api/users/current")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(28)
    void deleteUser_200Ok() throws Exception {

        String token = getToken("admin", "password");;
        Long id = 2L;

        mockMvc.perform(delete("/api/users/" + id)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());
    }

}