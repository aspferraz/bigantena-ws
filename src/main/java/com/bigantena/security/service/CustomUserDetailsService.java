/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.security.service;

import com.bigantena.model.Usuario;
import com.bigantena.security.CustomUserDetails;
import com.bigantena.service.UsuarioRegraService;
import com.bigantena.service.UsuarioService;
import java.util.List;
import org.apache.log4j.Logger;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
 

 
/**
 *
 * @author aspferraz
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired    
    private UsuarioService usuarioService;
    @Autowired    
    private UsuarioRegraService usuarioRegraService;    
    
    private Logger logger = Logger.getLogger(CustomUserDetailsService.class);
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername(username: " + username +")");
        Usuario user = usuarioService.obterPorLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            List<String> userRoles = usuarioRegraService.obterRegrasPorUsuario(user);
            return new CustomUserDetails(user, userRoles);
        }
    }
    
}
