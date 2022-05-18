package com.fillolej.mysns.services.impl;

import com.fillolej.mysns.dtos.PostDto;
import com.fillolej.mysns.exceptions.PostNotFoundException;
import com.fillolej.mysns.mappers.PostMapper;
import com.fillolej.mysns.models.Image;
import com.fillolej.mysns.models.Post;
import com.fillolej.mysns.models.User;
import com.fillolej.mysns.repositories.ImageRepository;
import com.fillolej.mysns.repositories.PostRepository;
import com.fillolej.mysns.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository,
                           ImageRepository imageRepository,
                           PostMapper postMapper) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.postMapper = postMapper;
    }


    // Создание поста
    @Override
    public Post createPost(PostDto postDto, User user) {

        Post post = new Post();

        post.setUser(user);
        post.setCaption(postDto.getCaption());
        post.setLocation(postDto.getLocation());
        post.setTitle(postDto.getTitle());
        post.setLikes(0);

        log.info("Saving Post for User: {}", user.getEmail());
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Long postId, PostDto postDto, User user) {

        Post post = getPostByIdAndUser(postId, user);
        postMapper.updatePostFromPostDto(postDto, post);

        return postRepository.save(post);
    }


    // Возвращение всех постов из БД
    @Override
    public List<Post> getAllPosts() {

        return postRepository.findByOrderByCreatedDateDesc();
    }

    // Нахождение поста по id поста и текущему пользователю
    // Principal необходим для того определения принадлежности поста данному пользователю
    @Override
    public Post getPostByIdAndUser(Long postId, User user) {

        Post post = postRepository.findByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username " + user.getEmail()));

        return post;
    }

    // Находит пост по Id
    @Override
    public Post getPostById(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for postId " + postId));

        return post;
    }

    // Возвращение всех постов для текущего пользователя
    @Override
    public List<Post> getAllPostsForUser(User user) {

        List<Post> posts = postRepository.findByUserOrderByCreatedDateDesc(user);

        return posts;
    }

    // Добавление или удаление лайка к посту
    @Override
    public Post likePost(Long postId, User user) {

        String username = user.getUsername();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for postId " + postId));

        // Когда пользователь будет лайкать пост, то посту будет заноситься username пользователя
        // Метод создает контейнер с именем текущего пользователя, которого там может и не быть, если пользователь не лайкал пост
        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(userLike -> userLike.equals(username))
                .findAny();

        // Если имя пользователя все-таки найдено, то
        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);         // тогда пользователь забирает свой лайк
            post.getLikedUsers().remove(username);      // и имя пользователя убирается из множества лайкнувших пост
        } else {
            post.setLikes(post.getLikes() + 1);         // в противном случае пользователь лайкает пост
            post.getLikedUsers().add(username);         // и добавляем пользователя в список тех, кто лайкнул пост
        }

        return postRepository.save(post);
    }

    // Метод для удаления поста
    // Principal нужен для того, чтобы определить, принадлежит ли пост пользователю, который пытается его удалить
    @Override
    public void deletePost(Long postId, User user) {

        // Находится пост по id поста и пользователю, что говорит о том, что пост принадлежит пользователю
        Post post = getPostByIdAndUser(postId, user);

        // Есть ли фотография у данного поста?
        Optional<Image> imageOptional = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);

        // Если у поста есть фотография, то удалить ее
        imageOptional.ifPresent(imageRepository::delete);
    }

}
