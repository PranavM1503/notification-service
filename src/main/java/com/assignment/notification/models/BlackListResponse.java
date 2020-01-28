package com.assignment.notification.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder @Data @NoArgsConstructor @AllArgsConstructor
public class BlackListResponse {
    private String status;
}
