package com.assignment.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ElasticQueryForSMSDTO {
    private UUID request_id;
    private String phone_number;
    private String message;
    private String created_at;
    private String updated_at;
}
