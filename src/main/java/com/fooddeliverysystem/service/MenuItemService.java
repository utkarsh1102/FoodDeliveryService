package com.fooddeliverysystem.service;

import java.util.List;

import com.fooddeliverysystem.entity.MenuItem;

/**
 * Service interface for managing menu items. Provides methods for performing
 * CRUD operations and retrieving menu item information.
 */
public interface MenuItemService {

	/**
	 * Retrieves the most recently added menu item from the database.
	 *
	 * This method queries the database to find the menu item with the highest
	 * itemId, which represents the most recently added item. It returns the most
	 * recent MenuItem object or null if no items are found.
	 *
	 * @return The most recently added MenuItem object, or null if no items are
	 *         found.
	 */
	MenuItem findLastRecord();

	/**
	 * Saves a menu item to the database.
	 *
	 * This method persists the given MenuItem object to the database using the
	 * menuItemDao. If the MenuItem already exists, it will be updated; otherwise, a
	 * new record will be created.
	 *
	 * @param menuItem The MenuItem object to be saved.
	 * @return The saved MenuItem object, which includes any updates or new
	 *         database-generated fields.
	 */
	MenuItem save(MenuItem menuItem);

	/**
	 * Finds a menu item by its unique ID.
	 *
	 * This method retrieves a MenuItem from the database using its unique ID. If no
	 * menu item is found with the provided ID, the method returns null.
	 *
	 * @param id The unique ID of the menu item to be retrieved.
	 * @return A MenuItem object if found, otherwise null.
	 */
	MenuItem findById(int id);

	/**
	 * Retrieves a list of all menu items from the database.
	 *
	 * This method fetches all the MenuItem records from the database and returns
	 * them as a list.
	 *
	 * @return A list of MenuItem objects.
	 */
	List<MenuItem> findAll();
}
