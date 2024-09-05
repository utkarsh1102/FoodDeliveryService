package com.fooddeliverysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fooddeliverysystem.entity.Coupon;

@Repository
public interface CouponDao extends JpaRepository<Coupon, Integer>{
	
}
