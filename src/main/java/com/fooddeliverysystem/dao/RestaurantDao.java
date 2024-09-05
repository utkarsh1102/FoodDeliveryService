package com.fooddeliverysystem.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fooddeliverysystem.entity.Restaurant;

@Repository
public interface RestaurantDao extends JpaRepository<Restaurant, Integer>{
	Restaurant findFirstByOrderByRestaurantIdDesc();
}
