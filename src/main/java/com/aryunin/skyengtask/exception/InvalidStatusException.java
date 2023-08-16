package com.aryunin.skyengtask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class InvalidStatusException extends InvalidDataException {

    public InvalidStatusException(String message) {
        super(message);
    }
}
