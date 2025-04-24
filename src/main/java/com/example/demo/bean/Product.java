	package com.example.demo.bean;
	
	import jakarta.persistence.Entity;


import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
	
	@Entity
	public class Product {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    private String Name;
	    private String description;
	    private String brand;
	    private String imageUrl;
		private double price;
	    private int stock;
	    private String genre;
	
	    
	    
	    /////////////
	    @JsonIgnore
	    @ManyToMany(mappedBy = "wishlist")
	    private Set<Usuario> usuarios = new HashSet<>();

	    public Set<Usuario> getUsuarios() {
	        return usuarios;
	    }


	    public void setUsuarios(Set<Usuario> usuarios) {
	        this.usuarios = usuarios;
	    }
	    
	    //////////////////
	    

	    public String getBrand() {
	        return brand;
	    }

	    public void setBrand(String brand) {
	        this.brand = brand;
	    }
	    public Long getId() {
	        return id;
	    }
	
	    public void setId(Long id) {
	        this.id = id;
	    }
	
	    public String getName() {
	        return Name;
	    }
	
	    public void setName(String Name) {
	        this.Name = Name;
	    }
	    
	    public String getDescription() {
	 		return description;
	 	}
	
	 	public void setDescription(String description) {
	 		this.description = description;
	 	}
	
	 	public double getPrice() {
	 		return price;
	 	}
	
	 	public void setPrice(double price) {
	 		this.price = price;
	 	}
	
	 	public int getStock() {
	 		return stock;
	 	}
	
	 	public void setStock(int stock) {
	 		this.stock = stock;
	 	}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}


		public String getGenre() {
			return genre;
		}


		public void setGenre(String genre) {
			this.genre = genre;
		}
	
	}
