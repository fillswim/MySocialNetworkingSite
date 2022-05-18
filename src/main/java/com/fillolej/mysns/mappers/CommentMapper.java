package com.fillolej.mysns.mappers;

import com.fillolej.mysns.dtos.CommentDto;
import com.fillolej.mysns.models.Comment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CommentMapper {

    Comment commentDtoToComment(CommentDto commentDto);

    CommentDto commentToCommentDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCommentFromCommentDto(CommentDto commentDto, @MappingTarget Comment comment);
}
