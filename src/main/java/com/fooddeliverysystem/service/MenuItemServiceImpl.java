package com.fooddeliverysystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddeliverysystem.dao.MenuItemDao;
import com.fooddeliverysystem.entity.MenuItem;

import jakarta.transaction.Transactional;

@Service
public class MenuItemServiceImpl implements MenuItemService {
	
	private static final Logger logger = LoggerFactory.getLogger(MenuItemServiceImpl.class);
	private MenuItemDao menuItemDao;
	@Autowired
	public MenuItemServiceImpl(MenuItemDao menuItemDao) {
		super();
		this.menuItemDao = menuItemDao;
	}

	/**
	 * Retrieves the most recently added menu item from the database.
	 *
	 * This method queries the database to find the menu item with the highest ID, which 
	 * represents the most recently added item. It uses the menuItemDao to fetch the record 
	 * ordered by itemId in descending order.
	 *
	 * @return The most recently added MenuItem object, or null if no items are found.
	 */
	@Override
	public MenuItem findLastRecord() {
	    // Log the attempt to find the last menu item record
	    logger.info("Attempting to find the most recently added MenuItem.");

	    // Query the database to find the menu item with the highest itemId
	    MenuItem lastMenuItem = menuItemDao.findFirstByOrderByItemIdDesc();

	    // Log the result of the query
	    if (lastMenuItem != null) {
	        logger.info("Most recently added MenuItem found with ID: {}", lastMenuItem.getItemId());
	    } else {
	        logger.info("No MenuItem records found in the database.");
	    }

	    return lastMenuItem;
	}


	/**
	 * Saves a menu item to the database.
	 *
	 * This method persists the given MenuItem object to the database using the menuItemDao.
	 * If the MenuItem already exists, it will be updated; otherwise, a new record will be created.
	 *
	 * @param menuItem The MenuItem object to be saved.
	 * @return The saved MenuItem object, which includes any updates or new database-generated fields.
	 */
	@Transactional
	@Override
	public MenuItem save(MenuItem menuItem) {
	    // Log the start of the save operation
	    logger.info("Attempting to save MenuItem with ID: {}", menuItem.getItemId());

	    // Save the MenuItem to the database and return the saved entity
	    MenuItem savedMenuItem = menuItemDao.save(menuItem);

	    // Log the successful save operation
	    logger.info("MenuItem with ID: {} has been successfully saved", savedMenuItem.getItemId());

	    return savedMenuItem;
	}



	/**
	 * Finds a menu item by its ID.
	 *
	 * This method retrieves a MenuItem from the database using its unique ID. If no menu item
	 * is found with the provided ID, the method returns null.
	 *
	 * @param id The unique ID of the menu item to be retrieved.
	 * @return A MenuItem object if found, otherwise null.
	 */
	@Override
	public MenuItem findById(int id) {
	    // Log the request to find a menu item by its ID
	    logger.info("Attempting to find menu item with ID: {}", id);

	    // Retrieve the MenuItem by ID using the menuItemDao
	    MenuItem menuItem = menuItemDao.findById(id).orElse(null);

	    // Log the result of the retrieval
	    if (menuItem != null) {
	        logger.info("Menu item found with ID: {}", id);
	    } else {
	        logger.warn("No menu item found with ID: {}", id);
	    }

	    // Return the MenuItem if found, otherwise return null
	    return menuItem;
	}



	/**
	 * Retrieves a list of all menu items from the database.
	 *
	 * This method fetches all the MenuItem records stored in the database.
	 *
	 * @return A list of MenuItem objects representing all menu items in the database.
	 */
	@Override
	public List<MenuItem> findAll() {
	    // Log the retrieval operation
	    logger.info("Fetching all menu items from the database.");
	    
	    // Retrieve and return the list of all MenuItem records from the menuItemDao
	    List<MenuItem> menuItems = menuItemDao.findAll();
	    
	    // Log the number of menu items retrieved
	    logger.info("Retrieved {} menu items from the database.", menuItems.size());
	    
	    return menuItems;
	}



}
