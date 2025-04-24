package com.example.demo.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bean.OrderTable;
import com.example.demo.dao.OrderRepository;

import java.util.List;
import java.util.Optional;



import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<OrderTable> getAllOrders() {
        Iterable<OrderTable> iterable = orderRepository.findAll();
        List<OrderTable> orders = new ArrayList<>();
        for (OrderTable orderTable : iterable) {
            orders.add(orderTable);
        }
        return orders;
    }

    public OrderTable getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public OrderTable createOrder(OrderTable order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}