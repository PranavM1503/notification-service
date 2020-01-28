package com.assignment.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NegativeOrZero;

@Data @NoArgsConstructor @AllArgsConstructor
public class ThirdPartyFailResponse {
    private ThirdPartyResponseDTO response;
}
