package com.assignment.notification.transformers;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsDetailTransformerDTO {

    @NotNull @Size(min = 13, max = 13) @JsonProperty("phone_number")
    private String phoneNumber;
    @NotNull
    private String message;
}
