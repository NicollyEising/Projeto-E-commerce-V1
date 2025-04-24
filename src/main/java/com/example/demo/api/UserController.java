package com.example.demo.api;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.Product;
import com.example.demo.bean.Usuario;
import com.example.demo.dao.UsuarioRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

import java.util.Map;
import java.util.HashMap;
@RestController
@Order(0)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsuarioRepository userRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsers(Model model) {
        List<Usuario> users = (List<Usuario>) userRepository.findAll();
        if (users == null || users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<Usuario> createUser(@RequestBody Usuario user) {
    	Usuario newUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("getUserById/{id}")
    public Usuario getUserById(@PathVariable Long id) {
        Optional<Usuario> result = userRepository.findById(id);

        return result.get();
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<Usuario> updateUser(@PathVariable Long id, @RequestBody Usuario updatedUser) {
        System.out.println("Received request to update user with ID: " + id);
        
        Optional<Usuario> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();
            System.out.println("User found: " + user);

            // Atualize os campos relevantes do usuário
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setEndereco(updatedUser.getEndereco());
            user.setTelefone(updatedUser.getTelefone());
            user.setSexo(updatedUser.getSexo());
            user.setNascimento(updatedUser.getNascimento());

            Usuario savedUser = userRepository.save(user);
            System.out.println("User updated: " + savedUser);
            return ResponseEntity.ok(savedUser);
        } else {
            System.out.println("User not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<Usuario> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/deleteAllUser")
    public ResponseEntity<Void> deleteAllUsers() {
    	userService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping(value = "/login")
    public ResponseEntity loginUsuario(@RequestBody Usuario usuarioRequest){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        
        System.out.println("Usuário recebido: " + usuarioRequest.getUsername());
        System.out.println("Senha recebida: " + usuarioRequest.getPassword());

        try {
            // Primeiro, tente encontrar o usuário pelo email
            Optional<Usuario> usuario = userRepository.findByEmail(usuarioRequest.getUsername());

            if (!usuario.isPresent()) {
                // Se não encontrar pelo email, então tenta encontrar pelo username
                usuario = userRepository.findByUsername(usuarioRequest.getUsername());
            }

            if (usuario.isPresent()) {
                // Verifique se a senha corresponde
                if (usuario.get().getPassword().equals(usuarioRequest.getPassword())) {
                    Long userId = usuario.get().getId();
                    userService.setLoggedInUserId(userId);

                    return ResponseEntity.ok().headers(headers).build();
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).build();
                }
            } else {
                return ResponseEntity.notFound().headers(headers).build();
            }

        } catch (Exception e) {
            throw e;
        }
    }
    
    //metodo para verificar qual id está logado
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        Long loggedInUserId = userService.getLoggedInUserId();
        if (loggedInUserId != null) {
            System.out.println(loggedInUserId);
            return ResponseEntity.ok(loggedInUserId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não logado.");
        }
    }

 

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody Usuario novoUsuario, BindingResult result) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");

        try {
            // Verifica erros de validação
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body("Erro de validação: " + result.getAllErrors());
            }

            // Verifica se já existe um usuário com o mesmo email ou nome de usuário
            Optional<Usuario> usuarioExistente = userRepository.findByEmailOrUsername(novoUsuario.getEmail(), novoUsuario.getUsername());

            // Se o usuário já existe, retornar erro de conflito
            if (usuarioExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).headers(headers).build();
            }

            // Caso contrário, salva o novo usuário no banco de dados
            Usuario usuarioSalvo = userRepository.save(novoUsuario);

            // Retorna sucesso com o usuário criado
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(usuarioSalvo);

        } catch (Exception e) {
            throw e;
        }
    


}


    
    @PostMapping("/{userId}/wishlist/add/{productId}")
    public ResponseEntity<Product> addToWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        Optional<Usuario> optionalUsuario = userRepository.findById(userId);
        Optional<Product> optionalProduct = productService.getProductById(productId);

        if (optionalUsuario.isPresent() && optionalProduct.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Product produto = optionalProduct.get();

            // Adicionar o produto à lista de desejos do usuário
            usuario.addToWishlist(produto); // Implemente este método na entidade Usuario para adicionar o produto à lista de desejos

            userRepository.save(usuario); // Salvar o usuário com o produto adicionado à lista de desejos

            return ResponseEntity.ok(produto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @GetMapping("/wishlist/{userId}")
    public ResponseEntity<Set<Product>> getWishlist(@PathVariable Long userId) {
        Optional<Usuario> optionalUsuario = userRepository.findById(userId);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Set<Product> wishlist = usuario.getWishlist();

            return ResponseEntity.ok(wishlist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Remover produto da lista de desejos de um usuário específico
    @DeleteMapping("/{userId}/wishlist/remove/{productId}")
    public ResponseEntity<Product> removeFromWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        Optional<Usuario> optionalUsuario = userRepository.findById(userId);
        Optional<Product> optionalProduct = productService.getProductById(productId);

        if (optionalUsuario.isPresent() && optionalProduct.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Product produto = optionalProduct.get();

            // Remover o produto da lista de desejos do usuário
            usuario.removeFromWishlist(produto); // Implemente este método na entidade Usuario para remover o produto da lista de desejos

            userRepository.save(usuario); // Salvar o usuário com o produto removido da lista de desejos

            return ResponseEntity.ok(produto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @GetMapping("/checkAdm/{userId}")
    public ResponseEntity<String> checkAdmVariable(@PathVariable Long userId) {
        Optional<Usuario> optionalUsuario = userRepository.findById(userId);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            String admSet = usuario.getAdm();
            
            // Verifica se admSet contém a palavra "y" (ignorando maiúsculas/minúsculas)
            if (admSet.toLowerCase().contains("y")) {
                return ResponseEntity.ok("y");
            } else {
                return ResponseEntity.ok("n"); // Retorna 'n' se não contém "y"
            }
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found se o usuário não for encontrado
        }}
    
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUsuario() {
        userService.logout(); // Chama o método de logout do UserService
        return ResponseEntity.noContent().build();
    }
    }
