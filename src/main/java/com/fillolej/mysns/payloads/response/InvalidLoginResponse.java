package com.fillolej.mysns.payloads.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {

    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid Username";
        this.password = "Invalid password";
    }
}
