package com.assignment.notification.models;

import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class BlackListNumberDisplay {
    List<String> phone_numbers;
}
