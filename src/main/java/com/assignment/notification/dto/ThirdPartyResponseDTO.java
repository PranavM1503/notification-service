package com.assignment.notification.dto;


import lombok.*;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ThirdPartyResponseDTO {

    private String code;
    private String transid;
    private String description;
    private String correlationid;
}
