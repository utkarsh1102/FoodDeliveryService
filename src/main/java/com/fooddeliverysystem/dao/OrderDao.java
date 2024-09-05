package com.fooddeliverysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fooddeliverysystem.entity.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
	Order findFirstByOrderByOrderIdDesc();
}
