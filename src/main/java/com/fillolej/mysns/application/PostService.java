package com.fillolej.mysns.application;

import com.fillolej.mysns.application.dtos.PostDto;
import com.fillolej.mysns.domain.model.Post;
import com.fillolej.mysns.domain.model.User;

import java.util.List;

public interface PostService {

    // Создание поста
    public Post createPost(PostDto postDto, User user);

    // Обновление поста
    public Post updatePost(Long postId, PostDto postDto, User user);

    // Возвращение всех постов из БД
    public List<Post> getAllPosts();

    // Нахождение поста по id поста и текущему пользователю
    public Post getPostByIdAndUser(Long postId, User user);

    // Находит пост по Id
    public Post getPostById(Long postId);

    // Возвращение всех постов для текущего пользователя
    public List<Post> getAllPostsForUser(User user);

    // Добавление или удаление лайка к посту
    public Post likePost(Long postId, User user);

    // Удаление поста по Id и пользователю
    public void deletePost(Long postId, User user);

}
