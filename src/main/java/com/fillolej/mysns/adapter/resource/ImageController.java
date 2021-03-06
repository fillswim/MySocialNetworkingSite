package com.fillolej.mysns.adapter.resource;

import com.fillolej.mysns.adapter.resource.response.MessageResponse;
import com.fillolej.mysns.application.impl.ImageService;
import com.fillolej.mysns.domain.model.Image;
import com.fillolej.mysns.domain.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@Api(tags = "Image Controller")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // Метод для загрузки фотографии для пользователя
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Upload image to user")
    @PreAuthorize("hasAnyAuthority('users:write')")
    public ResponseEntity<Object> uploadImageToUser(
            @Parameter(
                    description = "Files to be uploaded",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestPart(value = "file") MultipartFile file,
            @ApiIgnore @AuthenticationPrincipal User user) throws IOException {

        imageService.uploadImageToUser(file, user);

        String message = "Image to user has been upload successfully";
        return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
    }

    // Метод загрузки фотографии для поста
    @PostMapping(value = "/post/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Upload image to post")
    @PreAuthorize("hasAnyAuthority('users:write')")
    public ResponseEntity<Object> uploadImageToPost(
            @Parameter(
                    description = "Files to be uploaded",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestPart(value = "file") MultipartFile file,
            @PathVariable("postId") Long postId,
            @ApiIgnore @AuthenticationPrincipal User user) throws IOException {

        imageService.uploadImageToPost(file, user, postId);

        String message = "Image to post has been upload successfully";
        return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
    }


    // Метод будет возвращать фотографию для пользователя, который зашел на страницу профайла
    @GetMapping(value = "/profile", produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperation(value = "Get the user image")
    @PreAuthorize("hasAnyAuthority('users:read')")
    public ResponseEntity<byte[]> getImageToUser(@ApiIgnore @AuthenticationPrincipal User user) {

        Image image = imageService.getImageToUser(user);

        byte[] imageBytes = image.getImageBytes();

        ResponseEntity<byte[]> responseEntity = ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageBytes);

        return responseEntity;
    }


    // Метод для получения фотографии для поста
    @GetMapping(value = "/post/{postId}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ApiOperation(value = "Get the post image")
    @PreAuthorize("hasAnyAuthority('users:read')")
    public ResponseEntity<byte[]> getImageToPost(@PathVariable("postId") Long postId) {

        Image image = imageService.getImageToPost(postId);

        byte[] imageBytes = image.getImageBytes();

        ResponseEntity<byte[]> responseEntity = ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);

        return responseEntity;
    }

}
