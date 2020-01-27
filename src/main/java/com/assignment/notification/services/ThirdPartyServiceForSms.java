package com.assignment.notification.services;

import com.assignment.notification.dto.ThirdPartyResponseDTO;
import com.assignment.notification.dto.ThirdPartyResponseInterceptorDTO;
import com.assignment.notification.models.SmsApiModel;
import com.assignment.notification.entities.SmsRequest;
import com.assignment.notification.repositories.smsRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThirdPartyServiceForSms {

    private final String apiUrl = "https://api.imiconnect.in/resources/v1/messaging";

    private final Logger logger = LoggerFactory.getLogger(ThirdPartyServiceForSms.class);
    private smsRequestRepository smsRequestRepository;

    @Autowired
    public ThirdPartyServiceForSms(smsRequestRepository smsRequestRepository){
        this.smsRequestRepository = smsRequestRepository;
    }



    public ThirdPartyResponseDTO callThirdParty(SmsRequest smsRequest) throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("key", "7b73f76d-369e-11ea-9e4e-025282c394f2");



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
//        ResponseEntity<Object> response = restTemplate.postForEntity(apiUrl, httpRequest, Object.class);
        logger.info(response.toString());
        logger.info(response.getBody().toString());
        logger.info(String.valueOf(response.getStatusCodeValue()));


        //*******************Intercepting Response body and converting it to object***************
        ThirdPartyResponseInterceptorDTO thirdPartyResponseInterceptorDTO = objectMapper.readValue(response.getBody().toString(), ThirdPartyResponseInterceptorDTO.class);
        List<ThirdPartyResponseDTO> responseList = thirdPartyResponseInterceptorDTO.getResponse();
        logger.info(responseList.get(0).getCode());
//        ResponseEntity<ThirdPartyResponseDTO> response = restTemplate.postForEntity(apiUrl, httpRequest, ThirdPartyResponseDTO.class).getBody();
//        logger.info(response.toString())
        logger.info(response.getBody().toString());
//     *********************************************************************************************

//        ThirdPartyResponseDTO data;
//        data = (ThirdPartyResponseDTO) response.getBody();

//        logger.info(response.getBody().toString());

        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("Request Successful");
        }else{
//                logger.info("Request Failed");
            logger.info(String.format(response.getStatusCode().toString()));
        }

        return responseList.get(0);
    }

}
