package com.fooddeliverysystem.service;

import java.util.List;

import com.fooddeliverysystem.entity.DeliveryDriver;

/**
 * Service interface for managing delivery drivers.
 */
public interface DeliveryDriverService {

	/**
	 * Finds a delivery driver by their unique ID.
	 *
	 * This method retrieves a delivery driver from the database using the provided
	 * ID. If no delivery driver is found with the given ID, the method returns
	 * null.
	 *
	 * @param id The unique ID of the delivery driver to be retrieved.
	 * @return A DeliveryDriver object if found, otherwise null.
	 */
	DeliveryDriver findById(int id);

	/**
	 * Retrieves a list of all delivery drivers.
	 *
	 * This method fetches all delivery drivers from the database and returns them
	 * as a list. It may be used to populate lists or reports showing all available
	 * delivery drivers.
	 *
	 * @return A list of DeliveryDriver objects representing all delivery drivers in
	 *         the database.
	 */
	List<DeliveryDriver> findAll();

	/**
	 * Saves a delivery driver to the database.
	 *
	 * This method persists the given DeliveryDriver object. If the delivery driver
	 * already exists, it will be updated; otherwise, a new record will be created.
	 *
	 * @param deliveryDriver The DeliveryDriver object to be saved.
	 * @return The saved DeliveryDriver object, which includes any updates or new
	 *         database-generated fields.
	 */
	DeliveryDriver save(DeliveryDriver deliveryDriver);
}
