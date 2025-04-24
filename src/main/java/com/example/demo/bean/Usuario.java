package com.example.demo.bean;

import java.util.Objects;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import  jakarta.persistence.JoinColumn;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.HashSet;
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String endereco;
    private LocalDate  nascimento;
    private String sexo;
    private int telefone;
    private String adm;

    
    
    public Set<Product> getWishlist() {
        return wishlist;
    }
    
    public void setWishlist(Set<Product> wishlist) {
        this.wishlist = wishlist;
    }
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "usuario_wishlist",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> wishlist = new HashSet<>();

    // Getter e Setter para wishlist



    public void addToWishlist(Product product) {
        wishlist.add(product);
        product.getUsuarios().add(this);
    }

    public void removeFromWishlist(Product product) {
        wishlist.remove(product);
        product.getUsuarios().remove(this);
    }
    
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(getPassword(), usuario.getPassword());
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    
    @ManyToMany
    @JoinTable(
        name = "usuario_carrinho",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private Set<Product> cart = new HashSet<>();

    // Métodos para adicionar, remover e obter produtos no carrinho
    public void addToCart(Product product) {
        this.cart.add(product);
    }

    public void removeFromCart(Product product) {
        this.cart.remove(product);
    }

    public Set<Product> getCart() {
        return this.cart;
    }
    // Getters and Setters

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		this.nascimento = nascimento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public int getTelefone() {
		return telefone;
	}

	public void setTelefone(int telefone) {
		this.telefone = telefone;
	}

	public String getAdm() {
		return adm;
	}

	public void setAdm(String adm) {
		this.adm = adm;
	}

	public void setCart(Set<Product> cart2) {
		// TODO Auto-generated method stub
		
	}
} 