package com.assignment.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class SmsTimeQueryRequestDTO {
    private String phone_number;
    private String startDateTime;
    private String endDateTime;
    private String scrollId;

    public String getPhone_number() {
        return phone_number;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }
}
