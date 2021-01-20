/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.ws.rest;


import com.bigantena.model.Cidade;
import com.bigantena.model.Estado;
import com.bigantena.model.Genero;
import com.bigantena.model.Pais;
import com.bigantena.service.RadioService;
import com.bigantena.model.Radio;
import com.bigantena.web.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author aspferraz
 */

@RestController
public class RadioController {

    @Autowired
    private RadioService radioService;

    private static final Logger logger = Logger.getLogger(RadioController.class);
    
    @JsonView(View.Basica.class)
    @RequestMapping(value = "/radio/{id:[\\d]+}", method = RequestMethod.GET)
    public Radio getRadio(@PathVariable("id") Integer idRadio) throws BigAntenaException {
        
        Radio radio = null;

        try {
            radio = radioService.buscarPorId(idRadio);
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarPorId. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Returing Radio: " + ((radio == null) ? "null" : radio.toString()));

        return radio;
    }
    
    @JsonView(View.Basica.class)
    @RequestMapping(value = "/radios/{ids}", method = RequestMethod.GET)
    public List<Radio> getRadios(@PathVariable("ids") List<Integer> idsRadio) throws BigAntenaException {
        
        List<Radio> radios = null;

        try {
            radios = radioService.buscarPorIds(idsRadio);
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarPorIds. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((radios == null) ? "0" : radios.size()));
        return radios;
    }
    
    @JsonView(View.Basica.class)
    @RequestMapping(value = "/radios/ids", method = RequestMethod.GET)
    public List<Radio> getRadiosPorIdsComPaginacao(
            @RequestParam(value = "values", required = true) List<Integer> idsRadio,
            @RequestParam(value = "pagina", required = false) Integer pagina,
            @RequestParam(value = "limite", required = false) Integer limite
           
    )throws BigAntenaException {
        
        List<Radio> radios = null;
        if (pagina == null || limite == null) {
            pagina = 1;
            limite = 200;
        }

        try {
            radios = radioService.buscarPorIds(idsRadio, pagina, limite);
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarPorIds. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((radios == null) ? "0" : radios.size()));
        return radios;
    }
    
    @RequestMapping(value = "/radios/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addRadio(@Valid @RequestBody Radio radio, UriComponentsBuilder ucb, Errors errors) throws BigAntenaException {
        
        Radio nRadio = null;
        
        if (errors.hasErrors()) {
            String msg = errors.getAllErrors()
                                .stream()
                                .map(x -> x.getDefaultMessage())
                                .collect(Collectors.joining(","));
            throw new HttpMessageNotReadableException(msg);
        }
        
        logger.info("Creating Radio: " + radio.toString());
        
        try {
            
            Integer id = radioService.obterProximoId();
            radio.setId(id);
            nRadio = radioService.salvar(radio);
            
        } catch (Exception ex) {
            String errorMessage = "Error creating new radio. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        URI uri = ucb.path("/radios/{id:[\\d]+}").buildAndExpand(nRadio.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);
        
        logger.info("New radio created: " + nRadio.toString());
        
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/radios/update{id:[\\d]+}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateRadio(
            @PathVariable("id") Integer idRadio, @Valid @RequestBody Radio radio, Errors errors) throws BigAntenaException {
        
        radio.setId(idRadio);
        
        if (errors.hasErrors()) {
            String msg = errors.getAllErrors()
                                .stream()
                                .map(x -> x.getDefaultMessage())
                                .collect(Collectors.joining(","));
            throw new HttpMessageNotReadableException(msg);
        }
        
        logger.info("Updating Radio: " + radio.toString());
        
        try {
            radioService.atualizar(radio);
        } catch (Exception ex) {
            String errorMessage = "Error updating radio. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Radio updated: {mId = " + radio.getId() + ", ...}");
        
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(value = "/radios/{id:[\\d]+}/avaliacao/positivar", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Radio> positivarRadio(
            @PathVariable("id") Integer idRadio, @Valid @RequestBody Radio radio, Errors errors) throws BigAntenaException {
        
        if (errors.hasErrors()) {
            String msg = errors.getAllErrors()
                                .stream()
                                .map(x -> x.getDefaultMessage())
                                .collect(Collectors.joining(","));
            throw new HttpMessageNotReadableException(msg);
        }
        
        Radio currentRadio = radioService.buscarPorId(idRadio);
        
        if (currentRadio == null) {
            logger.error(String.format("Unable to update. Radio with id %d not found.", idRadio));
            return new ResponseEntity(new BigAntenaException("Unable to upate. Radio with id " + idRadio + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        
        currentRadio.positivar();
        logger.info("Saving positive evaluation for Radio: " + radio.toString());
        try {
            radioService.atualizar(currentRadio);
        } catch (Exception ex) {
            String errorMessage = "Error updating radio. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Radio updated: {mId = " + idRadio + ", ...}");

        radio.setQtdAvaliacoes(currentRadio.getQtdAvaliacoes());
        return new ResponseEntity<>(radio, HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @RequestMapping(value = "/radios/{id:[\\d]+}/avaliacao/negativar", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Radio> negativarRadio(
            @PathVariable("id") Integer idRadio, @Valid @RequestBody Radio radio, Errors errors) throws BigAntenaException {
        
        if (errors.hasErrors()) {
            String msg = errors.getAllErrors()
                                .stream()
                                .map(x -> x.getDefaultMessage())
                                .collect(Collectors.joining(","));
            throw new HttpMessageNotReadableException(msg);
        }
        
        Radio currentRadio = radioService.buscarPorId(idRadio);
        
        if (currentRadio == null) {
            logger.error(String.format("Unable to update. Radio with id %d not found.", idRadio));
            return new ResponseEntity(new BigAntenaException("Unable to upate. Radio with id " + idRadio + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        
        currentRadio.negativar();
        logger.info("Saving negative evaluation for Radio: " + radio.toString());
        try {
            radioService.atualizar(currentRadio);
        } catch (Exception ex) {
            String errorMessage = "Error updating radio. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Radio updated: {mId = " + idRadio + ", ...}");

        radio.setQtdAvaliacoes(currentRadio.getQtdAvaliacoes());
        return new ResponseEntity<>(radio, HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
//    @RequestMapping(value = "/radios/{id:[\\d]+}", method = RequestMethod.DELETE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public  void removeRadio(@PathVariable("id") Integer id) {
//            radioService.remover(id);
//    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/radios/cidades", method = RequestMethod.GET)
    public List<Radio> getRadiosPorCidade(
            @RequestParam(value = "id-cidade", required = false) Integer idCidade,
            @RequestParam(value = "nome-cidade", required = false) String nomeCidade) throws BigAntenaException 
    {
        List<Radio> radios = null;
        Cidade cidade = new Cidade(idCidade);
        cidade.setNome(nomeCidade);
        
        try {
            radios = radioService.buscarPorCidade(cidade);
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarPorCidade. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((radios == null) ? "0" : radios.size()));
        return radios;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/cidades/{idCidade:[\\d]+}/generos/{idsGeneros}/radios", method = RequestMethod.GET)
    public List<Radio> getRadiosPorCidadeEGeneros(
            @PathVariable("idCidade") Integer idCidade,
            @PathVariable("idsGeneros") List<Integer> idsGeneros) throws BigAntenaException 
    {
        List<Radio> radios = null;
        Cidade cidade = new Cidade(idCidade);
        
        try {
            radios = radioService.buscarPorCidadeEGeneros(cidade, idsGeneros);
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarPorCidadeEGeneros. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((radios == null) ? "0" : radios.size()));
        return radios;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/radios/estados", method = RequestMethod.GET)
    public List<Radio> getRadiosPorEstado(
            @RequestParam(value = "id-estado", required = false) Integer idEstado,
            @RequestParam(value = "nome-estado", required = false) String nomeEstado) throws BigAntenaException 
    {
        List<Radio> radios = null;
        Estado estado = new Estado(idEstado);
        estado.setNome(nomeEstado);
        
        try {
            radios = radioService.buscarPorEstado(estado);
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarPorEstado. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((radios == null) ? "0" : radios.size()));
        return radios;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/radios/paises", method = RequestMethod.GET)
    public List<Radio> getRadiosPorPais(
            @RequestParam(value = "id-pais", required = false) Integer idPais,
            @RequestParam(value = "nome-pais", required = false) String nomePais) throws BigAntenaException 
    {
        List<Radio> radios = null;
        Pais pais = new Pais(idPais);
        pais.setNome(nomePais);
        
        try {
            radios = radioService.buscarPorPais(pais);
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarPorPais. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((radios == null) ? "0" : radios.size()));
        return radios;
    }
    
    @JsonView(View.Minima.class)
    @RequestMapping(value = "/radios/generos", method = RequestMethod.GET)
    public List<Radio> getRadiosPorGenero(
            @RequestParam(value = "id-genero", required = false) Integer idGenero,
            @RequestParam(value = "nome-genero", required = false) String nomeGenero) throws BigAntenaException 
    {
        List<Radio> radios = null;
        Genero genero = new Genero(idGenero);
        genero.setNome(nomeGenero);
        
        try {
            radios = radioService.buscarPorGenero(genero);
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarPorGenero. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((radios == null) ? "0" : radios.size()));
        return radios;
    }
    
    @JsonView(View.MinimaComLogo.class)
    @RequestMapping(value = "/radios/mais-acessadas", method = RequestMethod.GET)
    public List<Radio> getRadiosMaisAcessadas(
            @RequestParam(value = "id-genero", required = false) Integer idGenero,
            @RequestParam(value = "nome-genero", required = false) String nomeGenero,
            @RequestParam(value = "dt-inicial") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date dtInicial,
            @RequestParam(value = "dt-final") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date dtFinal,
            @RequestParam(value = "limite", required = false) Integer limite) throws BigAntenaException
    {
        List<Radio> radios = null;
        Genero genero = new Genero(idGenero);
        genero.setNome(nomeGenero);
        
        try {
            radios = radioService.buscarMaisAcessadas(genero, dtInicial, dtFinal, limite);
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarMaisAcessadasPorGenero. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((radios == null) ? "0" : radios.size()));
        return radios;
    }
    
    @JsonView(View.MinimaComLogo.class)
    @RequestMapping(value = "/radios/palavras-chave", method = RequestMethod.GET)
    public List<Radio> getRadios(
            @RequestParam(value = "q") String palavrasChave,
            @RequestParam(value = "pagina", required = false) Integer pagina,
            @RequestParam(value = "limite", required = false) Integer limite
    
    ) throws BigAntenaException {

        List<Radio> radios = null;

        if (StringUtils.isBlank(palavrasChave)) {
            throw new BigAntenaException("Empty radio keywords requested");
        }
        if (pagina == null || limite == null){
            pagina = 1;
            limite = 200;
        }
        try {
            radios = radioService.buscarPorPalavrasChave(palavrasChave, pagina, limite);
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarPorPalavrasChave. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }
        
        logger.info("Number of objects found: " + ((radios == null) ? "0" : radios.size()));
        return radios;
    }
    
    @JsonView(View.MinimaComLogo.class)
    @RequestMapping(value = "/radios", method = RequestMethod.GET)
    public List<Radio> getRadios(
            @RequestParam(value = "nome-radio", required = true) String nomeRadio,
            @RequestParam(value = "id-cidade", required = false) Integer idCidade,
            @RequestParam(value = "nome-cidade", required = false) String nomeCidade,
            @RequestParam(value = "id-estado", required = false) Integer idEstado,
            @RequestParam(value = "nome-estado", required = false) String nomeEstado,
            @RequestParam(value = "id-pais", required = false) Integer idPais, 
            @RequestParam(value = "nome-pais", required = false) String nomePais, 
            @RequestParam(value = "id-genero", required = false) Integer idGenero, 
            @RequestParam(value = "nome-genero", required = false) String nomeGenero) throws BigAntenaException {
        
        List<Radio> radios = null;
        if (StringUtils.isBlank(nomeRadio)) {
            throw new BigAntenaException("Empty radio name requested");
        }
        Radio rExample = new Radio();
        rExample.setNome(nomeRadio);
        
        if (idCidade != null || StringUtils.isNotBlank(nomeCidade)) {
            Cidade c = new Cidade(idCidade);
            c.setNome(nomeCidade);
            if (idEstado != null || StringUtils.isNotBlank(nomeEstado)) {
                Estado e = new Estado(idEstado);
                e.setNome(nomeEstado);
                c.setEstado(e);
                if (idPais != null || StringUtils.isNotBlank(nomePais)) {
                    Pais p = new Pais(idPais);
                    p.setNome(nomePais);
                    e.setPais(p);
                }
            }
            else if (idPais != null || StringUtils.isNotBlank(nomePais)) {
                Pais p = new Pais(idPais);
                p.setNome(nomePais);
                c.setPais(p);
            }
            rExample.setCidade(c);
        } 
        if (idGenero != null || StringUtils.isNotBlank(nomeGenero)) {
            Genero g = new Genero(idGenero);
            g.setNome(nomeGenero);
            rExample.setGeneros(new HashSet<>((List<Genero>)Arrays.asList(g)));
        }
        
        try {
            radios = radioService.buscarPorExemplo(rExample);
            
        } catch (Exception ex) {
            String errorMessage = "Error invoking radioService.buscarPorExemplo. [%1$s]";
            throw new BigAntenaException(String.format(errorMessage, ex.toString()));
        }

        logger.info("Number of objects found: " + ((radios == null) ? "0" : radios.size()));
        return radios;
    }
    
    // 500
    @ExceptionHandler(BigAntenaException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
