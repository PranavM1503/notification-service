package com.assignment.notification.entities;

import javax.persistence.*;
import java.time.*;
import java.util.UUID;


@Entity
@Table(name = "smsDetails")
public class SmsRequest {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private UUID requestId;
    private String phoneNumber;
    private String message;
    private String status;
    private int failureCode;
    private String failureComments;
    private String createdAt;
    private String updatedAt;

    public SmsRequest(){}
    public SmsRequest(String phoneNumber, String message, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.phoneNumber = phoneNumber;
        this.requestId = UUID.randomUUID();
        this.message = message;
        this.status = "Success";
        this.failureCode = 101;
        this.failureComments = "No Comment";
        this.createdAt = createdAt.toString();
        this.updatedAt = updatedAt.toString();
    }

    public UUID getRequestId() {
        return requestId;
    }
//    public String getRequestId() {
//        return requestId;
//    }

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public int getFailureCode() {
        return failureCode;
    }

    public String getFailureComments() {
        return failureComments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

}
