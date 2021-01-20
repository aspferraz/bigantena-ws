/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.ws.rest;

import com.bigantena.model.Continente;
import com.bigantena.model.Pais;
import com.bigantena.service.PaisService;
import com.bigantena.web.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author aspferraz
 */

@RestController
public class PaisController {
    
    @Autowired
    private PaisService paisService;
    
    private static final Logger logger = Logger.getLogger(PaisController.class);
    
    
    @JsonView(View.BasicaComLocais.class)
    @RequestMapping(value = "/pais/{id:[\\d]+}", method = RequestMethod.GET)
    public Pais getPais(@PathVariable("id") Integer idPais) throws BigAntenaException {
        
        Pais pais = null;

        try {
            pais = paisService.buscarPorId(idPais);
        } catch (Exception ex) {
            String errorMessage = "Error invoking paisService.buscarPorId. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Returing Pais: " + ((pais == null) ? "null" : pais.toString()));

        return pais;
    }
    
    @JsonView(View.MinimaComBandeira.class)
    @RequestMapping(value = "/continentes/paises", method = RequestMethod.GET)
    public List<Pais> getPaises() throws BigAntenaException {
        
        List<Pais> paises = null;

        try {
            paises = paisService.buscarTodos();
        } catch (Exception ex) {
            String errorMessage = "Error invoking paisService.buscarTodos. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((paises == null) ? "0" : paises.size()));
        return paises;
    }
    
    
    
    @JsonView(View.MinimaComBandeira.class)
    @RequestMapping(value = "/generos/{idsGeneros}/paises", method = RequestMethod.GET)
    public List<Pais> getPaisesPorGeneros(@PathVariable("idsGeneros") List<Integer> idsGeneros) throws BigAntenaException 
    {
        List<Pais> paises = null;

        try {
            paises = paisService.buscarPorGeneros(idsGeneros);
        } catch (Exception ex) {
            String errorMessage = "Error invoking paisService.buscarPorGeneros. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((paises == null) ? "0" : paises.size()));
        return paises;
    }
    
    @JsonView(View.MinimaComBandeira.class)
    @RequestMapping(value = "/continentes/{idContinente:[\\d]+}/paises", method = RequestMethod.GET)
    public List<Pais> getPaisesPorContinente(@PathVariable("idContinente") Integer idContinente) throws BigAntenaException 
    {
        List<Pais> paises = null;
        Continente continente = new Continente(idContinente);
        
        try {
            paises = paisService.buscarPorContinente(continente);
        } catch (Exception ex) {
            String errorMessage = "Error invoking paisService.buscarPorContinente. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((paises == null) ? "0" : paises.size()));
        return paises;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/paises", method = RequestMethod.GET)
    public Pais getPaisPorSiglaOuNomeEn(
            @RequestParam(value = "sigla", required = true) String sigla,
            @RequestParam(value = "nome-en", required = false) String nomeEn) throws BigAntenaException 
    {
        Pais pais = null;
         
        try {
            pais = paisService.obterPorSiglaOuNomeEn(sigla, nomeEn);
        } catch (Exception ex) {
            String errorMessage = "Error invoking paisService.obterPaisPorSiglaOuNomeEn. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Returing Pais: " + ((pais == null) ? "null" : pais.toString()));

        return pais;
    }
    
}
