package com.fillolej.mysns.unit.mappers;

import com.fillolej.mysns.dtos.CommentDto;
import com.fillolej.mysns.mappers.CommentMapperImpl;
import com.fillolej.mysns.models.Comment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentMapperTest {

    private CommentMapperImpl mapper = new CommentMapperImpl();

    @Test
    void commentDtoToComment() {

        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .message("Message")
                .build();

        Comment comment = mapper.commentDtoToComment(commentDto);

        assertEquals(1l, comment.getId());
        assertEquals("Message", comment.getMessage());

    }

    @Test
    void commentToCommentDto() {

        Comment comment = new Comment();

        comment.setId(1L);
        comment.setMessage("Message");

        CommentDto commentDto = mapper.commentToCommentDto(comment);

        assertEquals(1l, commentDto.getId());
        assertEquals("Message", commentDto.getMessage());

    }

    @Test
    void updateCommentFromCommentDto() {

        CommentDto commentDto = CommentDto.builder()
                .id(1l)
                .message("New message")
                .build();

        Comment comment = new Comment();
        comment.setId(1l);
        comment.setMessage("Message");

        mapper.updateCommentFromCommentDto(commentDto, comment);

        assertEquals(1l, comment.getId());
        assertEquals("New message", comment.getMessage());

    }
}