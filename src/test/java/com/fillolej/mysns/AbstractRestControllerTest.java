package com.fillolej.mysns;

import com.fillolej.mysns.application.dtos.UserDto;
import com.fillolej.mysns.integration.initializer.Postgres;
import com.fillolej.mysns.adapter.resource.request.LoginRequest;
import com.fillolej.mysns.adapter.resource.response.JWTTokenSuccessResponse;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Sql("/sql/data.sql")
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = {
        Postgres.Initializer.class
})
public class AbstractRestControllerTest {

    // Start Testcontainer
    @BeforeAll
    static void startContainer() {
        Postgres.container.start();
    }

    @Autowired
    protected MockMvc mockMvc;

    protected UserDto userDto = UserDto.builder()
            .id(1L)
            .username("user")
            .firstname("UserFirstname")
            .lastname("UserLastname")
            .biography(null)
            .build();

    protected UserDto adminDto = UserDto.builder()
            .id(2L)
            .username("admin")
            .firstname("AdminFirstname")
            .lastname("AdminLastname")
            .biography(null)
            .build();

    protected List<UserDto> users = new ArrayList<>(Arrays.asList(userDto, adminDto));


    protected String getToken(String username, String password) throws Exception {

        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        String loginRequestJson = new Gson().toJson(loginRequest);

        ResultActions result = mockMvc.perform(post("/api/auth/signin")
                .content(loginRequestJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        MockHttpServletResponse response = result.andReturn().getResponse();

        JWTTokenSuccessResponse tokenResponse =
                new Gson().fromJson(response.getContentAsString(), JWTTokenSuccessResponse.class);

        String token = tokenResponse.getToken();

        return token;
    }

}
