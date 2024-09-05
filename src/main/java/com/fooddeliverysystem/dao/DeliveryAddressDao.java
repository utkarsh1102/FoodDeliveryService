package com.fooddeliverysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fooddeliverysystem.entity.DeliveryAddress;

@Repository
public interface DeliveryAddressDao extends JpaRepository<DeliveryAddress, Integer>{
	
}
