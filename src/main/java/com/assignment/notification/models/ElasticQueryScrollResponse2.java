package com.assignment.notification.models;

import com.assignment.notification.dto.SmsTimeQueryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class ElasticQueryScrollResponse2 {
    private List<SmsTimeQueryDTO> query;
    private String scrollId;
}
