package com.fooddeliverysystem.service;

import java.util.List;

import com.fooddeliverysystem.entity.Coupon;

//This interface defines the contract for Coupon-related operations in the service layer
public interface CouponService {

	/**
	 * Retrieves a list of all coupons.
	 *
	 * @return A list of all Coupon objects.
	 */
	List<Coupon> findAll();

	/**
	 * Finds a specific coupon by its unique ID.
	 *
	 * @param id The unique ID of the coupon to be retrieved.
	 * @return A Coupon object if found, otherwise null.
	 */
	Coupon findById(int id);

	/**
	 * Saves a coupon to the database.
	 *
	 * This method either creates a new coupon or updates an existing one depending
	 * on whether the coupon already exists in the database.
	 *
	 * @param coupon The Coupon object to be saved.
	 * @return The saved Coupon object, including any updates or new
	 *         database-generated fields.
	 */
	Coupon save(Coupon coupon);
}
