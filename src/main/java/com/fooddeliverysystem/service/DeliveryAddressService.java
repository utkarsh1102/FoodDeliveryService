package com.fooddeliverysystem.service;

import com.fooddeliverysystem.entity.DeliveryAddress;

/**
 * Service interface for managing delivery addresses. Provides methods for
 * saving and retrieving delivery addresses.
 */
public interface DeliveryAddressService {

	/**
	 * Saves a delivery address to the database.
	 *
	 * This method persists the given DeliveryAddress object to the database. If the
	 * address already exists, it will be updated; otherwise, a new record will be
	 * created.
	 *
	 * @param deliveryAddress The DeliveryAddress object to be saved.
	 * @return The saved DeliveryAddress object, which includes any updates or new
	 *         database-generated fields.
	 */
	DeliveryAddress save(DeliveryAddress deliveryAddress);

	/**
	 * Finds a delivery address by its unique ID.
	 *
	 * This method retrieves a DeliveryAddress object from the database using its
	 * unique ID. If no address is found with the provided ID, the method will
	 * return null.
	 *
	 * @param id The unique ID of the delivery address to be retrieved.
	 * @return The DeliveryAddress object if found, otherwise null.
	 */
	DeliveryAddress findById(int id);
}
