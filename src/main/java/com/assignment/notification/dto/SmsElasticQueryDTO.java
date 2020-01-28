package com.assignment.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SmsElasticQueryDTO {
    private String text;
    private String scrollId;
}
