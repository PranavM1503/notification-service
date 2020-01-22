package com.assignment.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor  @Setter @NoArgsConstructor
public class CheckBlackListNumberDTO {
    private String phone_number;

    public String getPhoneNumber(){
        return phone_number;
    }
}
