package com.fillolej.mysns.services;

import com.fillolej.mysns.models.Image;
import com.fillolej.mysns.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    // Метод для загрузки фотографии для пользователя
    public void uploadImageToUser(MultipartFile file,
                                  User user) throws IOException;

    // Метод для загрузки фотографии для поста
    public void uploadImageToPost(MultipartFile file,
                                  User user,
                                  Long postId) throws IOException;

    // Метод, который возвращает фотографию пользователя
    public Image getImageToUser(User user);

    // Метод, который возвращает фотографию для поста
    public Image getImageToPost(Long postId);

}
