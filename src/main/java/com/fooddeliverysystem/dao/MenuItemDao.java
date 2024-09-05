package com.fooddeliverysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fooddeliverysystem.entity.MenuItem;

@Repository
public interface MenuItemDao extends JpaRepository<MenuItem, Integer> {
	MenuItem findFirstByOrderByItemIdDesc();
}
