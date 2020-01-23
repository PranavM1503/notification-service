package com.assignment.notification.controllers;


import com.assignment.notification.dto.ElasticQueryForSMSDTO;
import com.assignment.notification.services.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/message/{message}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ElasticQueryForSMSDTO>> getAllSmsWithGivenText(@PathVariable("message") String message) throws IOException {

        logger.info("Message : " + message);
        List<ElasticQueryForSMSDTO> requiredDetails = new ArrayList<>();
        requiredDetails = elasticSearchService.getAllSmsWithGivenText(message);

        return new ResponseEntity<>(requiredDetails, HttpStatus.OK);
    }
}
