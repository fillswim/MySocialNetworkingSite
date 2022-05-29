package com.fillolej.mysns.adapter.resource.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

// Объект будет передаваться на сервер, когда будет попытка авторизации на веб-сайте
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Login request")
public class LoginRequest {

    @ApiModelProperty(value = "Username", example = "chris1989")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @ApiModelProperty(value = "Password for the user account", example = "9RdgRF0p3ZPnArH")
    @NotEmpty(message = "Password cannot be empty")
    private String password;

}
