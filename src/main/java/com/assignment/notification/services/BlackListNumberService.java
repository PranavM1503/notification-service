package com.assignment.notification.services;


import com.assignment.notification.dto.BlackListNumberDTO;
import com.assignment.notification.dto.CheckBlackListNumberDTO;
import com.assignment.notification.entities.BlackListNumber;
import com.assignment.notification.repositories.BlackListNumberRepository;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BlackListNumberService {

    private BlackListNumberRepository blackListNumberRepository;

    @Autowired
     public BlackListNumberService(BlackListNumberRepository blackListNumberRepository){
        this.blackListNumberRepository = blackListNumberRepository;
    }

    Jedis jedis = new Jedis();

    public void saveBlackListNumber(BlackListNumberDTO blackListNumberDTO){

        List<String> numbers = blackListNumberDTO.getBlackListNumber();


        for(String number : numbers){
            BlackListNumber blackListNumber = new BlackListNumber(number);
            blackListNumberRepository.save(blackListNumber);
            jedis.sadd("phone_number", number);
        }
//
    }

    public List<String> getAllBlackListNumbers(){
        Set <String> AllBlackListNumbersSet = jedis.smembers("phone_number");

        List <String> AllBlackListNumbers = new ArrayList<String>(AllBlackListNumbersSet);
        return AllBlackListNumbers;
    }

    public String removeBlackListNumber(CheckBlackListNumberDTO checkBlackListNumberDTO){

        boolean isBlackListedBefore = jedis.sismember("phone_number", checkBlackListNumberDTO.getPhoneNumber());

        if(isBlackListedBefore){

            jedis.srem("phone_number", checkBlackListNumberDTO.getPhoneNumber());
            BlackListNumber blackListNumber = new BlackListNumber(checkBlackListNumberDTO.getPhoneNumber());
            blackListNumberRepository.delete(blackListNumber);
            return checkBlackListNumberDTO.getPhoneNumber() + " is removed from blacklist.";

        }
        return "No Such Number Found";
    }

}
