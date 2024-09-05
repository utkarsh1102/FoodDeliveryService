package com.fooddeliverysystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddeliverysystem.dao.OrderDao;
import com.fooddeliverysystem.entity.Order;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService{
	
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	private OrderDao orderDao;
	@Autowired
	public OrderServiceImpl(OrderDao orderDao) {
		super();
		this.orderDao = orderDao;
	}
	/**
	 * Retrieves a list of all orders from the database.
	 *
	 * This method fetches all Order objects stored in the database using the orderDao.
	 * It returns a list containing all orders available.
	 *
	 * @return A list of Order objects.
	 */
	@Override
	public List<Order> findAll() {
	    // Log the request to find all orders
	    logger.info("Fetching all orders from the database.");

	    // Retrieve all Order objects using the orderDao
	    List<Order> orders = orderDao.findAll();

	    // Log the number of orders retrieved
	    logger.info("Successfully fetched {} orders from the database.", orders.size());

	    return orders;
	}

	/**
	 * Finds an order by its ID.
	 *
	 * This method retrieves an Order from the database using its unique ID. If no order
	 * is found with the provided ID, the method returns null.
	 *
	 * @param id The unique ID of the order to be retrieved.
	 * @return An Order object if found, otherwise null.
	 */
	@Override
	public Order findById(int id) {
	    // Log the request to find an order by its ID
	    logger.info("Attempting to find Order with ID: {}", id);

	    // Retrieve the Order by ID using the orderDao
	    Order order = orderDao.findById(id).orElse(null);

	    // Log the result of the find operation
	    if (order != null) {
	        logger.info("Order found with ID: {}", id);
	    } else {
	        logger.warn("No Order found with ID: {}", id);
	    }

	    // Return the Order if found, otherwise return null
	    return order;
	}

	/**
	 * Retrieves the most recently added order from the database.
	 *
	 * This method queries the database to find the order with the highest ID, 
	 * representing the most recently added order. It uses the orderDao to fetch 
	 * the record ordered by orderId in descending order.
	 *
	 * @return The most recently added Order object, or null if no orders are found.
	 */
	@Override
	public Order findLastRecord() {
	    // Log the attempt to find the last order record
	    logger.info("Attempting to find the most recently added Order.");

	    // Query the database to find the order with the highest orderId
	    Order lastOrder = orderDao.findFirstByOrderByOrderIdDesc();

	    // Log the result of the query
	    if (lastOrder != null) {
	        logger.info("Most recently added Order found with ID: {}", lastOrder.getOrderId());
	    } else {
	        logger.info("No Order records found in the database.");
	    }

	    return lastOrder;
	}

	/**
	 * Saves an order to the database.
	 *
	 * This method persists the given Order object to the database using the orderDao.
	 * If the Order already exists, it will be updated; otherwise, a new record will be created.
	 *
	 * @param order The Order object to be saved.
	 * @return The saved Order object, which includes any updates or new database-generated fields.
	 */
	@Transactional
	@Override
	public Order save(Order order) {
	    // Log the attempt to save the order
	    logger.info("Attempting to save Order with ID: {}", order.getOrderId());

	    // Save the Order to the database and return the saved entity
	    Order savedOrder = orderDao.save(order);

	    // Log the successful save operation
	    logger.info("Order with ID: {} has been successfully saved.", savedOrder.getOrderId());

	    return savedOrder;
	}

	/**
	 * Deletes an order by its ID.
	 *
	 * This method removes the order with the specified ID from the database using the orderDao.
	 * If the order does not exist, no action is taken.
	 *
	 * @param id The unique ID of the order to be deleted.
	 */
	@Override
	public void deleteById(int id) {
	    // Log the request to delete an order by its ID
	    logger.info("Attempting to delete order with ID: {}", id);

	    // Perform the deletion operation using the orderDao
	    orderDao.deleteById(id);

	    // Log the successful deletion of the order
	    logger.info("Order with ID: {} has been successfully deleted.", id);
	}


}
