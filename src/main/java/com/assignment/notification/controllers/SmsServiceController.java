package com.assignment.notification.controllers;


import com.assignment.notification.dto.SmsDetailTransformerDTO;
import com.assignment.notification.dto.SmsGetRequestDTO;
import com.assignment.notification.exceptions.RequestNotFoundException;
import com.assignment.notification.services.RequestProducerService;
import com.assignment.notification.services.SmsRequestService;
import org.slf4j.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1")
public class SmsServiceController {

    public static final Logger logger = LoggerFactory.getLogger(SmsServiceController.class);
    private SmsRequestService smsRequestService;
    private final RequestProducerService requestProducerService;

    @Autowired
    public SmsServiceController (SmsRequestService smsRequestService, RequestProducerService requestProducerService){
        this.smsRequestService= smsRequestService;
        this.requestProducerService = requestProducerService;
    }

    @RequestMapping(value = "/sms/send", method = RequestMethod.POST)
    public String smsRequest(@RequestBody SmsDetailTransformerDTO smsDetailTransformerDTO){

        int smsid = smsRequestService.saveData(smsDetailTransformerDTO);
        String id = Integer.toString(smsid);
        logger.info("Id retrieved is  : %s" , id);

        this.requestProducerService.sendRequestToConsumer(id);
        logger.info("Id consumed by the consumer service");
        return "Success : " + id;
    }

    @RequestMapping(value = "/sms/{id}", method = RequestMethod.GET)
    public ResponseEntity<SmsGetRequestDTO> getSmsDetails(@PathVariable("id") int id) throws RequestNotFoundException{
        logger.info("Fetching SMS Details by id : {}", id);

        SmsGetRequestDTO smsGetRequestDTO;
        smsGetRequestDTO  = smsRequestService.getSMSdetails(id);

        if(smsGetRequestDTO == null){
            throw new RequestNotFoundException("Request_id not found");
        }
        return new ResponseEntity<SmsGetRequestDTO>(smsGetRequestDTO, HttpStatus.OK);

    }
}
