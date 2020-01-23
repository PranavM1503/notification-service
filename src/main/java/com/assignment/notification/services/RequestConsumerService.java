package com.assignment.notification.services;

import com.assignment.notification.dto.SmsDetailsForElasticSearch;
import com.assignment.notification.entities.SmsRequest;
import com.assignment.notification.repositories.smsRequestRepository;
import org.apache.kafka.clients.producer.Producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


import java.io.IOException;

@Service
public class RequestConsumerService {
    private final Logger logger = LoggerFactory.getLogger(Producer.class);
    private smsRequestRepository smsRequestRepository;
    private ElasticSearchService elasticSearchService;
    @Autowired
    public RequestConsumerService(smsRequestRepository smsRequestRepository, ElasticSearchService elasticSearchService){
        this.smsRequestRepository = smsRequestRepository;
        this.elasticSearchService = elasticSearchService;
    }

    Jedis jedis = new Jedis();

    @KafkaListener(topics = "smsRequest", groupId = "group_id")

    public void consume(String smsId) throws IOException {

        logger.info(String.format("#### -> Consumed message -> %s ", smsId));

        int id = Integer.parseInt(smsId);

//        if(smsRequestRepository.existsById(smsId)){
        SmsRequest smsRequest = smsRequestRepository.findById(id).get();

        String phoneNumber = smsRequest.getPhoneNumber();
        logger.info(String.format("Phone Number is : %s", phoneNumber));
        boolean isBlackListed = jedis.sismember("phone_number", phoneNumber);

        //dto for elastic search service
        SmsDetailsForElasticSearch smsDetailsForElasticSearch = new SmsDetailsForElasticSearch(smsRequest.getId(), smsRequest.getRequestId(),
                smsRequest.getPhoneNumber(), smsRequest.getMessage(), smsRequest.getCreatedAt(), smsRequest.getUpdatedAt());

//        elasticSearchService.indexSmsData(smsDetailsForElasticSearch);


        if(isBlackListed == true){
            logger.info("Blacklisted number");
        }
        else{
            logger.info("Not Blacklisted \n");
            elasticSearchService.indexSmsData(smsDetailsForElasticSearch);
            elasticSearchService.getSmsDatabyId(Integer.toString(smsDetailsForElasticSearch.getId()));
        }
////
//        else{
//            String apiUrl = "https://api.imiconnect.in/resources/v1/messaging";
//
//            RestTemplate restTemplate = new RestTemplate();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Content-Type", "application/json");
//            headers.set("key", "7b73f76d-369e-11ea-9e4e-025282c394f2");
//
//            List <String> msisdn = new ArrayList<>();
//            msisdn.add(smsRequest.getPhoneNumber());
//
//
//            SmsApiModel smsApiModel = new SmsApiModel(smsRequest.getMessage(), msisdn, smsRequest.getRequestId());
//
////   ********************         json conversion to verify the post request           *******************
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            try {
//                String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(smsApiModel);
//                logger.info("\n"  + json);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////   ******************************************************************************************************
//            HttpEntity<SmsApiModel> httpRequest = new HttpEntity<>(smsApiModel, headers);
//            logger.info(String.format(httpRequest.toString()));
//            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpRequest, String.class);
//
//            logger.info(String.format(response.toString()));
//            if (response.getStatusCode() == HttpStatus.CREATED) {
//                logger.info("Request Successful");
//                logger.info(response.getBody());
//            }else{
////                logger.info("Request Failed");
//                logger.info(String.format(response.getStatusCode().toString()));
//            }
//
//        }
    }
}
