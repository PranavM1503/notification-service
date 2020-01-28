package com.assignment.notification.dto;

import lombok.*;
import org.hibernate.annotations.GeneratorType;

@Getter @Setter

public class CheckBlackListNumberDTO {
    private String phone_number;

    public CheckBlackListNumberDTO(){
    }

    public CheckBlackListNumberDTO(String phone_number){
        this.phone_number = phone_number;
    }
    public String getPhoneNumber(){
        return phone_number;
    }

    @Override
    public String toString() {
        return "CheckBlackListNumberDTO{" +
                "phone_number='" + phone_number + '\'' +
                '}';
    }
}
