package com.fillolej.mysns.application.impl;

import com.fillolej.mysns.application.dtos.CommentDto;
import com.fillolej.mysns.domain.model.exceptions.CommentNotFoundException;
import com.fillolej.mysns.adapter.resource.mappers.CommentMapper;
import com.fillolej.mysns.domain.model.Comment;
import com.fillolej.mysns.domain.model.Post;
import com.fillolej.mysns.domain.model.User;
import com.fillolej.mysns.domain.model.repositories.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          PostService postService,
                          UserService userService,
                          CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
        this.commentMapper = commentMapper;
    }

    // Метод для сохранения комментария
    public Comment saveComment(Long postId,
                               CommentDto commentDto,
                               User user) {

        Post post = postService.getPostById(postId);

        // Создание комментария
        Comment comment = new Comment();

        comment.setPost(post);
        comment.setUser(user);
        comment.setMessage(commentDto.getMessage());

        log.info("Saving comment for Post : {}", post.getId());
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, CommentDto commentDto, User user) {

        Comment comment = getCommentByIdAndUser(commentId, user);

        commentMapper.updateCommentFromCommentDto(commentDto, comment);

        return commentRepository.save(comment);
    }

    // Метод будет возвращать все комментарии для поста
    public List<Comment> getAllCommentsForPost(Long postId) {

        Post post = postService.getPostById(postId);

        List<Comment> comments = commentRepository.findByPost(post);

        return comments;
    }

    public Comment getCommentByIdAndUser(Long commentId, User user) {

        Comment comment = commentRepository.findByIdAndUser(commentId, user)
                .orElseThrow(() -> new CommentNotFoundException(
                        "Comment cannot be found for username: " + user.getUsername() + " and Id: " + commentId));

        return comment;
    }

    // Метод для удаления комментария
    public void deleteComment(Long commentId, User user) {

        Comment comment = getCommentByIdAndUser(commentId, user);

        commentRepository.delete(comment);

    }

}
