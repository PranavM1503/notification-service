package com.assignment.notification.exceptionhandler;


import com.assignment.notification.dto.SmsDetailTransformerDTO;
import com.assignment.notification.exceptions.AlreadyBlackListNumberException;
import com.assignment.notification.exceptions.InvalidPhoneNumberException;
import com.assignment.notification.exceptions.RequestNotFoundException;
import com.assignment.notification.models.BlackListResponse;
import com.assignment.notification.models.exceptionresponse.ExceptionResponseModel;
import com.assignment.notification.models.exceptionresponse.GetSmsExceptionResponse;
import com.assignment.notification.models.exceptionresponse.GetSmsExceptionSubResponse;
import com.assignment.notification.models.exceptionresponse.SendSmsValidationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.network.Send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<GetSmsExceptionSubResponse> handleRequestNotFoundException(RequestNotFoundException ex){

        GetSmsExceptionSubResponse responseModel = new GetSmsExceptionSubResponse(ex.getMessage());
        return new ResponseEntity<>(responseModel,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<GetSmsExceptionSubResponse> handleSqlException(SQLException ex){
        GetSmsExceptionSubResponse responseModel = new GetSmsExceptionSubResponse(ex.getMessage());
        return new ResponseEntity<>(responseModel, new HttpHeaders(),  HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex){
        logger.info(ex.getMessage());
//        logger.info(ex.getCause().toString());
        if(ex.getParameter().getParameterName().equals("smsDetailTransformerDTO")) {
//            logger.info("Inside 1");
            SendSmsValidationResponse sendSmsValidationResponse = SendSmsValidationResponse.builder().request_id(null).comments(null).code("INVALID_REQUEST").message("phone_number is Invalid").build();
            return new ResponseEntity<>(sendSmsValidationResponse, HttpStatus.BAD_REQUEST);
        }
        else if(ex.getParameter().getParameterName().equals("blackListNumberDTO")){
            ExceptionResponseModel exceptionResponseModel = ExceptionResponseModel.builder().error("No Numbers found").details("Empty List").build();
            return new ResponseEntity<>(exceptionResponseModel, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<Object> handlePhoneNumberExceptions(InvalidPhoneNumberException ex){
        logger.info(ex.getMessage());
        SendSmsValidationResponse sendSmsValidationResponse = SendSmsValidationResponse.builder().request_id(null).comments(null).code("INVALID_REQUEST").message("phone_number is Invalid").build();
        return new ResponseEntity<>(sendSmsValidationResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyBlackListNumberException.class)
    public ResponseEntity<Object> alreadyBlacklistException(AlreadyBlackListNumberException ex){
        logger.info(ex.getMessage());
        BlackListResponse blackListResponse = BlackListResponse.builder().status(ex.getMessage()).build();
        return new ResponseEntity<>(blackListResponse, HttpStatus.OK);
    }


}
