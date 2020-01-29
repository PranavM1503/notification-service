package com.assignment.notification.services;

import com.assignment.notification.dto.ThirdPartyFailResponse;
import com.assignment.notification.dto.ThirdPartyResponseDTO;
import com.assignment.notification.dto.ThirdPartyResponseInterceptorDTO;
import com.assignment.notification.models.SmsApiModel;
import com.assignment.notification.entities.SmsRequest;
import com.assignment.notification.repositories.smsRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThirdPartyServiceForSms {

    private final String apiUrl = "https://api.imiconnect.in/resources/v1/messaging";

    private final Logger logger = LoggerFactory.getLogger(ThirdPartyServiceForSms.class);
    private final smsRequestRepository smsRequestRepository;
    private final String contentType = "application/json";
    private final String key = "Enter yout key";
    //remove last 1 from key ::::::::::::::: wrong key above

    @Autowired
    public ThirdPartyServiceForSms(smsRequestRepository smsRequestRepository){
        this.smsRequestRepository = smsRequestRepository;
    }

    public ThirdPartyResponseDTO callThirdParty(SmsRequest smsRequest) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = this.createHeaders(contentType, key);
        SmsApiModel smsApiModel = this.createAPIRequestModel(smsRequest);

//   ********************         json conversion to verify the post request           *******************
        this.verifyJSON(smsApiModel);
//   ******************************************************************************************************

        HttpEntity<SmsApiModel> httpRequest = new HttpEntity<>(smsApiModel, headers);
        logger.info(String.format(httpRequest.toString()));

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, httpRequest, String.class);
        logger.info(response.toString());
        logger.info(response.getBody().toString());
        logger.info(String.valueOf(response.getStatusCodeValue()));

        ObjectMapper objectMapper = new ObjectMapper();
        try{
            ThirdPartyResponseInterceptorDTO thirdPartyResponseInterceptorDTO = objectMapper.readValue(response.getBody().toString(), ThirdPartyResponseInterceptorDTO.class);
            List<ThirdPartyResponseDTO> responseList = thirdPartyResponseInterceptorDTO.getResponse();
            logger.info(responseList.get(0).getCode());
            return responseList.get(0);
        }catch (MismatchedInputException ex){
            logger.info(ex.getMessage());
            logger.info("In catch block");
            ThirdPartyFailResponse thirdPartyFailResponse = objectMapper.readValue(response.getBody().toString(), ThirdPartyFailResponse.class);
            logger.info(thirdPartyFailResponse.getResponse().getCode());
            return thirdPartyFailResponse.getResponse();
        }catch (RestClientException ex){
            logger.debug(ex.getMessage());
            logger.info(ex.getClass().toString());
            ThirdPartyFailResponse thirdPartyFailResponse = objectMapper.readValue(response.getBody().toString(), ThirdPartyFailResponse.class);
            return  thirdPartyFailResponse.getResponse();
        }

    }

    public SmsApiModel createAPIRequestModel(SmsRequest smsRequest){
        List <String> msisdn = new ArrayList<>();
        msisdn.add(smsRequest.getPhoneNumber());
        return new SmsApiModel(smsRequest.getMessage(), msisdn, smsRequest.getRequestId());
    }

    public HttpHeaders createHeaders(String type, String key){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", type);
        headers.set("key", key);

        return headers;
    }

    public void verifyJSON(SmsApiModel smsApiModel){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(smsApiModel);
            logger.info("\n"  + json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
