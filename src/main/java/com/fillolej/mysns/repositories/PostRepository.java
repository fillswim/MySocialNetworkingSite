package com.fillolej.mysns.repositories;

import com.fillolej.mysns.models.Post;
import com.fillolej.mysns.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Поиск всех постов с сортировкой по убыванию
    List<Post> findByOrderByCreatedDateDesc();

    // Поиск постов пользователя с сортировкой в порядке убывания
    List<Post> findByUserOrderByCreatedDateDesc(User user);

    // Поиск поста по пользователю и postId
    Optional<Post> findByIdAndUser(Long postId, User user);

}