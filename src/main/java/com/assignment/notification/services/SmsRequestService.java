package com.assignment.notification.services;


import com.assignment.notification.models.exceptionresponse.SendSmsValidationResponse;
import com.assignment.notification.dto.SmsDetailDTO;
import com.assignment.notification.transformers.SmsDetailTransformerDTO;
import com.assignment.notification.dto.SmsGetRequestDTO;
import com.assignment.notification.repositories.smsRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.assignment.notification.entities.SmsRequest;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.assignment.notification.constants.Constants.*;

@Service
public class SmsRequestService {

    public static final Logger logger = LoggerFactory.getLogger(SmsRequestService.class);
    private final smsRequestRepository smsRequestRepository;
    private final RequestProducerService requestProducerService;

    @Autowired
    public SmsRequestService (smsRequestRepository smsRequestRepository, RequestProducerService requestProducerService){
        this.smsRequestRepository = smsRequestRepository;
        this.requestProducerService = requestProducerService;
    }


    public SendSmsValidationResponse saveAndSendSms(SmsDetailTransformerDTO smsDetailTransformerDTO){

        SmsRequest smsRequest = saveSmsToDB(smsDetailTransformerDTO);
        if(smsRequest == null){
            return this.sendFailureResponse();
        }


        int id = smsRequest.getId();
        this.addIdToKafkaQueue(id);

        return this.sendSuccessResponse(smsRequest);
    }

    public void addIdToKafkaQueue(int id){
        String smsId = Integer.toString(id);

        this.requestProducerService.sendRequestToConsumer(smsId);
        logger.info("Id consumed by the consumer service : " + smsId);
    }

    public SmsRequest saveSmsToDB(SmsDetailTransformerDTO smsDetailTransformerDTO){
        SmsRequest smsRequest = saveSmsDetails(smsDetailTransformerDTO);
        smsRequestRepository.save(smsRequest);

        if(smsRequestRepository.findById(smsRequest.getId()).isPresent()){
            return smsRequest;
        }
        return null;
    }

    public SendSmsValidationResponse sendSuccessResponse(SmsRequest smsRequest){
        SendSmsValidationResponse sendSmsValidationResponse = SendSmsValidationResponse.builder().request_id(smsRequest.getRequestId()).comments(SUCCESS).code(null).message(null).build();
        return sendSmsValidationResponse;
    }

    public SendSmsValidationResponse sendFailureResponse(){
        SendSmsValidationResponse sendSmsValidationResponse = SendSmsValidationResponse.builder().request_id(null).comments(null).code(INVALID_REQUEST).message(MANDATORY_PHONE_NUMBER).build();
        return sendSmsValidationResponse;
    }

    public SmsRequest saveSmsDetails(SmsDetailTransformerDTO smsDetailTransformerDTO){
        SmsDetailDTO smsDetailDTO = SmsDetailDTO.builder().phoneNumber(smsDetailTransformerDTO.getPhoneNumber()).message(smsDetailTransformerDTO.getMessage())
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        SmsRequest smsRequest = new SmsRequest(smsDetailDTO.getPhoneNumber(), UUID.randomUUID(), smsDetailDTO.getMessage(), smsDetailDTO.getCreatedAt(), smsDetailDTO.getUpdatedAt());
        return smsRequest;
    }

    public SmsGetRequestDTO getSMSdetails (int id){

        if(smsRequestRepository.findById(id).isPresent()) {
            SmsRequest smsRequest = smsRequestRepository.findById(id).get();

            SmsGetRequestDTO smsGetRequestDTO = SmsGetRequestDTO.builder().phoneNumber(smsRequest.getPhoneNumber()).requestId(smsRequest.getRequestId())
                    .message(smsRequest.getMessage()).status(smsRequest.getStatus()).failureCode(smsRequest.getFailureCode()).failureComments(smsRequest.getFailureComments())
                    .createdAt(smsRequest.getCreatedAt()).updatedAt(smsRequest.getUpdatedAt()).build();

            return smsGetRequestDTO;
        }
        return null;
    }

}
