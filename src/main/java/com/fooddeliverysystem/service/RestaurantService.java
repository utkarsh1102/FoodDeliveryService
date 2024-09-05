package com.fooddeliverysystem.service;

import java.util.List;

import com.fooddeliverysystem.entity.DeliveryAddress;
import com.fooddeliverysystem.entity.MenuItem;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;

/**
 * Interface for the Restaurant service layer. Defines operations related to
 * restaurants, menu items, ratings, and delivery addresses.
 */
public interface RestaurantService {

	/**
	 * Retrieves a list of all restaurants from the database.
	 *
	 * @return A list of all Restaurant objects.
	 */
	List<Restaurant> findAll();

	/**
	 * Finds a restaurant by its unique ID.
	 *
	 * This method attempts to locate a restaurant in the database by its ID. If no
	 * restaurant is found, it will return null.
	 *
	 * @param id The unique ID of the restaurant to be retrieved.
	 * @return The Restaurant object if found, otherwise null.
	 */
	Restaurant findById(int id);

	/**
	 * Saves or updates a restaurant in the database.
	 *
	 * This method will persist the given Restaurant object. If the restaurant
	 * already exists (based on its ID), it will be updated; otherwise, a new record
	 * will be created.
	 *
	 * @param restaurant The Restaurant object to be saved or updated.
	 * @return The saved or updated Restaurant object.
	 */
	Restaurant save(Restaurant restaurant);

	/**
	 * Deletes a restaurant by its unique ID.
	 *
	 * This method will remove the restaurant with the specified ID from the
	 * database.
	 *
	 * @param id The unique ID of the restaurant to be deleted.
	 */
	void deleteById(int id);

	/**
	 * Retrieves a list of menu items for a specific restaurant.
	 *
	 * This method finds the restaurant by its ID and returns all associated menu
	 * items.
	 *
	 * @param id The unique ID of the restaurant whose menu items are to be
	 *           retrieved.
	 * @return A list of MenuItem objects associated with the specified restaurant.
	 */
	List<MenuItem> menuItemsById(int id);

	/**
	 * Retrieves a list of reviews for a specific restaurant.
	 *
	 * This method finds the restaurant by its ID and returns all associated
	 * reviews.
	 *
	 * @param id The unique ID of the restaurant whose reviews are to be retrieved.
	 * @return A list of Rating objects associated with the specified restaurant.
	 */
	List<Rating> reviewsById(int id);

	/**
	 * Retrieves a list of delivery addresses served by a specific restaurant.
	 *
	 * This method finds the restaurant by its ID, gathers all associated orders,
	 * and then collects all unique delivery addresses from the customers who placed
	 * the orders.
	 *
	 * @param id The unique ID of the restaurant whose served delivery addresses are
	 *           to be retrieved.
	 * @return A list of DeliveryAddress objects served by the specified restaurant.
	 */
	List<DeliveryAddress> deliveryAddressServedById(int id);

	/**
	 * Retrieves the most recently added restaurant.
	 *
	 * This method queries the database to find the restaurant with the highest ID,
	 * which represents the most recently added restaurant.
	 *
	 * @return The most recently added Restaurant object, or null if no restaurants
	 *         are found.
	 */
	Restaurant findLastRecord();

	/**
	 * Saves or updates a menu item for a specific restaurant.
	 *
	 * This method associates the given MenuItem with the specified Restaurant,
	 * assigns a new item ID, and then saves both the restaurant and the menu item
	 * to the database.
	 *
	 * @param restaurant The Restaurant object to which the menu item will be
	 *                   associated.
	 * @param menuItem   The MenuItem object to be saved.
	 * @return The saved MenuItem object.
	 */
	MenuItem saveMenuItemsById(Restaurant restaurant, MenuItem menuItem);

	/**
	 * Deletes a menu item by its unique ID.
	 *
	 * This method removes the menu item with the specified ID from the database.
	 *
	 * @param itemId The unique ID of the menu item to be deleted.
	 */
	void deleteMenuItemsById(int itemId);
}
