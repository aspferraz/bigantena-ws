/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.ws.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

/**
 *
 * @author aspferraz
 */
public class BigAntenaError {

    private HttpStatus status;
    private String message;
    private String error;
    

    //
    public BigAntenaError() {
        super();
    }

    public BigAntenaError(final HttpStatus status, final String message, final List<ObjectError> errors) {
        super();
        this.status = status;
        this.message = errors.stream()
            .map(e -> e.getDefaultMessage())
        .collect(Collectors.joining(","));
    }

    public BigAntenaError(final HttpStatus status, final String message, final String error) {
        super();
        this.status = status;
        this.message = message;
        this.error = error;
        
    }

    //
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setError(final String error) {
        this.error = error;
    }
    
    public String getError() {
        return error;
    }

}
