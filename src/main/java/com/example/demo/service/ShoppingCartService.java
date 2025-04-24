package com.example.demo.service;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Product;
import com.example.demo.bean.ShoppingCart;
import com.example.demo.dao.ShoppingCartRepository;



@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public ShoppingCart getShoppingCart(Long id) {
        return shoppingCartRepository.findById(id).orElse(null);
    }

    public ShoppingCart createShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

 // Dentro do método addProductToCart da classe ShoppingCartService
    public void addProductToCart(Long cartId, Product product) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        List<Product> products = cart.getProducts();
        products.add(product);
        cart.setProducts(products);

        double total = cart.getTotal() + product.getPrice();
        cart.setTotal(total);

        shoppingCartRepository.save(cart);
    }
    
    



    public void removeProductFromCart(Long cartId, Long productId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElse(null);
        if (cart != null) {
            List<Product> products = cart.getProducts();
            // Remove o produto com o ID fornecido
            products = products.stream().filter(p -> !p.getId().equals(productId)).collect(Collectors.toList());
            cart.setProducts(products);
            // Atualiza o total (calcula o total novamente com base nos produtos restantes)
            double total = products.stream().mapToDouble(Product::getPrice).sum();
            cart.setTotal(total);
            shoppingCartRepository.save(cart);
        }
    }


    public ShoppingCart getShoppingCartById(Long id) {
        return shoppingCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrinho de compras não encontrado com id: " + id));
    }

}
