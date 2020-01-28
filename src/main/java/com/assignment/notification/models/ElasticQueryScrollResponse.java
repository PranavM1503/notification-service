package com.assignment.notification.models;


import com.assignment.notification.dto.ElasticQueryForSMSDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class ElasticQueryScrollResponse {
    List<ElasticQueryForSMSDTO> query;
    String scrollId;
}
