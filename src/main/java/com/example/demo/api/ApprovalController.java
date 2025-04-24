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
import com.example.demo.paypal.PayPalIntegration;
import com.example.demo.service.ApprovalUrlService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
@RestController
@Order(0)
@RequestMapping("/paypal")
public class ApprovalController  {
    
	@Autowired
	private ApprovalUrlService approvalUrlService;
    
    @Autowired
    public ApprovalController(ApprovalUrlService approvalUrlService) {
        this.approvalUrlService = approvalUrlService;
    }
    
    @PostMapping("/processJsonResponse")
    public ResponseEntity<String> processJsonResponse(@RequestBody String jsonResponse) {
        // Processa o JSON e obtém a URL de aprovação
        String approvalUrl = null;
        // Código para obter a approvalUrl do JSON
        
        // Armazena a URL no serviço
        approvalUrlService.storeApprovalUrl(approvalUrl);
        
        return ResponseEntity.ok("URL de aprovação armazenada com sucesso");
    }
    
    
 

    @Autowired
    private UsuarioRepository userRepository;

   

    @PostMapping("/{userId}/criarOrdem")
    public ResponseEntity<String> createOrdemProduto(@PathVariable("userId") Long userId) {
        try {
            // Obter o usuário pelo ID
            Optional<Usuario> optionalUsuario = userRepository.findById(userId);

            if (optionalUsuario.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Usuario usuario = optionalUsuario.get();

            // Obter o carrinho de compras do usuário
            Set<Product> cart = usuario.getCart();

            // Calcular o valor total dos produtos no carrinho
            double totalValue = cart.stream()
                    .mapToDouble(Product::getPrice)
                    .sum();

            // Chamar o método createOrder com o currencyCode e value obtidos
            String orderId = PayPalIntegration.createOrder("USD", String.valueOf(totalValue));

            return ResponseEntity.status(HttpStatus.CREATED).body(orderId);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar a ordem");
        }
    }


    }

    
    

  



    
