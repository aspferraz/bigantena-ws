/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.ws.rest;

import com.bigantena.model.Continente;
import com.bigantena.service.ContinenteService;
import com.bigantena.web.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author aspferraz
 */

@RestController
public class ContinenteController {
    
    @Autowired
    private ContinenteService continenteService;
    
    private static final Logger logger = Logger.getLogger(ContinenteController.class);
    
    @JsonView(View.BasicaComPaises.class)
    @RequestMapping(value = "/continentes", method = RequestMethod.GET)
    public List<Continente> getContinentes() throws BigAntenaException {
        
        List<Continente> continentes = null;

        try {
            continentes = continenteService.buscarTodos();
        } catch (Exception ex) {
            String errorMessage = "Error invoking continenteService.buscarTodos. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((continentes == null) ? "0" : continentes.size()));
        return continentes;
    }
    
    
}
