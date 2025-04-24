package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Usuario;
import com.example.demo.dao.UsuarioRepository;


import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsuarioRepository userRepository;

    public Optional<Usuario> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Usuario saveUser(Usuario user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
    
    
    private Long loggedInUserId;

    public void setLoggedInUserId(Long userId) {
        this.loggedInUserId = userId;
    }

    public Long getLoggedInUserId() {
        return this.loggedInUserId;
    }

    public void logout() {
        this.loggedInUserId = null;
        // Limpar qualquer outra informação de sessão, se necessário
    }
    
    
    
    
}
