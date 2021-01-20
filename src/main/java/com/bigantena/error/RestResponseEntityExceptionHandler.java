/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.error;

import com.bigantena.ws.rest.BigAntenaError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//import org.springframework.security.access.AccessDeniedException;

/**
 *
 * @author aspferraz
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messages;

    public RestResponseEntityExceptionHandler() {
        super();
    }

    
    // 415
    @ExceptionHandler({ InvalidMimeTypeException.class, InvalidMediaTypeException.class})
    protected ResponseEntity<Object> handleInvalidMimeTypeException(final IllegalArgumentException ex, final WebRequest request) {
        logger.warn("Unsupported Media Type", ex);

        final BigAntenaError bigAntenaError 
                = new BigAntenaError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, messages.getMessage("message.error", null, request.getLocale()), "Unsupported Media Type");
        return new ResponseEntity<>(bigAntenaError, new HttpHeaders(), bigAntenaError.getStatus());
    }
    
    
     // 409
    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class })
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest request) {
        logger.warn("Conflict", ex);

        final BigAntenaError bigAntenaError 
                = new BigAntenaError(HttpStatus.CONFLICT, messages.getMessage("message.error", null, request.getLocale()), "Conflict");
        return new ResponseEntity<>(bigAntenaError, new HttpHeaders(), bigAntenaError.getStatus());
    }
    
     // 400
    @Override
    protected final ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info("Bad Request", ex);

        final BigAntenaError bigAntenaError 
                = new BigAntenaError(HttpStatus.BAD_REQUEST, messages.getMessage("message.error", null, request.getLocale()), "Bad Request");
        return new ResponseEntity<>(bigAntenaError, headers, bigAntenaError.getStatus());
    }
    
    //500
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        logger.error("Internal Error ", ex);
        final BigAntenaError bigAntenaError 
                = new BigAntenaError(HttpStatus.INTERNAL_SERVER_ERROR, messages.getMessage("message.error", null, request.getLocale()), "Internal Error");
        return new ResponseEntity<>(bigAntenaError, new HttpHeaders(), bigAntenaError.getStatus());
    }

}
