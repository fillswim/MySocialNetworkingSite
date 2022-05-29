package com.fillolej.mysns.integration.controllers;

import com.fillolej.mysns.AbstractRestControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class ImageControllerTest extends AbstractRestControllerTest {

    @BeforeEach
    void uploadImageToUserOne() throws Exception {

        String token = getToken("user", "password");

        Resource fileResource = new ClassPathResource("images/User.png");

        assertNotNull(fileResource);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", fileResource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE, fileResource.getInputStream());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images/upload")
                        .file(mockMultipartFile)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Image to user has been upload successfully")));
    }

    @BeforeEach
    void uploadImageToPostOne() throws Exception {

        String token = getToken("user", "password");

        Resource fileResource = new ClassPathResource("images/Post1.jpg");

        assertNotNull(fileResource);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", fileResource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE, fileResource.getInputStream());

        Long postId = 1l;

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images/post/" + postId)
                        .file(mockMultipartFile)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Image to post has been upload successfully")));
    }

    @Test
    @Order(51)
    void uploadImageToUser_200Ok() throws Exception {

        String token = getToken("admin", "password");

        Resource fileResource = new ClassPathResource("images/Admin.png");

        assertNotNull(fileResource);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", fileResource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE, fileResource.getInputStream());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images/upload")
                        .file(mockMultipartFile)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Image to user has been upload successfully")));
    }

    @Test
    @Order(52)
    void uploadImageToPost_200Ok() throws Exception {

        String token = getToken("user", "password");

        Resource fileResource = new ClassPathResource("images/Post2.jpg");

        assertNotNull(fileResource);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file", fileResource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE, fileResource.getInputStream());

        Long postId = 2l;

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/images/post/" + postId)
                        .file(mockMultipartFile)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Image to post has been upload successfully")));
    }

    @Test
    @Order(53)
    void getImageToUser_200Ok() throws Exception {

        String token = getToken("user", "password");

        Resource fileResource = new ClassPathResource("images/User.png");

        mockMvc.perform(get("/api/images/profile")
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileResource.getInputStream().readAllBytes()));
    }

    @Test
    @Order(54)
    void getImageToPost_200Ok() throws Exception {

        String token = getToken("user", "password");

        Resource fileResource = new ClassPathResource("images/Post1.jpg");

        Long postId = 1l;

        mockMvc.perform(get("/api/images/post/" + postId)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isOk())
                .andExpect(content().bytes(fileResource.getInputStream().readAllBytes()));
    }
}