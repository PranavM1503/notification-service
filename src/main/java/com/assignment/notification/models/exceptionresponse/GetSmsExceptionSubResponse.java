package com.assignment.notification.models.exceptionresponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class GetSmsExceptionSubResponse {

    public String code;
    private String message;

    public GetSmsExceptionSubResponse(String message){
        this.code = "INVALID_REQUEST";
        this.message = message;
    }
}
