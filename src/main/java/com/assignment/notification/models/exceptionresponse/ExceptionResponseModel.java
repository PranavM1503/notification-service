package com.assignment.notification.models.exceptionresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ExceptionResponseModel {
    private String error;
    private String details;
}
