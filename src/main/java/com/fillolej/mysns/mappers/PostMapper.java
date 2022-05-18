package com.fillolej.mysns.mappers;

import com.fillolej.mysns.dtos.PostDto;
import com.fillolej.mysns.models.Post;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PostMapper {

    Post postDtoToPost(PostDto postDto);

    PostDto postToPostDto(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostFromPostDto(PostDto postDto, @MappingTarget Post post);
}
