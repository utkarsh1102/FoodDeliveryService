package com.fooddeliverysystem.service;

import java.util.List;

import com.fooddeliverysystem.entity.Order;

/**
 * Interface for managing orders in the system.
 */
public interface OrderService {

	/**
	 * Retrieves a list of all orders in the system.
	 *
	 * @return A list of all Order objects.
	 */
	List<Order> findAll();

	/**
	 * Finds an order by its unique ID.
	 *
	 * This method attempts to retrieve an order from the system using the specified
	 * ID. If no order is found with the provided ID, the method will return null.
	 *
	 * @param id The unique ID of the order to be retrieved.
	 * @return An Order object if found, otherwise null.
	 */
	Order findById(int id);

	/**
	 * Retrieves the most recently added order from the system.
	 *
	 * This method queries the system to find the order with the highest ID, which
	 * represents the most recently added order. It will return null if no orders
	 * are found.
	 *
	 * @return The most recently added Order object, or null if no orders are found.
	 */
	Order findLastRecord();

	/**
	 * Saves an order to the system.
	 *
	 * This method persists the given Order object to the system. If the order
	 * already exists, it will be updated; otherwise, a new order record will be
	 * created.
	 *
	 * @param order The Order object to be saved.
	 * @return The saved Order object, which includes any updates or new
	 *         system-generated fields.
	 */
	Order save(Order order);

	/**
	 * Deletes an order by its unique ID.
	 *
	 * This method removes the order identified by the given ID from the system. If
	 * no order is found with the provided ID, the method may throw an exception.
	 *
	 * @param id The unique ID of the order to be deleted.
	 */
	void deleteById(int id);
}
