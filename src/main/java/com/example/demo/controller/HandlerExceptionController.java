package com.example.demo.controller;

import com.example.demo.mapper.exception.FileParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerExceptionController {

    @ExceptionHandler(FileParseException.class)
    public ResponseEntity<String> handleFileParseException (FileParseException e){
        return new ResponseEntity<>("Error by paring file: " +  e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}