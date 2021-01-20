/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.ws.rest;

import com.bigantena.model.Genero;
import com.bigantena.service.GeneroService;
import com.bigantena.web.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author aspferraz
 */

@RestController
public class GeneroController {
    
    @Autowired
    private GeneroService generoService;
    
    private static final Logger logger = Logger.getLogger(GeneroController.class);
    
    
    @JsonView(View.Basica.class)
    @RequestMapping(value = "/genero/{id:[\\d]+}", method = RequestMethod.GET)
    public Genero getGenero(@PathVariable("id") Integer idGenero) throws BigAntenaException {
        
        Genero genero = null;

        try {
            genero = generoService.buscarPorId(idGenero);
        } catch (Exception ex) {
            String errorMessage = "Error invoking generoService.buscarPorId. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Returing Genero: " + ((genero == null) ? "null" : genero.toString()));

        return genero;
    }
    
    @JsonView(View.Basica.class)
    @RequestMapping(value = "/generos", method = RequestMethod.GET)
    public List<Genero> getGeneros() throws BigAntenaException {
        
        List<Genero> generos = null;

        try {
            generos = generoService.buscarTodos();
        } catch (Exception ex) {
            String errorMessage = "Error invoking generoService.buscarTodos. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((generos == null) ? "0" : generos.size()));
        return generos;
    }
    
}
