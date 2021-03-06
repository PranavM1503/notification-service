package com.assignment.notification.controllers;


import com.assignment.notification.transformers.SmsDetailTransformerDTO;
import com.assignment.notification.dto.SmsGetRequestDTO;
import com.assignment.notification.exceptions.InvalidPhoneNumberException;
import com.assignment.notification.exceptions.RequestNotFoundException;
import com.assignment.notification.models.SmsUserSuccessResponse;
import com.assignment.notification.models.exceptionresponse.SendSmsValidationResponse;
import com.assignment.notification.services.RequestProducerService;
import com.assignment.notification.services.SmsRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.assignment.notification.constants.Constants.*;


@RestController
@RequestMapping("api/v1")
public class SmsServiceController {

    public static final Logger logger = LoggerFactory.getLogger(SmsServiceController.class);
    private final SmsRequestService smsRequestService;

    @Autowired
    public SmsServiceController (SmsRequestService smsRequestService){
        this.smsRequestService= smsRequestService;
    }

    @RequestMapping(value = "/sms/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> smsRequest(@Valid @RequestBody SmsDetailTransformerDTO smsDetailTransformerDTO) throws InvalidPhoneNumberException{

        if(smsDetailTransformerDTO.getPhoneNumber().substring(0,3).compareTo("+91") != 0){
            throw new InvalidPhoneNumberException(INVALID_PHONE_NUMBER);
        }
        SendSmsValidationResponse sendSmsValidationResponse = smsRequestService.saveAndSendSms(smsDetailTransformerDTO);
        SmsUserSuccessResponse smsUserSuccessResponse = new SmsUserSuccessResponse(sendSmsValidationResponse);
        return new ResponseEntity<Object>(smsUserSuccessResponse, HttpStatus.OK);
    }


    @RequestMapping(value = "/sms/{id}", method = RequestMethod.GET)
    public ResponseEntity<SmsGetRequestDTO> getSmsDetails(@PathVariable("id") int id) throws RequestNotFoundException{
        logger.info("Fetching SMS Details by id : {}", id);
        SmsGetRequestDTO smsGetRequestDTO  = smsRequestService.getSMSdetails(id);

        if(smsGetRequestDTO == null){
            logger.info("Id not found : " + id);
            throw new RequestNotFoundException("Id not found : " + id);
        }
        return new ResponseEntity<SmsGetRequestDTO>(smsGetRequestDTO, HttpStatus.OK);
    }
}
