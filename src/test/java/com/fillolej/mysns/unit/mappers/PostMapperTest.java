package com.fillolej.mysns.unit.mappers;

import com.fillolej.mysns.dtos.PostDto;
import com.fillolej.mysns.mappers.PostMapperImpl;
import com.fillolej.mysns.models.Post;
import com.fillolej.mysns.models.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

class PostMapperTest {

    private final PostMapperImpl mapper = new PostMapperImpl();

    @Test
    void postDtoToPost() {

        PostDto postDto = PostDto.builder()
                .id(1l)
                .title("Title")
                .caption("Caption")
                .location("Location")
                .likes(1)
                .likedUsers(new LinkedHashSet<>(Arrays.asList("User")))
                .build();

        Post post = mapper.postDtoToPost(postDto);

        assertEquals(1l, post.getId());
        assertEquals("Title", post.getTitle());
        assertEquals("Caption", post.getCaption());
        assertEquals("Location", post.getLocation());
        assertEquals(1, post.getLikes());

        assertNotNull(post.getLikedUsers());
        assertEquals(new LinkedHashSet<>(Arrays.asList("User")), post.getLikedUsers());

    }

    @Test
    void postToPostDto() {

        User user = new User();
        Post post = new Post();

        user.setUsername("user");

        post.setId(1L);
        post.setTitle("Title");
        post.setCaption("Caption");
        post.setLocation("Location");
        post.setLikes(1);
        post.setLikedUsers(new LinkedHashSet<>(Arrays.asList("user")));
        post.setUser(user);

        PostDto postDto = mapper.postToPostDto(post);

        assertEquals(1l, postDto.getId());
        assertEquals("Title", postDto.getTitle());
        assertEquals("Caption", postDto.getCaption());
        assertEquals("Location", postDto.getLocation());
        assertEquals(1, postDto.getLikes());

        assertNotNull(postDto.getLikedUsers());
        assertEquals(new LinkedHashSet<>(Arrays.asList("user")), postDto.getLikedUsers());

    }

    @Test
    void updatePostFromPostDto() {

        // Old post
        User user = new User();
        Post post = new Post();

        user.setUsername("user");

        post.setId(1L);
        post.setTitle("Title");
        post.setCaption("Caption");
        post.setLocation("Location");
        post.setLikes(1);
        post.setLikedUsers(new LinkedHashSet<>(Arrays.asList("user")));
        post.setUser(user);


        // New postDto
        PostDto postDto = PostDto.builder()
                .id(1l)
                .title("TitleNew")
                .caption("CaptionNew")
                .build();


        mapper.updatePostFromPostDto(postDto, post);

        assertEquals(1l, post.getId());
        assertEquals("TitleNew", post.getTitle());
        assertEquals("CaptionNew", post.getCaption());
        assertEquals("Location", post.getLocation());
        assertEquals(1, post.getLikes());

        assertNotNull(post.getLikedUsers());
        assertEquals(new LinkedHashSet<>(Arrays.asList("user")), post.getLikedUsers());

    }
}