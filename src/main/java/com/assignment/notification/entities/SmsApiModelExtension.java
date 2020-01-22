package com.assignment.notification.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter @Setter
@NoArgsConstructor
public class SmsApiModelExtension {
    List<String> msisdn;
    private UUID correlationid;

    public SmsApiModelExtension(List<String> msisdn, UUID correlationid){
        this.msisdn = msisdn;
        this.correlationid = correlationid;
    }
}
