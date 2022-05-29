package com.fillolej.mysns.adapter.resource.mappers;

import com.fillolej.mysns.application.dtos.CommentDto;
import com.fillolej.mysns.domain.model.Comment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CommentMapper {

    Comment commentDtoToComment(CommentDto commentDto);

    CommentDto commentToCommentDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCommentFromCommentDto(CommentDto commentDto, @MappingTarget Comment comment);
}
