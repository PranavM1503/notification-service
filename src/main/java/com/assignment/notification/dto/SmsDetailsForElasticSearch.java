package com.assignment.notification.dto;

import lombok.*;

import java.util.UUID;

@Builder
 @NoArgsConstructor @AllArgsConstructor
public class SmsDetailsForElasticSearch {
    private int id;
    private UUID requestId;
    private String phoneNumber;
    private String message;
    private String createdAt;
    private String updatedAt;

     public int getId() {
         return id;
     }

     public UUID getRequestId() {
         return requestId;
     }

     public String getPhoneNumber() {
         return phoneNumber;
     }

     public String getMessage() {
         return message;
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

     public void setCreatedAt(String createdAt) {
         this.createdAt = createdAt;
     }

     public void setUpdatedAt(String updatedAt) {
         this.updatedAt = updatedAt;
     }
 }
