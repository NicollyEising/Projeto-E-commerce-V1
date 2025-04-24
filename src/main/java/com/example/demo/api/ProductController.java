package com.example.demo.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.bean.Product;
import com.example.demo.bean.Usuario;
import com.example.demo.dao.ProductRepository;
import com.example.demo.dao.UsuarioRepository;

@RestController
@RequestMapping("/produtos")
public class ProductController {

    @Autowired
    private ProductRepository produtoRepository;

    @Autowired
    private UsuarioRepository userRepository;

    @GetMapping("/api/todosProdutos")
    public ResponseEntity<List<Product>> getProdutos() {
        List<Product> produtos = (List<Product>) produtoRepository.findAll();

        if (produtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produtos);
    }

    @PostMapping("/api/criar")
    public ResponseEntity<Product> createProduto(@RequestBody Product produto) {
        Product novoProduto = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProdutoById(@PathVariable Long id) {
        Optional<Product> result = produtoRepository.findById(id);
        return result.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<Product> updateProduto(@PathVariable Long id, @RequestBody Product produtoAtualizado) {
        Optional<Product> optionalProduto = produtoRepository.findById(id);
        if (optionalProduto.isPresent()) {
            Product produto = optionalProduto.get();
            produto.setName(produtoAtualizado.getName());
            produto.setDescription(produtoAtualizado.getDescription());
            produto.setGenre(produtoAtualizado.getGenre());
            produto.setBrand(produtoAtualizado.getBrand());
            produto.setPrice(produtoAtualizado.getPrice());
            produto.setStock(produtoAtualizado.getStock());
            produto.setImageUrl(produtoAtualizado.getImageUrl());
            Product updatedProduto = produtoRepository.save(produto);
            return ResponseEntity.ok(updatedProduto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        Optional<Product> optionalProduto = produtoRepository.findById(id);
        if (optionalProduto.isPresent()) {
            Product produto = optionalProduto.get();

            // Verificar se o produto está na lista de desejos de algum usuário
            List<Usuario> usuariosComProdutoNaWishlist = userRepository.findByWishlistContaining(produto);
            List<Usuario> usuariosComProdutoNoCarrinho = userRepository.findByCartContaining(produto);

            // Remover o produto da lista de desejos de todos os usuários
            usuariosComProdutoNaWishlist.forEach(usuario -> {
                usuario.removeFromWishlist(produto);
                userRepository.save(usuario);
            });

            // Remover o produto do carrinho de compras de todos os usuários
            usuariosComProdutoNoCarrinho.forEach(usuario -> {
                usuario.removeFromCart(produto);
                userRepository.save(usuario);
            });

            // Agora pode excluir o produto, pois foi removido de todos os carrinhos de compras e wishlists
            produtoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllProdutos() {
        produtoRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/produtosPorPreco")
    public ResponseEntity<List<Product>> getProdutosPorFaixaDePreco(
            @RequestParam(value = "min", required = false) Double minPrice,
            @RequestParam(value = "max", required = false) Double maxPrice,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "marcas", required = false) List<String> marcas) {

        List<Product> produtos;

        if (minPrice != null && maxPrice != null && marcas != null && !marcas.isEmpty() && genre != null) {
            produtos = produtoRepository.findByPriceBetweenAndBrandInAndGenre(minPrice, maxPrice, marcas, genre);
        } else if (minPrice != null && maxPrice != null && marcas != null && !marcas.isEmpty()) {
            produtos = produtoRepository.findByPriceBetweenAndBrandIn(minPrice, maxPrice, marcas);
        } else if (minPrice != null && marcas != null && !marcas.isEmpty() && genre != null) {
            produtos = produtoRepository.findByPriceLessThanEqualAndBrandInAndGenre(minPrice, marcas, genre);
        } else if (minPrice != null && maxPrice != null && genre != null) {
            produtos = produtoRepository.findByPriceBetweenAndGenre(minPrice, maxPrice, genre);
        } else if (minPrice != null && genre != null) {
            produtos = produtoRepository.findByPriceGreaterThanEqualAndGenre(minPrice, genre);
        } else if (maxPrice != null && genre != null) {
            produtos = produtoRepository.findByPriceGreaterThanEqualAndGenre(maxPrice, genre);
        } else if (marcas != null && !marcas.isEmpty() && genre != null) {
            produtos = produtoRepository.findByBrandInAndGenre(marcas, genre);
        } else if (genre != null) {
            produtos = produtoRepository.findByGenre(genre);
        } else if (minPrice != null && marcas != null && !marcas.isEmpty()) {
            produtos = produtoRepository.findByPriceLessThanEqualAndBrandIn(minPrice, marcas);
        } else if (minPrice != null && maxPrice != null) {
            produtos = produtoRepository.findByPriceBetween(minPrice, maxPrice);
        } else if (minPrice != null) {
            produtos = produtoRepository.findByPriceGreaterThanEqual(minPrice);
        } else if (maxPrice != null) {
            produtos = produtoRepository.findByPriceLessThanEqual(maxPrice);
        } else if (marcas != null && !marcas.isEmpty()) {
            produtos = produtoRepository.findByBrandIn(marcas);
        } else {
            produtos = (List<Product>) produtoRepository.findAll();
        }

        return produtos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(produtos);
    }

    @GetMapping("/api/produtosPorMarca")
    public ResponseEntity<List<Product>> getProdutosPorMarca(@RequestParam(value = "marca", required = false) String marca) {
        List<Product> produtos = (marca != null && !marca.isEmpty()) ? produtoRepository.findByBrand(marca) : (List<Product>) produtoRepository.findAll();
        return produtos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(produtos);
    }

    @GetMapping("/api/produtosPorGenero")
    public ResponseEntity<List<Product>> getProdutosPorGenero(@RequestParam(value = "genre", required = false) String genre) {
        List<Product> produtos = produtoRepository.findByGenre(genre);
        return produtos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(produtos);
    }

    @GetMapping("/produtosPorNome")
    public ResponseEntity<List<Product>> getProdutosPorNome(@RequestParam(value = "Name") String name) {
        List<Product> produtos = produtoRepository.findByNameContaining(name);
        return produtos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(produtos);
    }

    @GetMapping("/api/getTodosFiltros")
    public ResponseEntity<List<Product>> getProdutosPorFiltros(
            @RequestParam(value = "min", required = false) Double minPrice,
            @RequestParam(value = "max", required = false) Double maxPrice,
            @RequestParam(value = "marcas", required = false) List<String> marcas,
            @RequestParam(value = "genre", required = false) String genre) {

        List<Product> produtos = produtoRepository.findByPriceBetweenAndBrandInAndGenre(minPrice, maxPrice, marcas, genre);

        return produtos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(produtos);
    }
}
