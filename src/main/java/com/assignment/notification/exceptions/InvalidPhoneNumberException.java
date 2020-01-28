package com.assignment.notification.exceptions;

public class InvalidPhoneNumberException extends RuntimeException {
    public InvalidPhoneNumberException(){
    }

    public InvalidPhoneNumberException(String message){
        super(message);
    }
}
