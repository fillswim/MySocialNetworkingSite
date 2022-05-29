package com.fillolej.mysns.adapter.resource.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Тело ответа сервера на успешную попытку аутентификации
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Successful login response")
public class JWTTokenSuccessResponse {

    @ApiModelProperty(value = "User ID", notes = "The auto-generated id")
    private boolean success;

    @ApiModelProperty(value = "JWT token", notes = "The auto-generated JWT token", example = "Bearer eyJhbGci...")
    private String token;

}
