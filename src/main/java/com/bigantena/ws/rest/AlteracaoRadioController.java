/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.ws.rest;

import com.bigantena.model.AlteracaoRadio;
import com.bigantena.service.AlteracaoRadioService;
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
public class AlteracaoRadioController {
    
    @Autowired
    AlteracaoRadioService alteracaoRadioService;
    
    private static final Logger logger = Logger.getLogger(AlteracaoRadioController.class);
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/alteracao-radio/{id:[\\d]+}", method = RequestMethod.GET)
    public AlteracaoRadio getAlteracaoRadio(@PathVariable("id") Integer idAlteracao) throws BigAntenaException {
        
        AlteracaoRadio alteracao = null;

        try {
            alteracao = alteracaoRadioService.buscarPorId(idAlteracao);
        } catch (Exception ex) {
            String errorMessage = "Error invoking alteracaoRadioService.buscarPorId. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Returing AlteracaoRadio: " + ((alteracao == null) ? "null" : alteracao.toString()));

        return alteracao;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/alteracoes-radios/{ids}", method = RequestMethod.GET)
    public List<AlteracaoRadio> getAlteracoesRadios(@PathVariable("ids") List<Integer> idsAlteracoes) throws BigAntenaException {
        
        List<AlteracaoRadio> alteracoes = null;

        try {
            alteracoes = alteracaoRadioService.buscarPorIds(idsAlteracoes);
        } catch (Exception ex) {
            String errorMessage = "Error invoking alteracaoRadioService.buscarPorIds. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((alteracoes == null) ? "0" : alteracoes.size()));
        return alteracoes;
    }
    
    @RequestMapping(value = "/alteracoes-radios/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAlteracoRadio(@Valid @RequestBody AlteracaoRadio alteracao, UriComponentsBuilder ucb, Errors errors) throws BigAntenaException {
        
        if (errors.hasErrors()) {
            String msg = errors.getAllErrors()
                                .stream()
                                .map(x -> x.getDefaultMessage())
                                .collect(Collectors.joining(","));
            throw new HttpMessageNotReadableException(msg);
        }
        
        logger.info("Creating AlteracaoRadio: " + alteracao.toString());
        
        try {
            
            alteracaoRadioService.salvar(alteracao);
            
        } catch (Exception ex) {
            String errorMessage = "Error creating new alteracoRadio. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        URI uri = ucb.path("/alteracao-radio/{id:[\\d]+}").buildAndExpand(-1).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);
        
        logger.info("New alteracaoRadio created: " + alteracao.toString());
        
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    
}
