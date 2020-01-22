package com.assignment.notification.services;


import com.assignment.notification.dto.SmsDetailDTO;
import com.assignment.notification.dto.SmsDetailTransformerDTO;
import com.assignment.notification.dto.SmsGetRequestDTO;
import com.assignment.notification.repositories.smsRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.assignment.notification.entities.SmsRequest;

import java.time.LocalDateTime;

@Service
public class SmsRequestService {

    private smsRequestRepository smsRequestRepository;

    @Autowired
    public SmsRequestService (smsRequestRepository smsRequestRepository){
        this.smsRequestRepository = smsRequestRepository;
    }


    public int saveData(SmsDetailTransformerDTO smsDetailTransformerDTO){
        SmsRequest SmsRequest;
        SmsDetailDTO smsDetailDTO;
        smsDetailDTO = new SmsDetailDTO(smsDetailTransformerDTO.getPhoneNumber(), smsDetailTransformerDTO.getMessage());
        smsDetailDTO.setCreatedAt(LocalDateTime.now());
        smsDetailDTO.setUpdatedAt(LocalDateTime.now());
        SmsRequest = new SmsRequest(smsDetailDTO.getPhoneNumber(), smsDetailDTO.getMessage(), smsDetailDTO.getCreatedAt(), smsDetailDTO.getUpdatedAt());
        smsRequestRepository.save(SmsRequest);
        int id = SmsRequest.getId();
        return id;
    }


//    public int saveData(SmsDetailDTO smsRequestDTO){
//        SmsRequest SmsRequest;
//        SmsRequest = new SmsRequest(smsRequestDTO.getPhoneNumber(), smsRequestDTO.getMessage(), smsRequestDTO.getCreatedAt(), smsRequestDTO.getUpdatedAt());
//        smsRequestRepository.save(SmsRequest);
//        int id = SmsRequest.getId();
//        return id;
//    }

    public SmsGetRequestDTO getSMSdetails (int id){

        SmsRequest smsRequest;

        if(smsRequestRepository.findById(id).isPresent()) {
            smsRequest = smsRequestRepository.findById(id).get();
            SmsGetRequestDTO smsGetRequestDTO;
            smsGetRequestDTO = new SmsGetRequestDTO(smsRequest.getPhoneNumber(), smsRequest.getRequestId(),
                    smsRequest.getMessage(),
                    smsRequest.getStatus(),
                    smsRequest.getFailureCode(),
                    smsRequest.getFailureComments(),
                    smsRequest.getCreatedAt(),
                    smsRequest.getUpdatedAt());

            return smsGetRequestDTO;
        }

        return null;
    }

}
