package com.fooddeliverysystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddeliverysystem.dao.DeliveryAddressDao;
import com.fooddeliverysystem.entity.DeliveryAddress;

@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryAddressServiceImpl.class);
	private DeliveryAddressDao deliveryAddressDao;

	@Autowired
	public DeliveryAddressServiceImpl(DeliveryAddressDao deliveryAddressDao) {
		super();
		this.deliveryAddressDao = deliveryAddressDao;
	}

	/**
	 * Save a delivery address to the database.
	 * 
	 * @param deliveryAddress The delivery address object to be saved.
	 * @return The saved delivery address object, which may include updated details
	 *         (e.g., generated ID).
	 */
	@Override
	public DeliveryAddress save(DeliveryAddress deliveryAddress) {
		logger.info("Attempting to save delivery address: {}", deliveryAddress);

		// Save the delivery address to the database
		DeliveryAddress savedAddress = deliveryAddressDao.save(deliveryAddress);

		// Log the successful save operation
		logger.info("Delivery address saved successfully with ID: {}", savedAddress.getAddressId());

		// Return the saved delivery address
		return savedAddress;
	}

	/**
	 * Retrieve a delivery address from the database by its ID.
	 * 
	 * @param id The ID of the delivery address to retrieve.
	 * @return The delivery address with the specified ID, or null if no such
	 *         address exists.
	 */
	@Override
	public DeliveryAddress findById(int id) {
		logger.info("Fetching delivery address with ID: {}", id);

		// Retrieve the delivery address from the database
		DeliveryAddress deliveryAddress = deliveryAddressDao.findById(id).orElse(null);

		if (deliveryAddress == null) {
			logger.warn("No delivery address found with ID: {}", id);
		} else {
			logger.info("Delivery address found with ID: {}", id);
		}

		// Return the retrieved delivery address
		return deliveryAddress;
	}

}
