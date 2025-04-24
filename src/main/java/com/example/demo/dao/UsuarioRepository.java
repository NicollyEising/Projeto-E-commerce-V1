package com.example.demo.dao;

import java.util.List;
import java.util.Optional;


import org.springframework.data.repository.CrudRepository;


import org.springframework.stereotype.Repository;

import com.example.demo.bean.Product;
import com.example.demo.bean.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmailOrUsername(String email, String username);
    
    Optional<Usuario> findById(Long id);
    
    List<Usuario> findByWishlistContaining(Product product);
    
    List<Usuario> findByCartContaining(Product product);
    
    
    
}
