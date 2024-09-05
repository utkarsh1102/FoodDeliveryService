package com.fooddeliverysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fooddeliverysystem.entity.Customer;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Integer> {
	Customer findFirstByOrderByCustomerIdDesc();
}
