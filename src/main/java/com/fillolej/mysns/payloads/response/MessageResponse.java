package com.fillolej.mysns.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Data;

// Тело ответа сервера
@Data
@AllArgsConstructor
public class MessageResponse {

    private String message;

}
