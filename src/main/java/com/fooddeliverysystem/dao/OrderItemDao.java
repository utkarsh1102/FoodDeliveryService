package com.fooddeliverysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fooddeliverysystem.entity.OrderItem;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {
	OrderItem findFirstByOrderByOrderItemIdDesc();
}
