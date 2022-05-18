package com.fillolej.mysns.services.impl;

import com.fillolej.mysns.exceptions.ImageNotFoundException;
import com.fillolej.mysns.models.Image;
import com.fillolej.mysns.models.Post;
import com.fillolej.mysns.models.User;
import com.fillolej.mysns.repositories.ImageRepository;
import com.fillolej.mysns.services.ImageService;
import com.fillolej.mysns.services.PostService;
import com.fillolej.mysns.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Transactional
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,
                            UserService userService,
                            PostService postService) {
        this.imageRepository = imageRepository;
        this.userService = userService;
        this.postService = postService;
    }

    // Метод для загрузки фотографии для пользователя
    @Override
    public void uploadImageToUser(MultipartFile file,
                                  User user) throws IOException {

        // Проверка на то, имеется ли уже у пользователя фотография
        Image userImage = imageRepository.findByUser(user)
                .orElse(null);

        if (!ObjectUtils.isEmpty(userImage)) {
            // если фотография имеется, то она удаляется
            imageRepository.delete(userImage);
        }

        // Создаем новая фотография для профиля пользователя
        Image image = new Image();
        image.setUser(user);

        // Перед загрузкой фотографии в БД, компрессируем ее
//        image.setImageBytes(compressBytes(file.getBytes()));
        image.setImageBytes(file.getBytes());
        image.setName(file.getOriginalFilename());

        log.info("Uploading image to User {}", user.getUsername());

        imageRepository.save(image);
    }

    // Метод для загрузки фотографии для поста
    @Override
    public void uploadImageToPost(MultipartFile file,
                                  User user,
                                  Long postId) throws IOException {

        Post post = postService.getPostByIdAndUser(postId, user);

        Image image = new Image();

        image.setPost(post);
//        image.setImageBytes(compressBytes(file.getBytes()));
        image.setImageBytes(file.getBytes());
        image.setName(file.getOriginalFilename());

        log.info("Uploading image to Post {}", post.getId());

        imageRepository.save(image);
    }

    // Метод, который будет возвращать фотографию для пользователя
    @Override
    public Image getImageToUser(User user) {

        Image image = imageRepository.findByUser(user)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to User: " + user.getUsername()));

        // Если фотография не пустая
        if (!ObjectUtils.isEmpty(image)) {
            // тогда нужно вернуть фотографию из БД
            image.setImageBytes(image.getImageBytes());
        }

        return image;
    }

    // Метод для возвращения фотографии для поста
    @Override
    public Image getImageToPost(Long postId) {

        Image image = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Post with id: " + postId));

        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(image.getImageBytes());
        }

        return image;
    }

}
