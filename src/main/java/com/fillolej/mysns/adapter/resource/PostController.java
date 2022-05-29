package com.fillolej.mysns.adapter.resource;


import com.fillolej.mysns.application.dtos.PostDto;
import com.fillolej.mysns.adapter.resource.mappers.PostMapper;
import com.fillolej.mysns.domain.model.Post;
import com.fillolej.mysns.domain.model.User;
import com.fillolej.mysns.application.PostService;
import com.fillolej.mysns.common.validation.ResponseErrorValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin
@Api(tags = "Post Controller", description = "Actions when working with the posts")
@Slf4j
public class PostController {

    private final PostMapper postMapper;
    private final PostService postService;
    private final ResponseErrorValidation responseErrorValidation;

    public PostController(PostMapper postMapper,
                          PostService postService,
                          ResponseErrorValidation responseErrorValidation) {
        this.postMapper = postMapper;
        this.postService = postService;
        this.responseErrorValidation = responseErrorValidation;
    }

    // Метод для создания поста
    @PostMapping("")
    @ApiOperation(value = "Create new post")
    @PreAuthorize("hasAnyAuthority('users:write')")
    public PostDto createPost(@Valid @RequestBody PostDto postDto,
                              BindingResult bindingResult,
                              @ApiIgnore @AuthenticationPrincipal User user) {

        // Если есть ошибки в полученном запросе
        responseErrorValidation.mapValidationService(bindingResult);

        // если их нет, то создается пост
        Post post = postService.createPost(postDto, user);
        PostDto createdPost = postMapper.postToPostDto(post);

        return createdPost;
    }

    @PutMapping("/{postId}")
    @ApiOperation(value = "Update post")
    @PreAuthorize("hasAnyAuthority('users:read')")
    public PostDto updatePost(@Valid @RequestBody PostDto postDto,
                              BindingResult bindingResult,
                              @PathVariable("postId") Long postId,
                              @ApiIgnore @AuthenticationPrincipal User user) {

        // Если есть ошибки в полученном запросе
        responseErrorValidation.mapValidationService(bindingResult);

        // в ином же случае пост обновляется
        Post postUpdated = postService.updatePost(postId, postDto, user);

        return postMapper.postToPostDto(postUpdated);
    }

    // Метод для возвращения всех постов
    @GetMapping("")
    @ApiOperation(value = "Get all posts")
    @PreAuthorize("hasAnyAuthority('users:read')")
    public List<PostDto> getAllPosts() {

        List<PostDto> postDtos = postService.getAllPosts()
                .stream()
                .map(postMapper::postToPostDto)
                .collect(Collectors.toList());

        return postDtos;
    }

    // Метод для получения поста по Id
    @GetMapping("/{postId}")
    @ApiOperation(value = "Get post by Id")
    @PreAuthorize("hasAnyAuthority('users:read')")
    public PostDto getPostById(@PathVariable("postId") Long postId) {

        Post post = postService.getPostById(postId);

        return postMapper.postToPostDto(post);
    }

    // Метод, который будет возвращать посты для конкретного пользователя
    @GetMapping("/currentuser")
    @ApiOperation(value = "Get all posts by user token")
    @PreAuthorize("hasAnyAuthority('users:read')")
    public List<PostDto> getAllPostsForUser(@ApiIgnore @AuthenticationPrincipal User user) {

        List<PostDto> postDtos = postService.getAllPostsForUser(user)
                .stream()
                .map(postMapper::postToPostDto)
                .collect(Collectors.toList());

        return postDtos;
    }

    // Метод, который будет позволять лайкать пост
    @GetMapping("/{postId}/like")
    @ApiOperation(value = "Like the post")
    @PreAuthorize("hasAnyAuthority('users:write')")
    public PostDto likePost(@PathVariable("postId") Long postId,
                            @ApiIgnore @AuthenticationPrincipal User user) {

        Post post = postService.likePost(postId, user);

        return postMapper.postToPostDto(post);
    }

    // Метод для удаления поста
    @DeleteMapping("/{postId}")
    @ApiOperation(value = "Delete a post by id")
    @PreAuthorize("hasAnyAuthority('users:write')")
    public String deletePost(@PathVariable("postId") Long postId,
                             @ApiIgnore @AuthenticationPrincipal User user) {

        postService.deletePost(postId, user);

        return "Post was deleted";
    }

}
