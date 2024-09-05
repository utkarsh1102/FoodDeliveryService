package com.fooddeliverysystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddeliverysystem.dao.OrderItemDao;
import com.fooddeliverysystem.entity.OrderItem;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	private static final Logger logger = LoggerFactory.getLogger(OrderItemServiceImpl.class);
	private OrderItemDao orderItemDao;

	@Autowired
	public OrderItemServiceImpl(OrderItemDao orderItemDao) {
		super();
		this.orderItemDao = orderItemDao;
	}

	/**
	 * Retrieve the most recently added order item from the database.
	 * 
	 * @return The most recently added `OrderItem`, or null if no order items are
	 *         present.
	 */
	@Override
	public OrderItem findLastRecord() {
		logger.info("Fetching the most recent order item");

		OrderItem latestOrderItem = orderItemDao.findFirstByOrderByOrderItemIdDesc();
		if (latestOrderItem == null) {
			logger.warn("No order items found in the database");
		} else {
			logger.info("Most recent order item retrieved with ID: {}", latestOrderItem.getOrderItemId());
		}

		return latestOrderItem;
	}

	/**
	 * Save an order item to the database.
	 * 
	 * @param orderItem The order item object to be saved.
	 * @return The saved `OrderItem` object, which may include updated details such
	 *         as a generated ID.
	 */
	@Override
	public OrderItem save(OrderItem orderItem) {
		logger.info("Attempting to save order item: {}", orderItem);

		OrderItem savedOrderItem = orderItemDao.save(orderItem);

		logger.info("Order item saved successfully with ID: {}", savedOrderItem.getOrderItemId());

		return savedOrderItem;
	}

}
