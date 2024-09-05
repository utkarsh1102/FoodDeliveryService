package com.fooddeliverysystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddeliverysystem.dao.CouponDao;
import com.fooddeliverysystem.entity.Coupon;

@Service
public class CouponServiceImpl implements CouponService {

	private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);
	private CouponDao couponDao;

	@Autowired
	public CouponServiceImpl(CouponDao couponDao) {
		super();
		this.couponDao = couponDao;
	}

	/**
	 * Find a coupon by its ID.
	 * 
	 * @param id The ID of the coupon to find.
	 * @return The coupon with the specified ID, or null if no coupon is found.
	 */
	public Coupon findById(int id) {
		logger.info("Attempting to find coupon with ID: {}", id);

		Coupon coupon = couponDao.findById(id).orElse(null);

		if (coupon == null) {
			logger.warn("No coupon found with ID: {}", id);
		} else {
			logger.info("Coupon with ID {} found", id);
		}

		return coupon;
	}

	/**
	 * Save a coupon to the database.
	 * 
	 * @param coupon The coupon object to be saved.
	 * @return The saved coupon object with updated details (e.g., generated ID).
	 */
	public Coupon save(Coupon coupon) {
		logger.info("Attempting to save coupon: {}", coupon);

		// Save the coupon and return the saved entity
		Coupon savedCoupon = couponDao.save(coupon);

		logger.info("Coupon saved successfully with ID: {}", savedCoupon.getCouponId());

		return savedCoupon;
	}

	/**
	 * Retrieve all coupons from the database.
	 * 
	 * @return A list of all coupons.
	 */
	@Override
	public List<Coupon> findAll() {
		logger.info("Fetching all coupons from the database.");

		// Retrieve the list of all coupons
		List<Coupon> coupons = couponDao.findAll();

		logger.info("Number of coupons fetched: {}", coupons.size());

		return coupons;
	}
}
