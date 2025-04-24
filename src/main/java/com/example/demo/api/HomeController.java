package com.example.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.bean.Product;
import com.example.demo.service.ProductService;
@Controller
public class HomeController {
	
    @Autowired
    private ProductService productService;
	@GetMapping("/")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		List<Product> produtos = productService.getAllProducts();
		model.addAttribute("produtos", produtos);
		return "index";
	}
	
	
	@GetMapping("/login")
	public String loginPage() {
	    return "login";
	}
	
	@GetMapping("/cadastro")
	public String cadastroPagina() {
	    return "cadastro";
	}
	
    @GetMapping("/index")
    public String pesquisa(@RequestParam(value = "Name", required = false) String Name) {
        // Aqui você pode processar a lógica de pesquisa por nome

        // Redirecionamento para index.html usando Thymeleaf
		return "index";
    }
    
    @GetMapping("/gerenciamentoProduto")
    public String gerenciamento() {
        // Aqui você pode processar a lógica de pesquisa por nome

        // Redirecionamento para index.html usando Thymeleaf
		return "gerenciamentoProduto";
    }
    
    @GetMapping("/edicaoProduto")
    public String edicaoProduto() {
        // Aqui você pode processar a lógica de pesquisa por nome

        // Redirecionamento para index.html usando Thymeleaf
		return "edicaoProduto";
    }
    
    @GetMapping("/detalhesProduto")
    public String detalhesProduto() {
        // Aqui você pode processar a lógica de pesquisa por nome

        // Redirecionamento para index.html usando Thymeleaf
		return "detalhesProduto";
    }
    @GetMapping("/listaDesejos")
    public String listaDesejos() {
        // Aqui você pode processar a lógica de pesquisa por nome

        // Redirecionamento para index.html usando Thymeleaf
		return "listaDesejos";
    }
    
    
    
    @GetMapping("/carrinhoDeCompras")
    public String carrinhoDeCompras() {
        // Aqui você pode processar a lógica de pesquisa por nome

        // Redirecionamento para index.html usando Thymeleaf
		return "carrinhoDeCompras";
    }

    
    @GetMapping("/adicionarProduto")
    public String adicionarProduto() {
        // Aqui você pode processar a lógica de pesquisa por nome

        // Redirecionamento para index.html usando Thymeleaf
		return "adicionarProduto";
    }
    
    @GetMapping("/perfil")
    public String perfil() {
        // Aqui você pode processar a lógica de pesquisa por nome

        // Redirecionamento para index.html usando Thymeleaf
		return "perfil";
    }
    
    @GetMapping("/edicaoPerfil")
    public String edicaoPerfil() {
        // Aqui você pode processar a lógica de pesquisa por nome

        // Redirecionamento para index.html usando Thymeleaf
		return "edicaoPerfil";
    }
}