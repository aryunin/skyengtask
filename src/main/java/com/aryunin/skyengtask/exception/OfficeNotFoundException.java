package com.aryunin.skyengtask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class OfficeNotFoundException extends InvalidDataException {

    public OfficeNotFoundException(String message) {
        super(message);
    }
}
