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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Comment DTO")
public class CommentDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(
            value = "Comment ID", notes = "The auto-generated id",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Long id;

    @ApiModelProperty(value = "Comment text", example = "I liked it very much!")
    @NotEmpty(message = "Message cannot be empty")
    private String message;
}
