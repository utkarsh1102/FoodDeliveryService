package com.fooddeliverysystem.service;

import com.fooddeliverysystem.entity.OrderItem;

/**
 * Service interface for handling operations related to OrderItems.
 */
public interface OrderItemService {

	/**
	 * Retrieves the most recently added OrderItem from the database.
	 *
	 * This method queries the database to find the OrderItem with the highest ID,
	 * which represents the most recently added item. The implementation should
	 * provide logic to fetch the record ordered by item ID in descending order.
	 *
	 * @return The most recently added OrderItem object, or null if no items are
	 *         found.
	 */
	OrderItem findLastRecord();

	/**
	 * Saves an OrderItem to the database.
	 *
	 * This method persists the given OrderItem object to the database. If the
	 * OrderItem already exists, it will be updated; otherwise, a new record will be
	 * created. The implementation should handle the persistence logic for saving
	 * the OrderItem.
	 *
	 * @param orderItem The OrderItem object to be saved.
	 * @return The saved OrderItem object, which includes any updates or new
	 *         database-generated fields.
	 */
	OrderItem save(OrderItem orderItem);
}
