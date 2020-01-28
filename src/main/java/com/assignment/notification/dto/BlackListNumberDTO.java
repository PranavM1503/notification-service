package com.assignment.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlackListNumberDTO {
    @Size(min = 1)
    private List <String> phone_number;

    public List<String> getBlackListNumber(){
        return  phone_number;
    }


}
