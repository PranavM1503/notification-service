package com.assignment.notification.services;

import com.assignment.notification.dto.SmsDetailsForElasticSearch;
import com.assignment.notification.dto.ThirdPartyFailResponse;
import com.assignment.notification.dto.ThirdPartyResponseDTO;
import com.assignment.notification.entities.SmsRequest;
import com.assignment.notification.exceptions.AlreadyBlackListNumberException;
import com.assignment.notification.repositories.smsRequestRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


import java.io.IOException;
import java.time.LocalDateTime;


@Service
public class RequestConsumerService {
    private final Logger logger = LoggerFactory.getLogger(RequestConsumerService.class);

    private final smsRequestRepository smsRequestRepository;
    private final ElasticSearchService elasticSearchService;
    private final ThirdPartyServiceForSms thirdPartyServiceForSms;

    @Autowired
    public RequestConsumerService(smsRequestRepository smsRequestRepository, ElasticSearchService elasticSearchService, ThirdPartyServiceForSms thirdPartyServiceForSms){
        this.smsRequestRepository = smsRequestRepository;
        this.elasticSearchService = elasticSearchService;
        this.thirdPartyServiceForSms = thirdPartyServiceForSms;
    }

    Jedis jedis = new Jedis();

    @KafkaListener(topics = "smsRequest", groupId = "group_id")

    public void consume(String smsId) throws IOException{

        logger.info(String.format("#### -> Consumed message -> %s ", smsId));

        int id = Integer.parseInt(smsId);
        SmsRequest smsRequest = smsRequestRepository.findById(id).get();

        String phoneNumber = smsRequest.getPhoneNumber();
        logger.info(String.format("Phone Number is : %s", phoneNumber));
        boolean isBlackListed = jedis.sismember("phone_number", phoneNumber);

        //dto for elastic search service
        SmsDetailsForElasticSearch smsDetailsForElasticSearch = SmsDetailsForElasticSearch.builder().id(smsRequest.getId()).requestId(smsRequest.getRequestId())
                .phoneNumber(smsRequest.getPhoneNumber()).message(smsRequest.getMessage()).createdAt(smsRequest.getCreatedAt()).updatedAt(smsRequest.getUpdatedAt()).build();

        if(isBlackListed){
            logger.info("Blacklisted number");
//            throw new AlreadyBlackListNumberException("BlackListed Number");
            //
        }
        else{
            logger.info("Not Blacklisted \n");
            elasticSearchService.indexSmsData(smsDetailsForElasticSearch);
            elasticSearchService.getSmsDatabyId(Integer.toString(smsDetailsForElasticSearch.getId()));


            /* **************************  Use third party API Service***************** */
//            ThirdPartyResponseDTO responseDTO = thirdPartyServiceForSms.callThirdParty(smsRequest);
//            this.updateDB(responseDTO, smsRequest);
//            logger.info(responseDTO.toString());

            logger.info("third party api call successful");
        }

    }

    public void updateDB(ThirdPartyResponseDTO thirdPartyResponseDTO, SmsRequest smsRequest){
        if(thirdPartyResponseDTO.getCode().equals("1001")){
            smsRequest.setStatus(thirdPartyResponseDTO.getDescription());
            smsRequest.setUpdatedAt(LocalDateTime.now().toString());
            smsRequest.setFailureCode(Integer.parseInt(thirdPartyResponseDTO.getCode()));
            smsRequest.setFailureComments("No failure");
            smsRequestRepository.save(smsRequest);
        }

        else{
            smsRequest.setStatus(thirdPartyResponseDTO.getDescription());
            smsRequest.setUpdatedAt(LocalDateTime.now().toString());
            smsRequest.setFailureCode(Integer.parseInt(thirdPartyResponseDTO.getCode()));
            smsRequest.setFailureComments("Failed");
            smsRequestRepository.save(smsRequest);
        }
    }
}
