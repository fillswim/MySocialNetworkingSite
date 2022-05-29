package com.fillolej.mysns.application;

import com.fillolej.mysns.application.dtos.CommentDto;
import com.fillolej.mysns.domain.model.Comment;
import com.fillolej.mysns.domain.model.User;

import java.util.List;

public interface CommentService {

    // Метод для сохранения комментария
    public Comment saveComment(Long postId, CommentDto commentDto, User user);

    public Comment updateComment(Long commentId, CommentDto commentDto, User user);

    // Метод будет возвращать все комментарии для поста
    public List<Comment> getAllCommentsForPost(Long postId);

    // Метод для получения конкретного поста
    public Comment getCommentByIdAndUser(Long commentId, User user);

    // Метод для удаления комментария
    public void deleteComment(Long commentId, User user);

}