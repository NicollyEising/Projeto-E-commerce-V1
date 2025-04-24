package com.example.demo.dao;


import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.bean.OrderTable;


@Repository
public interface OrderRepository extends CrudRepository<OrderTable, Long> {
}
