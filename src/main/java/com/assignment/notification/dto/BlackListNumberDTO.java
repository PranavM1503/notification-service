package com.assignment.notification.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlackListNumberDTO {
    @Size(min = 1) @JsonProperty("phone_number")
    private List <String> phoneNumber;

    public List<String> getBlackListNumber(){
        return  phoneNumber;
    }


}
