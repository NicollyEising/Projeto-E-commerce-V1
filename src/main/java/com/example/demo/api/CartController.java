package com.example.demo.api;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.Product;
import com.example.demo.bean.ShoppingCart;
import com.example.demo.bean.Usuario;
import com.example.demo.dao.ShoppingCartRepository;
import com.example.demo.dao.UsuarioRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import java.util.Set;

@RestController
@Order(4)
@RequestMapping("/shopping-carts")
public class CartController {
    @Autowired
    private ProductService productService;
    


    @Autowired
    private UsuarioRepository userRepository;
    
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @GetMapping
    public ResponseEntity<List<ShoppingCart>> getShoppingCarts(Model model) {
        List<ShoppingCart> shoppingCarts = (List<ShoppingCart>) shoppingCartRepository.findAll();
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shoppingCarts);
    }

    @PostMapping
    public ResponseEntity<ShoppingCart> createShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        ShoppingCart newShoppingCart = shoppingCartRepository.save(shoppingCart);
        return ResponseEntity.status(HttpStatus.CREATED).body(newShoppingCart);
    }

    @GetMapping("/{id}")
    public ShoppingCart getShoppingCartById(@PathVariable Long id) {
        Optional<ShoppingCart> result = shoppingCartRepository.findById(id);
        if (result.isEmpty()) {
            throw new RuntimeException("Carrinho de compras não encontrado com id: " + id);
        }
        return result.get();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingCart> updateShoppingCart(@PathVariable Long id, @RequestBody ShoppingCart updatedShoppingCart) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(id);
        if (optionalShoppingCart.isPresent()) {
            ShoppingCart shoppingCart = optionalShoppingCart.get();
            // Atualize os campos relevantes do carrinho de compras
            ShoppingCart updatedCart = shoppingCartRepository.save(shoppingCart);
            return ResponseEntity.ok(updatedCart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long id) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(id);
        if (optionalShoppingCart.isPresent()) {
            shoppingCartRepository.delete(optionalShoppingCart.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    
    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<Product> addToCart(@PathVariable Long userId, @PathVariable Long productId) {
        Optional<Usuario> optionalUsuario = userRepository.findById(userId);
        Optional<Product> optionalProduct = productService.getProductById(productId);

        if (optionalUsuario.isPresent() && optionalProduct.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Product produto = optionalProduct.get();

            // Adicionar o produto ao carrinho do usuário
            usuario.addToCart(produto); // Implemente este método na entidade Usuario para adicionar o produto ao carrinho

            userRepository.save(usuario); // Salvar o usuário com o produto adicionado ao carrinho

            return ResponseEntity.ok(produto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("carrinhoPorUsuario/{userId}")
    public ResponseEntity<Set<Product>> getCart(@PathVariable Long userId) {
        Optional<Usuario> optionalUsuario = userRepository.findById(userId);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Set<Product> cart = usuario.getCart(); // Implemente este método na entidade Usuario para obter os produtos no carrinho

            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}/removerCarrinhoPorUsuario/{productId}")
    public ResponseEntity<Product> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        Optional<Usuario> optionalUsuario = userRepository.findById(userId);
        Optional<Product> optionalProduct = productService.getProductById(productId);

        if (optionalUsuario.isPresent() && optionalProduct.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Product produto = optionalProduct.get();

            // Remover o produto do carrinho do usuário
            usuario.removeFromCart(produto); // Implemente este método na entidade Usuario para remover o produto do carrinho

            userRepository.save(usuario); // Salvar o usuário com o produto removido do carrinho

            return ResponseEntity.ok(produto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{userId}/total")
    public ResponseEntity<Double> getCartTotal(@PathVariable Long userId) {
        System.out.println("Request received for user ID: " + userId); // Exemplo de log para debug

        Optional<Usuario> optionalUsuario = userRepository.findById(userId);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Set<Product> cart = usuario.getCart(); // Obter os produtos no carrinho do usuário

            // Calcular o valor total do carrinho
            double total = cart.stream()
                    .mapToDouble(Product::getPrice)
                    .sum();

            System.out.println("Total calculated for user ID " + userId + ": " + total); // Log do total calculado

            return ResponseEntity.ok(total);
        } else {
            System.out.println("User not found for user ID: " + userId); // Log se o usuário não for encontrado
            return ResponseEntity.notFound().build();
        }
    }



}
