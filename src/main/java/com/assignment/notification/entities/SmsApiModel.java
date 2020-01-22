package com.assignment.notification.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter @Setter
@NoArgsConstructor
public class SmsApiModel {
    private String deliverychannel;
    Map<String, Map<String , Object>> channels = new HashMap<>();
    List<Object> destination = new ArrayList<>();

    public SmsApiModel(String message, List<String> msisdn, UUID correlationid){
        this.deliverychannel = "sms";
        this.channels.put("sms", new HashMap<String, Object>(){{put("text", message);}});
        SmsApiModelExtension smsApiModelExtension = new SmsApiModelExtension(msisdn, correlationid);
        this.destination.add(smsApiModelExtension);
    }


}
