package com.assignment.notification.dto;



import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;


@Builder
public class SmsDetailDTO {

    private String phoneNumber;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SmsDetailDTO (String phoneNumber, String message, LocalDateTime createdAt, LocalDateTime updatedAt){
       this.phoneNumber = phoneNumber;
       this.message = message;
       this.createdAt = createdAt;
       this.updatedAt = updatedAt;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
