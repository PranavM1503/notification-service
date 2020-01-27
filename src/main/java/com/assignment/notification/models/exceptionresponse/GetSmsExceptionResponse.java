package com.assignment.notification.models.exceptionresponse;

public class GetSmsExceptionResponse {

    private GetSmsExceptionSubResponse error;

    public GetSmsExceptionResponse(String message){
        GetSmsExceptionSubResponse getSmsExceptionSubResponse = new GetSmsExceptionSubResponse(message);
        this.error = getSmsExceptionSubResponse;
    }
}
