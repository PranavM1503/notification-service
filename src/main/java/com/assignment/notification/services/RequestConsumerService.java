package com.assignment.notification.services;

import com.assignment.notification.entities.SmsApiModel;
import com.assignment.notification.entities.SmsRequest;
import com.assignment.notification.repositories.smsRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;

import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import org.springframework.http.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;
@Service
public class RequestConsumerService {
    private final Logger logger = LoggerFactory.getLogger(Producer.class);
    private smsRequestRepository smsRequestRepository;
    @Autowired
    public RequestConsumerService(smsRequestRepository smsRequestRepository){
        this.smsRequestRepository = smsRequestRepository;
    }

    Jedis jedis = new Jedis();

    @KafkaListener(topics = "smsRequest", groupId = "group_id")

    public void consume(String smsId) throws IOException {

        logger.info(String.format("#### -> Consumed message -> %s ", smsId));

        int id = Integer.parseInt(smsId);
        SmsRequest smsRequest = smsRequestRepository.findById(id).get();

        String phoneNumber = smsRequest.getPhoneNumber();
        logger.info(String.format("Phone Number is : %s", phoneNumber));
        boolean isBlackListed = jedis.sismember("phone_number", phoneNumber);

        if(isBlackListed == true){
            logger.info("Blacklisted number");
        }
//        else{
//            logger.info("Not Blacklisted");
//        }
//
        else{
            String apiUrl = "https://api.imiconnect.in/resources/v1/messaging";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("key", <enter key>);

            List <String> msisdn = new ArrayList<>();
            msisdn.add(smsRequest.getPhoneNumber());


            SmsApiModel smsApiModel = new SmsApiModel(smsRequest.getMessage(), msisdn, smsRequest.getRequestId());

//   ********************         json conversion to verify the post request           *******************
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(smsApiModel);
                logger.info("\n"  + json);
            } catch (IOException e) {
                e.printStackTrace();
            }
//   ******************************************************************************************************
            HttpEntity<SmsApiModel> httpRequest = new HttpEntity<>(smsApiModel, headers);
            logger.info(String.format(httpRequest.toString()));
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpRequest, String.class);

            logger.info(String.format(response.toString()));
            if (response.getStatusCode() == HttpStatus.CREATED) {
                logger.info("Request Successful");
                logger.info(response.getBody());
            }else{
//                logger.info("Request Failed");
                logger.info(String.format(response.getStatusCode().toString()));
            }

        }
    }
}
