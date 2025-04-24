package com.example.demo.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.bean.Usuario;
import com.example.demo.dao.UsuarioRepository;

@Configuration
public class LoadDataBase {
    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                Usuario user1 = new Usuario();
                user1.setId(1L);
                user1.setUsername("Nicolly");
                user1.setEmail("nicolly@example.com");
                user1.setEndereco("123 Main St");
                user1.setPassword("senha");
                user1.setNascimento(LocalDate.of(2002, 11, 16));
                user1.setAdm("y");
                repository.save(user1);

                Usuario user2 = new Usuario();
                user2.setId(2L);
                user2.setUsername("Jane Smith");
                user2.setEmail("jane.smith@example.com");
                user2.setEndereco("456 Elm St");
                user2.setPassword("senha");
                user1.setNascimento(LocalDate.of(2002, 11, 16));
                user2.setAdm("y");
                repository.save(user2);
                
            }
        };
    }

    
    
    
    
}
