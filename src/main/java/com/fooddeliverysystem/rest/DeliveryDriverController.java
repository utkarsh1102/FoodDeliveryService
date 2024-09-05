package com.fooddeliverysystem.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooddeliverysystem.dto.DeliveryDriverDto;
import com.fooddeliverysystem.entity.DeliveryDriver;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.service.DeliveryDriverService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/drivers")
public class DeliveryDriverController {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryDriverController.class);

	private DeliveryDriverService deliveryDriverService;

	@Autowired
	public DeliveryDriverController(DeliveryDriverService deliveryDriverService) {
		super();
		this.deliveryDriverService = deliveryDriverService;
	}

	/**
	 * Retrieves all delivery drivers.
	 *
	 * @return A ResponseEntity containing a list of DeliveryDriver objects and an
	 *         HTTP status code.
	 */
	@Operation(summary = "Retrieve all delivery drivers", description = "Returns a list of all registered delivery drivers.")
	@GetMapping
	public ResponseEntity<List<DeliveryDriver>> findAll() {
		// Log the method call
		logger.info("findAll method called to retrieve all delivery drivers.");

		// Return the list of delivery drivers with HTTP status 200 (OK)
		return new ResponseEntity<>(deliveryDriverService.findAll(), HttpStatus.OK);
	}

	/**
	 * Retrieves a delivery driver by their ID.
	 *
	 * @param id The ID of the delivery driver to retrieve.
	 * @return A ResponseEntity containing the DeliveryDriver object and an HTTP
	 *         status code.
	 * @throws EntityNotFoundException if no delivery driver is found with the
	 *                                 specified ID.
	 */
	@Operation(summary = "Retrieve a delivery driver by ID", description = "Returns a delivery driver object matching the provided ID.")
	@GetMapping("/{id}")
	public ResponseEntity<DeliveryDriver> findById(@PathVariable int id) {
		// Log the method call with the ID
		logger.info("findById method called to retrieve delivery driver with ID: {}", id);

		DeliveryDriver deliveryDriver = deliveryDriverService.findById(id);

		// Check if the delivery driver is found
		if (deliveryDriver == null) {
			logger.warn("Delivery driver with ID {} not found.", id);
			throw new EntityNotFoundException("Delivery Driver with ID " + id + " not found");
		}

		logger.info("Delivery driver with ID {} found.", id);

		// Return the delivery driver with HTTP status 200 (OK)
		return new ResponseEntity<>(deliveryDriver, HttpStatus.OK);
	}

	/**
	 * Updates the location of a delivery driver by their ID and stores it in a
	 * cookie.
	 *
	 * @param id                The ID of the delivery driver to update.
	 * @param deliveryDriverDto The DTO containing the new location information.
	 * @param response          The HttpServletResponse to add the cookie to.
	 * @param request           The HttpServletRequest to retrieve existing cookies.
	 * @return A ResponseEntity containing the updated DeliveryDriver object and an
	 *         HTTP status code.
	 * @throws EntityNotFoundException if no delivery driver is found with the
	 *                                 specified ID.
	 */
	@Operation(summary = "Update delivery driver location", description = "Updates the location of the specified delivery driver and stores it in a cookie.")
	@PutMapping("/{id}/location")
	public ResponseEntity<DeliveryDriver> updateLocation(@PathVariable int id,
			@RequestBody DeliveryDriverDto deliveryDriverDto, BindingResult bindingResult,
			HttpServletResponse response, HttpServletRequest request) {

		// Log the method call with the ID and new location
		logger.info("updateLocation method called for delivery driver with ID: {}", id);

		Cookie[] cookies = request.getCookies();
		DeliveryDriver deliveryDriver = deliveryDriverService.findById(id);

		// Check if the delivery driver is found
		if (deliveryDriver == null) {
			logger.warn("Delivery driver with ID {} not found.", id);
			throw new EntityNotFoundException("Delivery Driver with ID " + id + " not found");
		}

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("deliveryDriverId" + id + "Location")) {
					cookie.setValue(deliveryDriverDto.getLocation());
					response.addCookie(cookie);
					logger.info("Updated location cookie for delivery driver with ID: {}", id);
					return new ResponseEntity<>(deliveryDriver, HttpStatus.OK);
				}
			}
		}

		Cookie cookie = new Cookie("deliveryDriverId" + id + "Location", deliveryDriverDto.getLocation());
		cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
		response.addCookie(cookie);
		logger.info("New location cookie created for delivery driver with ID: {}", id);

		return new ResponseEntity<>(deliveryDriver, HttpStatus.OK);
	}

	/**
	 * Retrieves all orders for a specific delivery driver by their ID.
	 *
	 * @param id The ID of the delivery driver whose orders are to be retrieved.
	 * @return A ResponseEntity containing a list of Order objects and an HTTP
	 *         status code.
	 * @throws EntityNotFoundException if no delivery driver is found with the
	 *                                 specified ID.
	 */
	@Operation(summary = "Retrieve orders for a delivery driver", description = "Returns a list of orders assigned to the delivery driver with the specified ID.")
	@GetMapping("/{id}/orders")
	public ResponseEntity<List<Order>> findOrdersByDeliveryDriver(@PathVariable int id) {
		// Log the method call with the delivery driver ID
		logger.info("findOrdersByDeliveryDriver method called for delivery driver with ID: {}", id);

		DeliveryDriver deliveryDriver = deliveryDriverService.findById(id);

		// Check if the delivery driver is found
		if (deliveryDriver == null) {
			logger.warn("Delivery driver with ID {} not found.", id);
			throw new EntityNotFoundException("Delivery Driver with ID " + id + " not found");
		}

		// Log the successful retrieval of orders
		logger.info("Orders retrieved for delivery driver with ID: {}", id);

		return new ResponseEntity<>(deliveryDriver.getOrders(), HttpStatus.OK);
	}
}