package com.fooddeliverysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fooddeliverysystem.entity.Rating;

@Repository
public interface RatingDao extends JpaRepository<Rating, Integer> {
	Rating findFirstByOrderByRatingIdDesc();
}
