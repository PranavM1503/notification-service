package com.assignment.notification.exceptionhandler;


import com.assignment.notification.exceptions.RequestNotFoundException;
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
    public ResponseEntity<SendSmsValidationResponse> handleValidationExceptions(MethodArgumentNotValidException ex){
//        logger.info(ex.getCause().toString());
        SendSmsValidationResponse sendSmsValidationResponse = SendSmsValidationResponse.builder().request_id(null).comments(null).code("INVALID_REQUEST").message("phone_number is Invalid").build();
        return new ResponseEntity<>(sendSmsValidationResponse, HttpStatus.BAD_REQUEST);
    }

}
