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
@ApiModel(value = "User DTO")
public class UserDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(
            value = "User ID", notes = "The auto-generated id",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Long id;

    @ApiModelProperty(value = "Username", example = "chris1989")
    private String username;

    @ApiModelProperty(value = "First name", example = "Chris")
    @NotEmpty(message = "Firstname cannot be empty")
    private String firstname;

    @ApiModelProperty(value = "Last name", example = "Scott")
    @NotEmpty(message = "Lastname cannot be empty")
    private String lastname;

    @ApiModelProperty(value = "Biography", example = "I am Chris...")
    private String biography;
}
