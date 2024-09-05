package com.fooddeliverysystem.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooddeliverysystem.dto.CouponDto;
import com.fooddeliverysystem.dto.OrderDto;
import com.fooddeliverysystem.dto.OrderItemDto;
import com.fooddeliverysystem.dto.RatingDto;
import com.fooddeliverysystem.entity.Coupon;
import com.fooddeliverysystem.entity.Customer;
import com.fooddeliverysystem.entity.DeliveryDriver;
import com.fooddeliverysystem.entity.MenuItem;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.entity.OrderItem;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;
import com.fooddeliverysystem.exception.ValidationException;
import com.fooddeliverysystem.service.CouponService;
import com.fooddeliverysystem.service.CustomerService;
import com.fooddeliverysystem.service.DeliveryDriverService;
import com.fooddeliverysystem.service.MenuItemService;
import com.fooddeliverysystem.service.OrderItemService;
import com.fooddeliverysystem.service.OrderService;
import com.fooddeliverysystem.service.RatingService;
import com.fooddeliverysystem.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	private OrderService orderService;
	private CustomerService customerService;
	private RestaurantService restaurantService;
	private DeliveryDriverService deliveryDriverService;
	private MenuItemService menuItemService;
	private OrderItemService orderItemService;
	private RatingService ratingService;
	private CouponService couponService;

	@Autowired
	public OrderController(OrderService orderService, CustomerService customerService,
			RestaurantService restaurantService, DeliveryDriverService deliveryDriverService,
			MenuItemService menuItemService, OrderItemService orderItemService, RatingService ratingService,
			CouponService couponService) {
		super();
		this.orderService = orderService;
		this.customerService = customerService;
		this.restaurantService = restaurantService;
		this.deliveryDriverService = deliveryDriverService;
		this.menuItemService = menuItemService;
		this.orderItemService = orderItemService;
		this.ratingService = ratingService;
		this.couponService = couponService;
	}

	/**
	 * Adds a new order based on the provided order data transfer object.
	 *
	 * @param orderDto The DTO containing the order details.
	 * @return A ResponseEntity containing the created order and an HTTP status
	 *         code.
	 * @throws EntityNotFoundException if the customer, restaurant, menu item, or
	 *                                 coupon is not found with the specified IDs.
	 */
	@Operation(summary = "Add a new order", description = "Creates a new order with the specified customer, restaurant, menu items, and ratings.")
	@PostMapping
	public ResponseEntity<Order> addOrder(@Valid @RequestBody OrderDto orderDto, BindingResult bindingResult) {

		// Check if there are any validation errors in the binding result
		if (bindingResult.hasErrors()) {

			// Retrieve all error messages from the binding result and collect them into a
			// list of strings
			List<String> errors = bindingResult.getAllErrors() // Get all the errors from binding result
					.stream() // Stream the list of errors
					.map(error -> error.getDefaultMessage()) // Map each error to its default message
					.collect(Collectors.toList()); // Collect the mapped error messages into a list

			// Throw a custom ValidationException with the list of error messages
			throw new ValidationException(errors);
		}

		// Log the method call
		logger.info("addOrder method called with order data for customer ID: {}",
				orderDto.getCustomer().getCustomerId());

		Order order = new Order();
		Customer customer = customerService.findById(orderDto.getCustomer().getCustomerId());

		// Check if the customer is found
		if (customer == null) {
			logger.warn("Customer with ID {} not found.", orderDto.getCustomer().getCustomerId());
			throw new EntityNotFoundException(
					"Customer with ID " + orderDto.getCustomer().getCustomerId() + " not found");
		}

		Restaurant restaurant = restaurantService.findById(orderDto.getRestaurant().getRestaurantId());

		// Check if the restaurant is found
		if (restaurant == null) {
			logger.warn("Restaurant with ID {} not found.", orderDto.getRestaurant().getRestaurantId());
			throw new EntityNotFoundException(
					"Restaurant with ID " + orderDto.getRestaurant().getRestaurantId() + " not found");
		}


		order.setCustomer(customer);
		order.setRestaurant(restaurant);
		order.setOrderId(orderService.findLastRecord().getOrderId() + 1);
		order.setOrderDate(orderDto.getOrderDate());
		order.setOrderStatus(orderDto.getOrderStatus());
		orderService.save(order);

		List<OrderItem> items = new ArrayList<>();
		for (OrderItemDto orderItemDto : orderDto.getItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setQuantity(orderItemDto.getQuantity());

			MenuItem menuItem = menuItemService.findById(orderItemDto.getMenuItem().getMenuItemId());

			// Check if the menu item is found
			if (menuItem == null) {
				logger.warn("Menu Item with ID {} not found.", orderItemDto.getMenuItem().getMenuItemId());
				throw new EntityNotFoundException(
						"Menu Item with ID " + orderItemDto.getMenuItem().getMenuItemId() + " not found");
			}

			orderItem.setMenuItem(menuItem);
			orderItem.setOrderItemId(orderItemService.findLastRecord().getOrderItemId() + 1);
			items.add(orderItem);
			orderItemService.save(orderItem);
		}

		List<Rating> ratings = new ArrayList<>();
		for (RatingDto ratingDto : orderDto.getRatings()) {
			Rating rating = new Rating();
			rating.setRating(ratingDto.getRating());
			rating.setRestaurant(restaurant);
			rating.setOrder(order);
			rating.setRatingId(ratingService.findLastRecord().getRatingId() + 1);
			rating.setReview(ratingDto.getReview());
			ratings.add(rating);
			ratingService.save(rating);
		}

		List<Coupon> coupons = new ArrayList<>();
		for (CouponDto couponDto : orderDto.getCoupons()) {
			Coupon coupon = couponService.findById(couponDto.getCouponId());

			// Check if the coupon is found
			if (coupon == null) {
				logger.warn("Coupon with ID {} not found.", couponDto.getCouponId());
				throw new EntityNotFoundException("Coupon with ID " + couponDto.getCouponId() + " not found");
			}

			coupon.getOrders().add(order);
			coupons.add(coupon);
			couponService.save(coupon);
		}

		order.setItems(items);
		order.setRatings(ratings);
		order.setCoupons(coupons);

		// Log the successful creation of the order
		logger.info("Order created successfully with ID: {}", order.getOrderId());

		return new ResponseEntity<>(order, HttpStatus.CREATED);
	}

	/**
	 * Finds an order by its ID.
	 *
	 * @param id The ID of the order to find.
	 * @return A ResponseEntity containing the found order and an HTTP status code.
	 * @throws EntityNotFoundException if no order is found with the specified ID.
	 */
	@Operation(summary = "Find order by ID", description = "Retrieves an order using the provided order ID.")
	@GetMapping("/{id}")
	public ResponseEntity<Order> findById(@PathVariable int id) {
		// Log the method call
		logger.info("findById method called with order ID: {}", id);

		Order order = orderService.findById(id);

		// Check if the order is found
		if (order == null) {
			logger.warn("Order with ID {} not found.", id);
			throw new EntityNotFoundException("Order not found at ID " + id);
		}

		// Log the successful retrieval of the order
		logger.info("Order retrieved successfully with ID: {}", id);

		return new ResponseEntity<>(order, HttpStatus.OK);
	}

	/**
	 * Changes the status of an order.
	 *
	 * @param id       The ID of the order whose status needs to be changed.
	 * @param orderDto The order data transfer object containing the new status.
	 * @return A ResponseEntity containing the updated order and an HTTP status
	 *         code.
	 * @throws EntityNotFoundException if no order is found with the specified ID.
	 */
	@Operation(summary = "Change order status", description = "Updates the status of an order using the provided order ID and new status.")
	@PutMapping("/{id}/status")
	public ResponseEntity<Order> changeStatus(@PathVariable int id, @RequestBody OrderDto orderDto) {

		// Log the method call
		logger.info("changeStatus method called with order ID: {}", id);

		Order order = orderService.findById(id);

		// Check if the order is found
		if (order == null) {
			logger.warn("Order with ID {} not found.", id);
			throw new EntityNotFoundException("Order not found at ID " + id);
		} else {
			// Log the status change
			logger.info("Changing status of order ID: {} to {}", id, orderDto.getOrderStatus());

			order.setOrderStatus(orderDto.getOrderStatus());

			// Log the successful update of the order status
			logger.info("Order status updated successfully for order ID: {}", id);

			return new ResponseEntity<>(orderService.save(order), HttpStatus.OK);
		}
	}

	/**
	 * Deletes an order by its ID.
	 *
	 * @param id The ID of the order to delete.
	 * @throws EntityNotFoundException if no order is found with the specified ID.
	 */
	@Operation(summary = "Delete order by ID", description = "Deletes an order using the provided order ID.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
		// Log the method call
		logger.info("deleteOrder method called with order ID: {}", id);

		Order order = orderService.findById(id);

		// Check if the order is found
		if (order == null) {
			logger.warn("Order with ID {} not found.", id);
			throw new EntityNotFoundException("Order not found at ID " + id);
		} else {
			// Log the order deletion
			logger.info("Deleting order with ID: {}", id);

			orderService.deleteById(id);

			// Log the successful deletion of the order
			logger.info("Order with ID: {} deleted successfully.", id);
			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Assigns a delivery driver to an order.
	 *
	 * @param orderId  The ID of the order to which the driver is being assigned.
	 * @param driverId The ID of the delivery driver being assigned to the order.
	 * @return A ResponseEntity containing the assigned delivery driver and an HTTP
	 *         status code.
	 * @throws EntityNotFoundException if no order or delivery driver is found with
	 *                                 the specified IDs.
	 */
	@Operation(summary = "Assign driver to order", description = "Assigns a delivery driver to an order using the provided order ID and driver ID.")
	@PutMapping("/{orderId}/assignDriver/{driverId}")
	public ResponseEntity<DeliveryDriver> assignDriver(@PathVariable int orderId, @PathVariable int driverId) {

		// Log the method call
		logger.info("assignDriver method called with order ID: {} and driver ID: {}", orderId, driverId);

		DeliveryDriver deliveryDriver = deliveryDriverService.findById(driverId);

		// Check if the delivery driver is found
		if (deliveryDriver == null) {
			logger.warn("Delivery Driver with ID {} not found.", driverId);
			throw new EntityNotFoundException("Delivery Driver with ID " + driverId + " not found");
		}

		Order order = orderService.findById(orderId);

		// Check if the order is found
		if (order == null) {
			logger.warn("Order with ID {} not found.", orderId);
			throw new EntityNotFoundException("Order with ID " + orderId + " not found");
		}

		// Log the assignment action
		logger.info("Assigning driver ID: {} to order ID: {}", driverId, orderId);

		deliveryDriver.getOrders().add(order);
		order.setDeliveryDriver(deliveryDriver);
		orderService.save(order);
		deliveryDriverService.save(deliveryDriver);

		// Log the successful assignment
		logger.info("Driver ID: {} successfully assigned to order ID: {}", driverId, orderId);

		return new ResponseEntity<>(deliveryDriver, HttpStatus.OK);
	}
}