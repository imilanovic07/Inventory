package com.bis.h2Inventory.Inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class WrongDateAndTimeForProductException extends RuntimeException {

    public WrongDateAndTimeForProductException(String message) {
        super(message);
    }

}
