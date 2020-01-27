package com.assignment.notification.models.exceptionresponse;

import lombok.*;

import java.util.UUID;


@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SendSmsValidationResponse {
    private UUID request_id;
    private String comments;
    private String code;
    private String message;
}
