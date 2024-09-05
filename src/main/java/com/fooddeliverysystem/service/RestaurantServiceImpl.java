package com.fooddeliverysystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddeliverysystem.dao.MenuItemDao;
import com.fooddeliverysystem.dao.RestaurantDao;
import com.fooddeliverysystem.entity.Customer;
import com.fooddeliverysystem.entity.DeliveryAddress;
import com.fooddeliverysystem.entity.MenuItem;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class RestaurantServiceImpl implements RestaurantService{
	private static final Logger logger = LoggerFactory.getLogger(RestaurantServiceImpl.class);
	
	private RestaurantDao restaurantDao;
	private MenuItemDao menuItemDao;
	
	@Autowired
	public RestaurantServiceImpl(RestaurantDao restaurantDao, MenuItemDao menuItemDao) {
		super();
		this.restaurantDao = restaurantDao;
		this.menuItemDao = menuItemDao;
	}

	/**
     * Retrieves a list of all restaurants from the database.
     *
     * @return A list of Restaurant objects.
     */
	@Override
	@Operation(summary = "Retrieve all restaurants", description = "Fetches a list of all restaurants from the database.")
	public List<Restaurant> findAll() {
	    // Log the request to retrieve all restaurants
	    logger.info("Fetching all restaurants from the database");
	    // Return the list of all restaurants using the restaurantDao
	    return restaurantDao.findAll();
	}

	/**
	 * Finds a restaurant by its ID.
	 *
	 * This method attempts to find a restaurant in the database
	 * by the given ID. If no restaurant is found, it returns null.
	 *
	 * @param id The unique ID of the restaurant to be retrieved.
	 * @return A Restaurant object if found, otherwise null.
	 */
	@Override
	@Operation(summary = "Find a restaurant by ID", description = "Fetches a restaurant from the database using its unique ID. Returns null if no restaurant is found.")
	public Restaurant findById(int id) {
	    // Log the request to find a restaurant by its ID
	    logger.info("Fetching restaurant with ID: {}", id);
	    
	    // Retrieve the restaurant by ID using the restaurantDao
	    // Return the restaurant if found, otherwise return null
	    return restaurantDao.findById(id).orElse(null);
	}
	
	/**
	 * Saves a restaurant to the database.
	 *
	 * This method takes a Restaurant object and persists it to the database.
	 * The saved Restaurant object, which includes any updates or generated IDs, is returned.
	 *
	 * @param restaurant The Restaurant object to be saved.
	 * @return The saved Restaurant object, including any generated or updated fields.
	 */
	@Transactional
	@Override
	@Operation(summary = "Save a restaurant", description = "Saves the provided restaurant object to the database and returns the saved instance.")
	public Restaurant save(Restaurant restaurant) {
	    // Log the request to save a restaurant
	    logger.info("Saving restaurant with details: {}", restaurant);

	    // Persist the restaurant object to the database using the restaurantDao
	    // Return the saved restaurant object
	    return restaurantDao.save(restaurant);
	}
	
	/**
	 * Deletes a restaurant by its ID.
	 *
	 * This method removes a restaurant from the database using its unique ID.
	 * If the restaurant does not exist, no action is taken.
	 *
	 * @param id The unique ID of the restaurant to be deleted.
	 */
	@Transactional
	@Override
	@Operation(summary = "Delete a restaurant by ID", description = "Deletes a restaurant from the database based on the provided ID.")
	public void deleteById(int id) {
	    // Log the request to delete a restaurant by its ID
	    logger.info("Deleting restaurant with ID: {}", id);
	    
	    // Remove the restaurant from the database using the restaurantDao
	    restaurantDao.deleteById(id);
	}

	/**
	 * Retrieves a list of menu items for a specific restaurant by its ID.
	 *
	 * This method fetches the restaurant from the database using the provided ID
	 * and returns the list of menu items associated with that restaurant. If no
	 * restaurant is found with the given ID, it returns null.
	 *
	 * @param id The unique ID of the restaurant whose menu items are to be retrieved.
	 * @return A list of MenuItem objects associated with the restaurant, or null if no restaurant is found.
	 */
	@Override
	@Operation(summary = "Get menu items by restaurant ID", description = "Fetches a list of menu items for a specified restaurant using its ID.")
	public List<MenuItem> menuItemsById(int id) {
	    // Log the request to find menu items for the restaurant by its ID
	    logger.info("Fetching menu items for restaurant with ID: {}", id);
	    
	    // Retrieve the restaurant by ID using the restaurantDao
	    // If the restaurant is found, return its menu items; otherwise, return null
	    return restaurantDao.findById(id).orElse(null).getMenuItems();
	}


	/**
	 * Retrieves a list of reviews (ratings) for a specific restaurant by its ID.
	 *
	 * This method fetches the restaurant from the database using the provided ID
	 * and returns the list of reviews associated with that restaurant. If no
	 * restaurant is found with the given ID, it returns null.
	 *
	 * @param id The unique ID of the restaurant whose reviews are to be retrieved.
	 * @return A list of Rating objects associated with the restaurant, or null if no restaurant is found.
	 */
	@Override
	@Operation(summary = "Get reviews by restaurant ID", description = "Fetches a list of reviews (ratings) for a specified restaurant using its ID.")
	public List<Rating> reviewsById(int id) {
	    // Log the request to find reviews for the restaurant by its ID
	    logger.info("Fetching reviews for restaurant with ID: {}", id);
	    
	    // Retrieve the restaurant by ID using the restaurantDao
	    // If the restaurant is found, return its ratings; otherwise, return null
	    return restaurantDao.findById(id).orElse(null).getRatings();
	}


	/**
	 * Retrieves a list of delivery addresses served by a specific restaurant.
	 *
	 * This method finds all orders associated with the given restaurant ID,
	 * collects the customers from those orders, and then gathers all delivery
	 * addresses from those customers. It returns a list of these delivery addresses.
	 *
	 * @param id The unique ID of the restaurant whose served delivery addresses are to be retrieved.
	 * @return A list of DeliveryAddress objects associated with customers of the specified restaurant.
	 */
	@Override
	public List<DeliveryAddress> deliveryAddressServedById(int id) {
	    // Retrieve all orders associated with the given restaurant ID
	    List<Order> orders = findById(id).getOrders();

	    // Extract all customers from the retrieved orders
	    List<Customer> customers = orders.stream()
	                                     .map(order -> order.getCustomer())
	                                     .collect(Collectors.toList());

	    // Collect all delivery addresses from the customers
	    return customers.stream()
	                    .flatMap(customer -> customer.getAddresses().stream())
	                    .collect(Collectors.toList());
	}


	/**
	 * Retrieves the most recently added restaurant from the database.
	 *
	 * This method fetches the restaurant with the highest ID, which is 
	 * typically the most recently added record based on the ID ordering.
	 *
	 * @return The most recently added Restaurant object.
	 */
	@Override
	public Restaurant findLastRecord() {
	    // Fetch the restaurant with the highest ID (most recent record) from the database
	    return restaurantDao.findFirstByOrderByRestaurantIdDesc();
	}

	
	/**
	 * Saves a new menu item to the specified restaurant and returns the saved menu item.
	 *
	 * This method associates the provided menu item with the specified restaurant,
	 * sets a new item ID, saves both the restaurant and the menu item to the database,
	 * and then returns the saved menu item.
	 *
	 * @param restaurant The restaurant to which the menu item will be added.
	 * @param menuItem The menu item to be saved.
	 * @return The saved MenuItem object.
	 */
	@Transactional
	@Override
	public MenuItem saveMenuItemsById(Restaurant restaurant, MenuItem menuItem) {
	    // Associate the menu item with the specified restaurant
	    menuItem.setRestaurant(restaurant);

	    // Add the menu item to the restaurant's list of menu items
	    restaurant.getMenuItems().add(menuItem);

	    // Set a new item ID for the menu item by incrementing the highest existing ID
	    menuItem.setItemId(menuItemDao.findFirstByOrderByItemIdDesc().getItemId() + 1);

	    // Save the updated restaurant to the database
	    restaurantDao.save(restaurant);

	    // Save the new menu item to the database
	    menuItemDao.save(menuItem);

	    // Return the saved menu item
	    return menuItem;
	}


	/**
	 * Deletes a menu item from the database by its ID.
	 *
	 * This method attempts to find the menu item using the provided ID. If the item is not found,
	 * it throws an EntityNotFoundException. If the item is found, it is deleted from the database.
	 *
	 * @param itemId The unique ID of the menu item to be deleted.
	 */
	@Transactional
	@Override
	public void deleteMenuItemsById(int itemId) {
	    // Retrieve the menu item by its ID
	    MenuItem menuItem = menuItemDao.findById(itemId).orElse(null);
	    
	    // If the menu item is not found, throw an exception
	    if(menuItem == null) {
	        throw new EntityNotFoundException("Item not found at ID " + itemId);
	    }
	    
	    // Delete the menu item from the database
	    menuItemDao.deleteById(itemId);
	}



}
