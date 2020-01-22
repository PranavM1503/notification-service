package com.assignment.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlackListNumberDTO {
    private List <String> phone_number;

    public List<String> getBlackListNumber(){
        return  phone_number;
    }


}
