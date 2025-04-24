package com.example.demo;

import com.example.demo.bean.Product;
import com.example.demo.dao.UsuarioRepository;
import com.example.demo.service.ProductService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.example.demo.bean.Product;
import com.example.demo.bean.Usuario;

@Component
public class ExemploSalvandoProduto {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private UsuarioRepository UsuarioRepository;

    @PostConstruct
    public void init() {
        criarSapatos();
    }

    public void criarSapatos() {
        // Exemplo de criação de múltiplos sapatos
        String[] nomes = {"AstroStride", "Quantum Quicks", "HyperHops", "Nebula Sneakers", "Stellar Sprints", "Eclipse Edge", "Cosmic Cruisers", "Phantom Flyers", "Zenith Zoomers", "Pulsar Pace"};
        String[] imageUrl  = {"https://cdnv2.moovin.com.br/chinellus/imagens/produtos/det/sandalia-fem-rasteira-havaianas-you-rio-4146078-306e016f44266e580d93e9dcf57c5647.png",
        		"https://static.netshoes.com.br/bnn/l_netshoes/2023-11-09/8283_running-black-friday-netshoes.png",
        		"https://cdnv2.moovin.com.br/chinellus/imagens/produtos/det/sandalia-fem-rasteira-havaianas-you-rio-4146078-306e016f44266e580d93e9dcf57c5647.png",
        		"https://static.netshoes.com.br/bnn/l_netshoes/2023-11-09/8283_running-black-friday-netshoes.png", 
        		"https://cdnv2.moovin.com.br/chinellus/imagens/produtos/det/sandalia-fem-rasteira-havaianas-you-rio-4146078-306e016f44266e580d93e9dcf57c5647.png", 
        		"https://static.netshoes.com.br/bnn/l_netshoes/2023-11-09/8283_running-black-friday-netshoes.png", 
        		"https://cdnv2.moovin.com.br/chinellus/imagens/produtos/det/sandalia-fem-rasteira-havaianas-you-rio-4146078-306e016f44266e580d93e9dcf57c5647.png", 
        		"https://static.netshoes.com.br/bnn/l_netshoes/2023-11-09/8283_running-black-friday-netshoes.png", 
        		"https://cdnv2.moovin.com.br/chinellus/imagens/produtos/det/sandalia-fem-rasteira-havaianas-you-rio-4146078-306e016f44266e580d93e9dcf57c5647.png",
        		"https://static.netshoes.com.br/bnn/l_netshoes/2023-11-09/8283_running-black-friday-netshoes.png"};
        double[] precos = {89, 99, 39, 95, 85, 105, 92, 98, 110, 308};
        String [] genre = {"F", "M", "F", "M", "F", "M", "F", "M", "F", "M"};
        String[] descricoes = {
                "Casuais com design inspirado no espaço sideral, garantindo estilo e conforto.",
                "Casuais com design inspirado no espaço sideral, garantindo estilo e conforto.",
                "Casuais com design inspirado no espaço sideral, garantindo estilo e conforto.",
                "Casuais com design inspirado no espaço sideral, garantindo estilo e conforto.",
                "Casuais com design inspirado no espaço sideral, garantindo estilo e conforto.",
                "Casuais com design inspirado no espaço sideral, garantindo estilo e conforto.",
                "Casuais com design inspirado no espaço sideral, garantindo estilo e conforto.",
                "Casuais com design inspirado no espaço sideral, garantindo estilo e conforto.",
                "Casuais com design inspirado no espaço sideral, garantindo estilo e conforto.",
                "Casuais com design inspirado no espaço sideral, garantindo estilo e conforto.",
                };
        int[] stocks = {150, 200, 180, 220, 190, 170, 210, 195, 185, 175};
        String[] marcas = {"Nike", "Adidas", "Puma", "Puma", "Puma", "Adidas", "Adidas", "Nike", "Nike", "Nike"};

        for (int i = 0; i < nomes.length; i++) {
            Product produto = new Product();
            produto.setName(nomes[i]);
            produto.setPrice(precos[i]);
            produto.setGenre(genre[i]);
            produto.setDescription(descricoes[i]);
            produto.setStock(stocks[i]);
            produto.setBrand(marcas[i]);
            produto.setImageUrl(imageUrl[i]);
            System.out.println("Criando produto: " + produto);

            productService.saveProduct(produto);
        }
    }
    

}
