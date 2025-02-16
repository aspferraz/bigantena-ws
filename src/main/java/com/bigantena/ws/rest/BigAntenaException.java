/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.ws.rest;

/**
 *
 * @author aspferraz
 */
public class BigAntenaException extends Exception {

    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public BigAntenaException() {
        super();
    }
    
    public BigAntenaException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
}
