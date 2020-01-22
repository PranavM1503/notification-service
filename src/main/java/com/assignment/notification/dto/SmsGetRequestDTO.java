package com.assignment.notification.dto;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsGetRequestDTO {

    private String phoneNumber;
    private UUID requestId;
    private String message;
    private String status;
    private int failureCode;
    private String failureComments;
    private String createdAt;
    private String updatedAt;

}
