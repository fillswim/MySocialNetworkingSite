package com.fillolej.mysns.adapter.resource.request;

import com.fillolej.mysns.common.annotations.PasswordMatches;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

// Будет использоваться при регистрации нового пользователя
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches // Собственная аннотация, сравнивает присылаемые пароли
@ApiModel(value = "New user registration request")
public class SignupRequest {

    @ApiModelProperty(value = "User email", example = "chris1989@gmail.com")
    @Email(message = "It should have email format",
            regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    @NotBlank(message = "User email is required")
    private String email;

    @ApiModelProperty(value = "First name", example = "Chris")
    @NotEmpty(message = "Please enter your firstname")
    private String firstname;

    @ApiModelProperty(value = "Last name", example = "Scott")
    @NotEmpty(message = "Please enter your lastname")
    private String lastname;

    @ApiModelProperty(value = "Username", example = "chris1989")
    @NotEmpty(message = "Please enter your username")
    private String username;

    @ApiModelProperty(value = "Password for the user account", example = "9RdgRF0p3ZPnArH")
    @NotEmpty(message = "Password is required")
    @Size(min = 6)
    private String password;

    @ApiModelProperty(value = "Confirm the user account password", example = "9RdgRF0p3ZPnArH")
    @NotEmpty(message = "Please confirm your password")
    @Size(min = 6)
    private String confirmPassword;

}
