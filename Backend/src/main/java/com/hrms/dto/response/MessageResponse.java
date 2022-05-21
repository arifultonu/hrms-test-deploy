package com.hrms.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageResponse {

    private String message;
    private Boolean status;
    private Object data;


    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String message, Boolean status) {
        this.message = message;
        this.status = status;
    }

    public MessageResponse(String message, Boolean status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
}
