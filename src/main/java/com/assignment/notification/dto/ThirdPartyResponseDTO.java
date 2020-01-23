package com.assignment.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ThirdPartyResponseDTO {

    private String code;
    private String transid;
    private String description;
    private String correlationid;
}
