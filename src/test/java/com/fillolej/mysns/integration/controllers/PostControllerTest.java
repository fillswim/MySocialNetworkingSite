package com.fillolej.mysns.integration.controllers;

import com.fillolej.mysns.dtos.PostDto;
import com.fillolej.mysns.AbstractRestControllerTest;
import com.google.gson.Gson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerTest extends AbstractRestControllerTest {

    private PostDto postDto1 = PostDto.builder()
            .id(1l)
            .title("Title1")
            .caption("Caption1")
            .location("Location1")
            .likes(1)
            .likedUsers(new LinkedHashSet<>(Arrays.asList("user")))
            .build();

    private PostDto postDto2 = PostDto.builder()
            .id(2l)
            .title("Title2")
            .caption("Caption2")
            .location("Location2")
            .likes(1)
            .likedUsers(new LinkedHashSet<>(Arrays.asList("admin")))
            .build();

    private PostDto postDto3 = PostDto.builder()
            .id(3l)
            .title("Title3")
            .caption("Caption3")
            .location("Location3")
            .likes(1)
            .likedUsers(new LinkedHashSet<>(Arrays.asList("admin")))
            .build();

    private PostDto postDto4 = PostDto.builder()
            .id(4l)
            .title("Title4")
            .caption("Caption4")
            .location("Location4")
            .likes(1)
            .likedUsers(new LinkedHashSet<>(Arrays.asList("user")))
            .build();

    @Test
    @Order(31)
    void createPost_200Ok() throws Exception {

        String token = getToken("user", "password");

        PostDto postDtoNew = PostDto.builder()
                .title("Title5")
                .caption("Caption5")
                .location("Location5")
                .build();

        PostDto postDtoSaved = PostDto.builder()
                .id(5l)
                .title("Title5")
                .caption("Caption5")
                .location("Location5")
                .likes(0)
                .likedUsers(new LinkedHashSet<>())
                .build();

        mockMvc.perform(post("/api/posts")
                        .content(new Gson().toJson(postDtoNew))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(postDtoSaved)));
    }

    @Test
    @Order(32)
    void updatePost_200Ok() throws Exception {

        String token = getToken("user", "password");

        Long id = 1L;

        PostDto postDtoNew = PostDto.builder()
                .title("Title1New")
                .caption("Caption1New")
                .build();

        PostDto postDtoUpdated = PostDto.builder()
                .id(1l)
                .title("Title1New")
                .caption("Caption1New")
                .location("Location1")
                .likes(1)
                .likedUsers(new LinkedHashSet<>(Arrays.asList("user")))
                .build();

        mockMvc.perform(put("/api/posts/" + id)
                        .content(new Gson().toJson(postDtoNew))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(postDtoUpdated)));
    }

    @Test
    @Order(33)
    void getAllPosts_200Ok() throws Exception {

        String token = getToken("user", "password");

        List<PostDto> postDtos = new ArrayList<>(Arrays.asList(postDto1, postDto2, postDto3, postDto4));

        mockMvc.perform(get("/api/posts")
                        .header("Authorization", token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(postDtos)));
    }

    @Test
    @Order(34)
    void getPostById_200Ok() throws Exception {

        String token = getToken("user", "password");

        Long id = 1L;

        mockMvc.perform(get("/api/posts/" + id)
                        .header("Authorization", token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(postDto1)));
    }

    @Test
    @Order(35)
    void getAllPostsForUser_200Ok() throws Exception {

        String token = getToken("user", "password");

        List<PostDto> postDtos = new ArrayList<>(Arrays.asList(postDto1, postDto2));

        mockMvc.perform(get("/api/posts/currentuser")
                        .header("Authorization", token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(postDtos)));
    }

    @Test
    @Order(36)
    void likePost_200Ok() throws Exception {

        String token = getToken("admin", "password");

        Long postId = 1L;

        PostDto postDtoLiked = PostDto.builder()
                .id(1l)
                .title("Title1")
                .caption("Caption1")
                .location("Location1")
                .likes(2)
                .likedUsers(new LinkedHashSet<>(Arrays.asList("user", "admin")))
                .build();

        mockMvc.perform(get("/api/posts/" + postId + "/like")
                        .header("Authorization", token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new Gson().toJson(postDtoLiked)));

    }

    @Test
    @Order(38)
    void deletePost_200Ok() throws Exception {

        String token = getToken("admin", "password");;
        Long id = 4L;

        mockMvc.perform(delete("/api/posts/" + id)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());
    }

}