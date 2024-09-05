package com.fooddeliverysystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddeliverysystem.dao.DeliveryDriverDao;
import com.fooddeliverysystem.entity.DeliveryDriver;

import jakarta.transaction.Transactional;

@Service
public class DeliveryDriverServiceImpl implements DeliveryDriverService {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryDriverServiceImpl.class);
	private DeliveryDriverDao deliveryDriverDao;

	@Autowired
	public DeliveryDriverServiceImpl(DeliveryDriverDao deliveryDriverDao) {
		super();
		this.deliveryDriverDao = deliveryDriverDao;
	}

	/**
	 * Retrieve a delivery driver from the database by their ID.
	 * 
	 * @param id The ID of the delivery driver to retrieve.
	 * @return The delivery driver with the specified ID, or null if no such driver
	 *         exists.
	 */
	@Override
	public DeliveryDriver findById(int id) {
		logger.info("Fetching delivery driver with ID: {}", id);

		DeliveryDriver deliveryDriver = deliveryDriverDao.findById(id).orElse(null);

		if (deliveryDriver == null) {
			logger.warn("No delivery driver found with ID: {}", id);
		} else {
			logger.info("Delivery driver found with ID: {}", id);
		}

		return deliveryDriver;
	}

	/**
	 * Retrieve all delivery drivers from the database.
	 * 
	 * @return A list of all delivery drivers.
	 */
	@Override
	public List<DeliveryDriver> findAll() {
		logger.info("Fetching all delivery drivers");

		List<DeliveryDriver> deliveryDrivers = deliveryDriverDao.findAll();

		logger.info("Number of delivery drivers fetched: {}", deliveryDrivers.size());

		return deliveryDrivers;
	}

	/**
	 * Save a delivery driver to the database.
	 * 
	 * @param deliveryDriver The delivery driver object to be saved.
	 * @return The saved delivery driver object, which may include updated details
	 *         (e.g., generated ID).
	 */
	@Transactional
	@Override
	public DeliveryDriver save(DeliveryDriver deliveryDriver) {
		logger.info("Attempting to save delivery driver: {}", deliveryDriver);

		DeliveryDriver savedDriver = deliveryDriverDao.save(deliveryDriver);

		logger.info("Delivery driver saved successfully with ID: {}", savedDriver.getDriverId());

		return savedDriver;
	}

}
