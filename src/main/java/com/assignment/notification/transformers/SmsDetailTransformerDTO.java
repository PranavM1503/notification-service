package com.assignment.notification.transformers;


import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsDetailTransformerDTO {

    @NotNull @Size(min = 13, max = 13)
    private String phoneNumber;
    @NotNull
    private String message;
}
