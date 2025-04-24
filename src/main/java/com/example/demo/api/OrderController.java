package com.example.demo.api;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.bean.Customer;
import com.example.demo.bean.OrderTable;
import com.example.demo.dao.OrderRepository;
import com.example.demo.dao.CustomerRepository;

import com.example.demo.request.OrderRequest;
import com.example.demo.service.CustomerService;
import com.example.demo.service.OrderService;

import java.util.List;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Order(5)
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping
    public List<OrderTable> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    @GetMapping("/{id}")
    public OrderTable getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
    
    @PostMapping
    public ResponseEntity<OrderTable> createOrder(@RequestBody OrderTable order) {
        OrderTable createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}