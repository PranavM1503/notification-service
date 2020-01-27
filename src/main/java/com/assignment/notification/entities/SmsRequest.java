package com.assignment.notification.entities;

import lombok.Builder;

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
    public SmsRequest(String phoneNumber,UUID requestId, String message, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.phoneNumber = phoneNumber;
        this.requestId = requestId;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFailureCode(int failureCode) {
        this.failureCode = failureCode;
    }

    public void setFailureComments(String failureComments) {
        this.failureComments = failureComments;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
