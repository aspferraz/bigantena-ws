/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.ws.rest;

import com.bigantena.model.Cidade;
import com.bigantena.model.Estado;
import com.bigantena.model.Pais;
import com.bigantena.service.CidadeService;
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
public class CidadeController {
    
    
    @Autowired
    private CidadeService cidadeService;
    
    private static final Logger logger = Logger.getLogger(CidadeController.class);
    
    
    @JsonView(View.Basica.class)
    @RequestMapping(value = "/cidade/{id:[\\d]+}", method = RequestMethod.GET)
    public Cidade getCidade(@PathVariable("id") Integer idCidade) throws BigAntenaException {
        
        Cidade cidade = null;

        try {
            cidade = cidadeService.buscarPorId(idCidade);
        } catch (Exception ex) {
            String errorMessage = "Error invoking cidadeService.buscarPorId. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Returing Cidade: " + ((cidade == null) ? "null" : cidade.toString()));

        return cidade;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/cidades", method = RequestMethod.GET)
    public Cidade getCidadePorNome(@RequestParam(value = "nome", required = false) String nome) throws BigAntenaException 
    {
        Cidade cidade = null;
         
        try {
            cidade = cidadeService.obterPorNome(nome);
        } catch (Exception ex) {
            String errorMessage = "Error invoking cidadeService.obterPorNome. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Returing Cidade: " + ((cidade == null) ? "null" : cidade.toString()));

        return cidade;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/paises/{idPais:[\\d]+}/cidades", method = RequestMethod.GET)
    public List<Cidade> getCidadesPorPais(@PathVariable("idPais") Integer idPais) throws BigAntenaException 
    {
        List<Cidade> cidades = null;
        Pais pais = new Pais(idPais);
        
        try {
            cidades = cidadeService.buscarPorPais(pais);
        } catch (Exception ex) {
            String errorMessage = "Error invoking cidadeService.obterPorPais. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((cidades == null) ? "0" : cidades.size()));
        return cidades;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/paises/{idPais:[\\d]+}/generos/{idsGeneros}/cidades", method = RequestMethod.GET)
    public List<Cidade> getCidadesPorPaisEGeneros(@PathVariable("idPais") Integer idPais,
                                                     @PathVariable("idsGeneros") List<Integer> idsGeneros) throws BigAntenaException 
    {
        List<Cidade> cidades = null;
        Pais pais = new Pais(idPais);
        
        try {
            cidades = cidadeService.buscarPorPaisEGeneros(pais, idsGeneros);
        } catch (Exception ex) {
            String errorMessage = "Error invoking cidadeService.buscarPorPaisEGeneros. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((cidades == null) ? "0" : cidades.size()));
        return cidades;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/paises/estados/{idEstado:[\\d]+}/cidades", method = RequestMethod.GET)
    public List<Cidade> getCidadesPorEstado(@PathVariable("idEstado") Integer idEstado) throws BigAntenaException 
    {
        List<Cidade> cidades = null;
        Estado estado = new Estado(idEstado);
        
        try {
            cidades = cidadeService.buscarPorEstado(estado);
        } catch (Exception ex) {
            String errorMessage = "Error invoking cidadeService.obterPorEstado. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((cidades == null) ? "0" : cidades.size()));
        return cidades;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/paises/estados/{idEstado:[\\d]+}/generos/{idsGeneros}/cidades", method = RequestMethod.GET)
    public List<Cidade> getCidadesPorEstadoEGeneros(@PathVariable("idEstado") Integer idEstado,
                                                       @PathVariable("idsGeneros") List<Integer> idsGeneros) throws BigAntenaException 
    {
        List<Cidade> cidades = null;
        Estado estado = new Estado(idEstado);
        
        try {
            cidades = cidadeService.buscarPorEstadoEGeneros(estado, idsGeneros);
        } catch (Exception ex) {
            String errorMessage = "Error invoking cidadeService.buscarPorEstadoEGeneros. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((cidades == null) ? "0" : cidades.size()));
        return cidades;
    }
}
