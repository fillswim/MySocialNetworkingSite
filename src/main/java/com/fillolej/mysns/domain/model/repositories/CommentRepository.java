package com.fillolej.mysns.domain.model.repositories;

import com.fillolej.mysns.domain.model.Comment;
import com.fillolej.mysns.domain.model.Post;
import com.fillolej.mysns.domain.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    // Найти все комментарии для поста
    List<Comment> findByPost(Post post);

    // Найти по Id и пользователю
    Optional<Comment> findByIdAndUser(Long commentId, User user);

}