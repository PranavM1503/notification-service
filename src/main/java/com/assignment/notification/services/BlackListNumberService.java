package com.assignment.notification.services;


import com.assignment.notification.controllers.SmsServiceController;
import com.assignment.notification.dto.BlackListNumberDTO;
import com.assignment.notification.dto.CheckBlackListNumberDTO;
import com.assignment.notification.entities.BlackListNumber;
import com.assignment.notification.exceptions.InvalidPhoneNumberException;
import com.assignment.notification.models.BlackListResponse;
import com.assignment.notification.repositories.BlackListNumberRepository;

import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BlackListNumberService {

    public static final Logger logger = LoggerFactory.getLogger(BlackListNumberService.class);
    private BlackListNumberRepository blackListNumberRepository;

    @Autowired
     public BlackListNumberService(BlackListNumberRepository blackListNumberRepository){
        this.blackListNumberRepository = blackListNumberRepository;
    }

    Jedis jedis = new Jedis();

    public BlackListResponse saveBlackListNumber(BlackListNumberDTO blackListNumberDTO){

        List<String> numbers = blackListNumberDTO.getBlackListNumber();
        BlackListResponse blackListResponse;
        if(numbers.size() < 1){
            blackListResponse = BlackListResponse.builder().status("No Numbers found").build();
            return blackListResponse;
        }

        for(String number : numbers){
            if(jedis.sismember("phone_number", number)){
                logger.info("Phone Number : " + number + " Already Blacklisted");
                continue;
            }

            if(number.length() == 13) {
                BlackListNumber blackListNumber = new BlackListNumber(number);
                blackListNumberRepository.save(blackListNumber);
                jedis.sadd("phone_number", number);
            }
            else{
                logger.info("Invalid Phone Number : " + number);
            }
        }

        blackListResponse = BlackListResponse.builder().status("Numbers Blacklisted").build();
        return blackListResponse;
    }

    public List<String> getAllBlackListNumbers(){
        Set <String> AllBlackListNumbersSet = jedis.smembers("phone_number");

        if(AllBlackListNumbersSet.size() > 0) {
            List<String> AllBlackListNumbers = new ArrayList<String>(AllBlackListNumbersSet);
            return AllBlackListNumbers;
        }

        logger.info("No Blacklist Number found");
        return null;
    }


    public BlackListResponse removeBlackListNumber(CheckBlackListNumberDTO checkBlackListNumberDTO){

        boolean isBlackListedBefore = jedis.sismember("phone_number", checkBlackListNumberDTO.getPhoneNumber());
        BlackListResponse blackListResponse;
        if(isBlackListedBefore){
            jedis.srem("phone_number", checkBlackListNumberDTO.getPhoneNumber());
            BlackListNumber blackListNumber = new BlackListNumber(checkBlackListNumberDTO.getPhoneNumber());
            blackListNumberRepository.delete(blackListNumber);
            blackListResponse = BlackListResponse.builder().status("Phone Number : " + checkBlackListNumberDTO.getPhoneNumber() + " removed from blacklist").build();
            logger.info(checkBlackListNumberDTO.getPhoneNumber() + " is removed from blacklist.");
            return blackListResponse;

        }
        blackListResponse = BlackListResponse.builder().status("Phone Number Not Available").build();
        return blackListResponse;
    }

//
//    public String removeBlackListNumber(CheckBlackListNumberDTO checkBlackListNumberDTO){
//
//        boolean isBlackListedBefore = jedis.sismember("phone_number", checkBlackListNumberDTO.getPhoneNumber());
//
//        if(isBlackListedBefore == true){
//
//            jedis.srem("phone_number", checkBlackListNumberDTO.getPhoneNumber());
//            BlackListNumber blackListNumber = new BlackListNumber(checkBlackListNumberDTO.getPhoneNumber());
//            blackListNumberRepository.delete(blackListNumber);
//            return checkBlackListNumberDTO.getPhoneNumber() + " is removed from blacklist.";
//
//        }
//        return "No Such Number Found";
//    }

}
