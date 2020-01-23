package com.assignment.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SmsTimeQueryDTO {
    private UUID request_id;
    private String message;
}
