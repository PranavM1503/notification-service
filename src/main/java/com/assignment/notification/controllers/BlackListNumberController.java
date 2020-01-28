package com.assignment.notification.controllers;



import com.assignment.notification.dto.BlackListNumberDTO;
import com.assignment.notification.dto.CheckBlackListNumberDTO;
import com.assignment.notification.models.BlackListResponse;
import com.assignment.notification.services.BlackListNumberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("api/v1")
public class BlackListNumberController {

    public static final Logger logger = LoggerFactory.getLogger(BlackListNumberController.class);
    private BlackListNumberService blackListNumberService;

    @Autowired
    public BlackListNumberController (BlackListNumberService blackListNumberService){
        this.blackListNumberService = blackListNumberService;
    }

//    @RequestMapping(value = "/blacklist", method = RequestMethod.POST)
//    public String addBlackListNumber(@Valid  @RequestBody BlackListNumberDTO blackListNumberDTO) throws {
//        blackListNumberService.saveBlackListNumber(blackListNumberDTO);
//        return "Success";
//    }

    @RequestMapping(value = "/blacklist", method = RequestMethod.POST)
    public ResponseEntity<BlackListResponse> addBlackListNumber(@Valid @RequestBody BlackListNumberDTO blackListNumberDTO){
        BlackListResponse blackListResponse = blackListNumberService.saveBlackListNumber(blackListNumberDTO);

        return new ResponseEntity<>(blackListResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/blacklist", method = RequestMethod.GET)
    public ResponseEntity<BlackListNumberDTO> getBlackListNumbers(){
        List <String> numbers = blackListNumberService.getAllBlackListNumbers();
        BlackListNumberDTO blackListNumberDTO = new BlackListNumberDTO(numbers);

        return new ResponseEntity<BlackListNumberDTO>(blackListNumberDTO, HttpStatus.OK);
    }

    @RequestMapping(value =  "/blacklist/remove", method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BlackListResponse> removeBlackListNumber(@RequestBody CheckBlackListNumberDTO checkBlackListNumberDTO){
        logger.info(checkBlackListNumberDTO.getPhoneNumber());
        BlackListResponse blackListResponse = blackListNumberService.removeBlackListNumber(checkBlackListNumberDTO);
        return new ResponseEntity<>(blackListResponse, HttpStatus.OK);
//        return blackListNumberService.removeBlackListNumber(checkBlackListNumberDTO);
    }


//
//    @RequestMapping(value =  "/blacklist/remove", method = RequestMethod.POST)
//    public String removeBlackListNumber(@RequestBody CheckBlackListNumberDTO checkBlackListNumberDTO){
//        logger.info(checkBlackListNumberDTO.toString());
//        logger.info(checkBlackListNumberDTO.getPhoneNumber());
//        String response = blackListNumberService.removeBlackListNumber(checkBlackListNumberDTO);
//        return response;
//    }


}
