/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.ws.rest;

import com.bigantena.model.Estado;
import com.bigantena.model.Pais;
import com.bigantena.service.EstadoService;
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
public class EstadoController {
    
    @Autowired
    private EstadoService estadoService;
    
    private static final Logger logger = Logger.getLogger(EstadoController.class);
    
    
    @JsonView(View.Basica.class)
    @RequestMapping(value = "/estado/{id:[\\d]+}", method = RequestMethod.GET)
    public Estado getEstado(@PathVariable("id") Integer idEstado) throws BigAntenaException {
        
        Estado estado = null;

        try {
            estado = estadoService.buscarPorId(idEstado);
        } catch (Exception ex) {
            String errorMessage = "Error invoking estadoService.buscarPorId. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Returing Estado: " + ((estado == null) ? "null" : estado.toString()));

        return estado;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/estados", method = RequestMethod.GET)
    public Estado getEstadoPorNome(@RequestParam(value = "nome", required = false) String nome) throws BigAntenaException 
    {
        Estado estado = null;
         
        try {
            estado = estadoService.obterPorNome(nome);
        } catch (Exception ex) {
            String errorMessage = "Error invoking estadoService.obterPorNome. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Returing Estado: " + ((estado == null) ? "null" : estado.toString()));

        return estado;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/paises/{idPais:[\\d]+}/estados", method = RequestMethod.GET)
    public List<Estado> getEstadosPorPais(@PathVariable("idPais") Integer idPais) throws BigAntenaException 
    {
        List<Estado> estados = null;
        Pais pais = new Pais(idPais);
        
        try {
            estados = estadoService.buscarPorPais(pais);
        } catch (Exception ex) {
            String errorMessage = "Error invoking estadoService.obterPorPais. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((estados == null) ? "0" : estados.size()));
        return estados;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/paises/{idPais:[\\d]+}/generos/{idsGeneros}/estados", method = RequestMethod.GET)
    public List<Estado> getEstadosPorPaisEGeneros(@PathVariable("idPais") Integer idPais, 
                                                     @PathVariable("idsGeneros") List<Integer> idsGeneros) throws BigAntenaException 
    {
        List<Estado> estados = null;
        Pais pais = new Pais(idPais);
        
        try {
            estados = estadoService.buscarPorPaisEGeneros(pais, idsGeneros);
        } catch (Exception ex) {
            String errorMessage = "Error invoking estadoService.buscarPorPaisEGeneros. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((estados == null) ? "0" : estados.size()));
        return estados;
    }
}
