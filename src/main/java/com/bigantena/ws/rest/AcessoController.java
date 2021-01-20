/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.ws.rest;

import com.bigantena.model.Acesso;
import com.bigantena.service.AcessoService;
import com.bigantena.web.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author aspferraz
 */

@RestController
public class AcessoController {
    
    @Autowired
    private AcessoService acessoService;
    
    private static final Logger logger = Logger.getLogger(AcessoController.class);
    
    @JsonView(View.Basica.class)
    @RequestMapping(value = "/acesso/{id:[\\d]+}", method = RequestMethod.GET)
    public Acesso getAcesso(@PathVariable("id") Integer idAcesso) throws BigAntenaException {
        
        Acesso acesso = null;

        try {
            acesso = acessoService.buscarPorId(idAcesso);
        } catch (Exception ex) {
            String errorMessage = "Error invoking acessoService.buscarPorId. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Returing Acesso: " + ((acesso == null) ? "null" : acesso.toString()));

        return acesso;
    }
    
    @JsonView(View.Basica.class)
    @RequestMapping(value = "/acessos/{ids}", method = RequestMethod.GET)
    public List<Acesso> getAcessos(@PathVariable("ids") List<Integer> idsAcesso) throws BigAntenaException {
        
        List<Acesso> acessos = null;

        try {
            acessos = acessoService.buscarPorIds(idsAcesso);
        } catch (Exception ex) {
            String errorMessage = "Error invoking acessoService.buscarPorIds. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((acessos == null) ? "0" : acessos.size()));
        return acessos;
    }
    
    @RequestMapping(value = "/acessos/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAcesso(@Valid @RequestBody Acesso acesso, UriComponentsBuilder ucb, Errors errors) throws BigAntenaException {
        
        if (errors.hasErrors()) {
            String msg = errors.getAllErrors()
                                .stream()
                                .map(x -> x.getDefaultMessage())
                                .collect(Collectors.joining(","));
            throw new HttpMessageNotReadableException(msg);
        }
        
        logger.info("Creating Acesso: " + acesso.toString());
        
        try {
            
            acessoService.salvar(acesso);
            
        } catch (Exception ex) {
            String errorMessage = "Error creating new acesso. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        URI uri = ucb.path("/acessos/{id:[\\d]+}").buildAndExpand(-1).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);
        
        logger.info("New acesso created: " + acesso.toString());
        
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    
}
