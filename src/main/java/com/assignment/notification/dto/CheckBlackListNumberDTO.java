package com.assignment.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class CheckBlackListNumberDTO {
    private String phone_number;

    public CheckBlackListNumberDTO(String phone_number){
        this.phone_number = phone_number;
    }
    public String getPhoneNumber(){
        return phone_number;
    }
}
