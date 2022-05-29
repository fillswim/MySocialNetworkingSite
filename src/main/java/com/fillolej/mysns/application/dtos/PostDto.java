package com.fillolej.mysns.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Post DTO")
public class PostDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(
            value = "Post ID", notes = "The auto-generated id",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Long id;

    @ApiModelProperty(value = "Title of the post", example = "My summer story")
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @ApiModelProperty(value = "Caption of the post", example = "It was in the summer...")
    @NotEmpty(message = "Caption cannot be empty")
    private String caption;

    @ApiModelProperty(value = "Location of the post", example = "Moscow")
    private String location;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(value = "The number of likes of the post",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Integer likes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(
            value = "Users who liked the post", example = "tomtom2005",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Set<String> likedUsers;

}
