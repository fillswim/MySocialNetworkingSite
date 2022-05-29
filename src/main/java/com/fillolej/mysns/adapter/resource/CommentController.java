package com.fillolej.mysns.adapter.resource;

import com.fillolej.mysns.adapter.resource.response.MessageResponse;
import com.fillolej.mysns.application.dtos.CommentDto;
import com.fillolej.mysns.adapter.resource.mappers.CommentMapper;
import com.fillolej.mysns.application.impl.CommentService;
import com.fillolej.mysns.domain.model.Comment;
import com.fillolej.mysns.domain.model.User;
import com.fillolej.mysns.common.validation.ResponseErrorValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@Api(tags = "Comment Controller")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CommentController(CommentService commentService,
                             CommentMapper commentMapper,
                             ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    // Метод для сохранения комментария в базу данных
    @PostMapping("/{postId}")
    @ApiOperation(value = "Create new comment")
    @PreAuthorize("hasAnyAuthority('users:write')")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    BindingResult bindingResult,
                                    @PathVariable("postId") Long postId,
                                    @ApiIgnore @AuthenticationPrincipal User user) {

        // Проверка на ошибки
        responseErrorValidation.mapValidationService(bindingResult);

        // Если ошибок нет, то создается комментарий
        Comment comment = commentService.saveComment(postId, commentDto, user);
        CommentDto createdCommentDto = commentMapper.commentToCommentDto(comment);

        return createdCommentDto;
    }

    // Метод, который будет возвращать все комментарии к посту
    @GetMapping("/post/{postId}")
    @ApiOperation(value = "Get all comments by post id")
    @PreAuthorize("hasAnyAuthority('users:read')")
    public List<CommentDto> getAllCommentToPost(@PathVariable("postId") Long postId) {

        List<CommentDto> commentDtos = commentService.getAllCommentsForPost(postId)
                .stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());

        return commentDtos;
    }

    // Метод для получения комментария по пользователю и посту
    @GetMapping("/{commentId}")
    @ApiOperation(value = "Get comment by comment id")
    @PreAuthorize("hasAnyAuthority('users:read')")
    public CommentDto getCommentForUserAndId(@PathVariable("commentId") Long commentId,
                                             @ApiIgnore @AuthenticationPrincipal User user) {

        Comment comment = commentService.getCommentByIdAndUser(commentId, user);

        return commentMapper.commentToCommentDto(comment);
    }

    @PutMapping("/{commentId}")
    @ApiOperation(value = "Update comment")
    @PreAuthorize("hasAnyAuthority('users:write')")
    public CommentDto updateComment(@Valid @RequestBody CommentDto commentDto,
                                    BindingResult bindingResult,
                                    @PathVariable("commentId") Long commentId,
                                    @ApiIgnore @AuthenticationPrincipal User user) {

        // Проверка на ошибки
        responseErrorValidation.mapValidationService(bindingResult);

        // Обновление комментария
        Comment comment = commentService.updateComment(commentId, commentDto, user);

        return commentMapper.commentToCommentDto(comment);
    }

    // Метод будет удалять комментарий
    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "Delete comment")
    @PreAuthorize("hasAnyAuthority('users:write')")
    public ResponseEntity<Object> deleteComment(@PathVariable("commentId") Long commentId,
                                @ApiIgnore @AuthenticationPrincipal User user) {

        commentService.deleteComment(commentId, user);

        String message = "Comment was deleted";
        return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
    }

}
