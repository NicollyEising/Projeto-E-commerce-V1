package com.example.demo.bean;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "orders") 
public class Orders {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    
    
    @ManyToMany
    private List<Product> products;

    // Outros campos da ordem, como informações do cliente, endereço de entrega, etc.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderTable orderTable;


    public void setProduct(Product product) {
        this.product = product;
    }
    
    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }
}
