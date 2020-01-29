package com.assignment.notification.controllers;


import com.assignment.notification.dto.ElasticQueryForSMSDTO;
import com.assignment.notification.dto.SmsElasticQueryDTO;
import com.assignment.notification.dto.SmsTimeQueryDTO;
import com.assignment.notification.dto.SmsTimeQueryRequestDTO;
import com.assignment.notification.models.ElasticQueryScrollResponse;
import com.assignment.notification.models.ElasticQueryScrollResponse2;
import com.assignment.notification.services.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/search/")
public class ElasticSearchQueryController {
    public static final Logger logger = LoggerFactory.getLogger(ElasticSearchQueryController.class);

    private ElasticSearchService elasticSearchService;

    @Autowired
    public ElasticSearchQueryController(ElasticSearchService elasticSearchService){
        this.elasticSearchService = elasticSearchService;
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ElasticQueryScrollResponse> getAllSmsWithGivenText(@RequestBody SmsElasticQueryDTO smsElasticQueryDTO) throws IOException {

        logger.info("Message : " + smsElasticQueryDTO.getText());
        logger.info("Scroll Id : " + smsElasticQueryDTO.getScrollId());
        ElasticQueryScrollResponse requiredDetails = elasticSearchService.getAllSmsWithGivenText(smsElasticQueryDTO);

        return new ResponseEntity<>(requiredDetails, HttpStatus.OK);
    }


    @RequestMapping(value = "/find-message", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getSmsBetweenGivenTime(@RequestBody SmsTimeQueryRequestDTO smsTimeQueryRequestDTO) throws IOException{

        logger.info("Phone : " + smsTimeQueryRequestDTO.getPhone_number());
        logger.info("Start Time : " + smsTimeQueryRequestDTO.getStartDateTime());
        logger.info("End Time : " + smsTimeQueryRequestDTO.getEndDateTime());
        logger.info("Scroll Id : " + smsTimeQueryRequestDTO.getScrollId());

        ElasticQueryScrollResponse2 requiredDetails = elasticSearchService.getSmsBetweenGivenTime(smsTimeQueryRequestDTO);

        return new ResponseEntity<>(requiredDetails, HttpStatus.OK);
    }



}
