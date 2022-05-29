package com.fillolej.mysns.integration.controllers;

import com.fillolej.mysns.application.dtos.CommentDto;
import com.fillolej.mysns.AbstractRestControllerTest;
import com.google.gson.Gson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest extends AbstractRestControllerTest {

    CommentDto commentDto1 = CommentDto.builder()
            .id(1l)
            .message("Comment Text 1")
            .build();

    CommentDto commentDto2 = CommentDto.builder()
            .id(2l)
            .message("Comment Text 2")
            .build();

    CommentDto commentDto3 = CommentDto.builder()
            .id(3l)
            .message("Comment Text 3")
            .build();

    CommentDto commentDto4 = CommentDto.builder()
            .id(4l)
            .message("Comment Text 4")
            .build();

    @Test
    @Order(41)
    void createComment_200Ok() throws Exception {

        String token = getToken("user", "password");

        CommentDto commentDtoNew = CommentDto.builder()
                .message("Comment Text New")
                .build();

        Long postId = 1L;

        CommentDto commentDtoCreated = CommentDto.builder()
                .id(5l)
                .message("Comment Text New")
                .build();

        mockMvc.perform(post("/api/comments/" + postId)
                        .content(new Gson().toJson(commentDtoNew))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(commentDtoCreated)));

    }

    @Test
    @Order(42)
    void getAllCommentToPost_200Ok() throws Exception {

        List<CommentDto> commentDtos = new ArrayList<>(Arrays.asList(commentDto1, commentDto2));

        String token = getToken("user", "password");

        Long postId = 1L;

        mockMvc.perform(get("/api/comments/post/" + postId)
                        .header("Authorization", token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(commentDtos)));

    }

    @Test
    @Order(43)
    void getCommentForUserAndId_200Ok() throws Exception {

        String token = getToken("user", "password");

        Long commentId = 4L;

        mockMvc.perform(get("/api/comments/" + commentId)
                        .header("Authorization", token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(commentDto4)));
    }

    @Test
    @Order(44)
    void updateComment_200Ok() throws Exception {

        String token = getToken("user", "password");

        Long commentId = 1L;

        CommentDto commentDtoNew = CommentDto.builder()
                .message("New Comment Text")
                .build();

        mockMvc.perform(put("/api/comments/" + commentId)
                        .content(new Gson().toJson(commentDtoNew))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(commentDtoNew)));

    }

    @Test
    @Order(46)
    void deleteComment_200Ok() throws Exception {

        String token = getToken("user", "password");;
        Long commentId = 4L;

        mockMvc.perform(delete("/api/comments/" + commentId)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Comment was deleted")));
    }
}