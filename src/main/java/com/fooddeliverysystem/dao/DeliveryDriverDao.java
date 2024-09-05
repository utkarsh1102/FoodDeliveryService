package com.fooddeliverysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fooddeliverysystem.entity.DeliveryDriver;

public interface DeliveryDriverDao extends JpaRepository<DeliveryDriver, Integer> {

}
