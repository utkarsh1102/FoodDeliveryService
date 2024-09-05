package com.fooddeliverysystem.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

import com.fooddeliverysystem.dto.CustomerDto;
import com.fooddeliverysystem.dto.DeliveryAddressDto;
import com.fooddeliverysystem.dto.ListOfRestaurantsDto;
import com.fooddeliverysystem.dto.RestaurantDto;
import com.fooddeliverysystem.entity.Customer;
import com.fooddeliverysystem.entity.DeliveryAddress;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;
import com.fooddeliverysystem.exception.ValidationException;
import com.fooddeliverysystem.service.CustomerService;
import com.fooddeliverysystem.service.DeliveryAddressService;
import com.fooddeliverysystem.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private CustomerService customerService;
	private DeliveryAddressService deliveryAddressService;
	private RestaurantService restaurantService;

	@Autowired
	public CustomerController(CustomerService customerService, DeliveryAddressService deliveryAddressService,
			RestaurantService restaurantService) {
		super();
		this.customerService = customerService;
		this.deliveryAddressService = deliveryAddressService;
		this.restaurantService = restaurantService;
	}

	/**
	 * Retrieves a list of all customers.
	 *
	 * @return A ResponseEntity containing a list of Customer objects and an HTTP
	 *         status code.
	 */
	@Operation(summary = "Retrieve all customers", description = "Returns a list of all registered customers.")
	@GetMapping
	public ResponseEntity<List<Customer>> findAll() {
		// Log the method call
		logger.info("findAll method called to retrieve all customers.");

		// Return the list of customers with HTTP status 200 (OK)
		return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
	}

	/**
	 * Retrieves a customer by their ID.
	 *
	 * @param id The ID of the customer to retrieve.
	 * @return A ResponseEntity containing the Customer object and an HTTP status
	 *         code.
	 * @throws EntityNotFoundException if no customer is found with the specified
	 *                                 ID.
	 */
	@Operation(summary = "Retrieve a customer by ID", description = "Returns the customer object that matches the provided ID.")
	@GetMapping("/{id}")
	public ResponseEntity<Customer> findById(@PathVariable int id) {
		// Log the method call with the customer ID
		logger.info("findById method called for customer with ID: {}", id);

		Customer customer = customerService.findById(id);

		// Check if the customer is found
		if (customer == null) {
			logger.warn("Customer with ID {} not found.", id);
			throw new EntityNotFoundException("Customer with ID " + id + " not found");
		}

		// Log the successful retrieval of the customer
		logger.info("Customer with ID {} found.", id);

		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	/**
	 * Updates the details of a customer by their ID.
	 *
	 * @param id          The ID of the customer to update.
	 * @param customerDto The DTO containing the updated customer information.
	 * @return A ResponseEntity containing the updated Customer object and an HTTP
	 *         status code.
	 * @throws EntityNotFoundException if no customer or delivery address is found
	 *                                 with the specified IDs.
	 */
	@Operation(summary = "Update customer details", description = "Updates the customer information, including their addresses, based on the provided ID and DTO.")
	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable int id, @Valid @RequestBody CustomerDto customerDto,
			BindingResult bindingResult) {

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

		// Log the method call with the customer ID
		logger.info("updateCustomer method called for customer with ID: {}", id);

		Customer customer = customerService.findById(id);

		// Check if the customer is found
		if (customer == null) {
			logger.warn("Customer with ID {} not found.", id);
			throw new EntityNotFoundException("Customer with ID " + id + " not found");
		}

		// Update customer details
		customer.setEmail(customerDto.getEmail());
		customer.setName(customerDto.getName());
		customer.setPhone(customerDto.getPhone());

		List<DeliveryAddress> addresses = new ArrayList<>();
		for (DeliveryAddressDto deliveryAddressDto : customerDto.getAddresses()) {
			DeliveryAddress deliveryAddress = deliveryAddressService.findById(deliveryAddressDto.getAddressId());

			// Check if the delivery address is found
			if (deliveryAddress == null) {
				logger.warn("Delivery address with ID {} not found.", deliveryAddressDto.getAddressId());
				throw new EntityNotFoundException(
						"Delivery Address with ID " + deliveryAddressDto.getAddressId() + " not found");
			}

			// Update delivery address details
			deliveryAddress.setAddressLine1(deliveryAddressDto.getAddressLine1());
			deliveryAddress.setAddressLine2(deliveryAddressDto.getAddressLine2());
			deliveryAddress.setCity(deliveryAddressDto.getCity());
			deliveryAddress.setCustomer(customer);
			deliveryAddress.setPostal(deliveryAddressDto.getPostal());
			deliveryAddress.setState(deliveryAddressDto.getState());

			addresses.add(deliveryAddress);
			deliveryAddressService.save(deliveryAddress);
		}

		customer.setAddresses(addresses);

		// Save the updated customer and log the success
		Customer updatedCustomer = customerService.save(customer);
		logger.info("Customer with ID {} updated successfully.", id);

		return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
	}

	/**
	 * Deletes a customer by their ID.
	 *
	 * @param id The ID of the customer to delete.
	 * @throws EntityNotFoundException if no customer is found with the specified
	 *                                 ID.
	 */
	@Operation(summary = "Delete a customer by ID", description = "Deletes the customer with the specified ID.")
	@DeleteMapping("/{id}")
	public void deleteCustomer(@PathVariable int id) {
		// Log the method call with the customer ID
		logger.info("deleteCustomer method called for customer with ID: {}", id);

		Customer customer = customerService.findById(id);

		// Check if the customer is found
		if (customer == null) {
			logger.warn("Customer with ID {} not found.", id);
			throw new EntityNotFoundException("Customer with ID " + id + " not found");
		}

		// Delete the customer and log the success
		customerService.deleteById(id);
		logger.info("Customer with ID {} deleted successfully.", id);
	}

	/**
	 * Retrieves all orders for a specific customer by their ID.
	 *
	 * @param id The ID of the customer whose orders are to be retrieved.
	 * @return A ResponseEntity containing a list of Order objects and an HTTP
	 *         status code.
	 * @throws EntityNotFoundException if no customer is found with the specified
	 *                                 ID.
	 */
	@Operation(summary = "Retrieve orders by customer ID", description = "Returns a list of orders associated with the customer having the specified ID.")
	@GetMapping("/{id}/orders")
	public ResponseEntity<List<Order>> findOrdersByCustomerId(@PathVariable int id) {
		// Log the method call with the customer ID
		logger.info("findOrdersByCustomerId method called for customer with ID: {}", id);

		Customer customer = customerService.findById(id);

		// Check if the customer is found
		if (customer == null) {
			logger.warn("Customer with ID {} not found.", id);
			throw new EntityNotFoundException("Customer with ID " + id + " not found");
		}

		// Log the successful retrieval of orders
		logger.info("Orders retrieved for customer with ID: {}", id);

		return new ResponseEntity<>(customer.getOrders(), HttpStatus.OK);
	}

	/**
	 * Retrieves all ratings for a specific customer by their ID.
	 *
	 * @param id The ID of the customer whose ratings are to be retrieved.
	 * @return A ResponseEntity containing a list of Rating objects and an HTTP
	 *         status code.
	 * @throws EntityNotFoundException if no customer is found with the specified
	 *                                 ID.
	 */
	@Operation(summary = "Retrieve ratings by customer ID", description = "Returns a list of ratings associated with the orders of the customer having the specified ID.")
	@GetMapping("/{id}/reviews")
	public ResponseEntity<List<Rating>> findRatingsByCustomerId(@PathVariable int id) {
		// Log the method call with the customer ID
		logger.info("findRatingsByCustomerId method called for customer with ID: {}", id);

		Customer customer = customerService.findById(id);

		// Check if the customer is found
		if (customer == null) {
			logger.warn("Customer with ID {} not found.", id);
			throw new EntityNotFoundException("Customer with ID " + id + " not found");
		}

		// Retrieve and collect ratings from the customer's orders
		List<Rating> ratings = customer.getOrders().stream().flatMap(order -> order.getRatings().stream())
				.collect(Collectors.toList());

		// Log the successful retrieval of ratings
		logger.info("Ratings retrieved for customer with ID: {}", id);

		return new ResponseEntity<>(ratings, HttpStatus.OK);
	}

	/**
	 * Adds a list of restaurants to the customer's favorites and updates the
	 * cookie.
	 *
	 * @param id                   The ID of the customer to update.
	 * @param request              The HTTP request containing the current cookies.
	 * @param response             The HTTP response to set the updated cookie.
	 * @param listOfRestaurantsDto The DTO containing the list of restaurants to add
	 *                             to favorites.
	 * @return A ResponseEntity with a success message and an HTTP status code.
	 * @throws EntityNotFoundException if the customer or any restaurant is not
	 *                                 found with the specified IDs.
	 */
	@Operation(summary = "Add restaurants to customer favorites", description = "Adds a list of restaurants to the customer's favorites and updates the corresponding cookie.")
	@PostMapping("/{id}/favorites")
	public ResponseEntity<String> addRestaurantsToCustomerFavorites(@PathVariable int id, HttpServletRequest request,
			HttpServletResponse response, @Valid @RequestBody ListOfRestaurantsDto listOfRestaurantsDto,
			BindingResult bindingResult) {

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

		// Log the method call with the customer ID
		logger.info("addRestaurantsToCustomerFavorites method called for customer with ID: {}", id);

		Customer customer = customerService.findById(id);

		// Check if the customer is found
		if (customer == null) {
			logger.warn("Customer with ID {} not found.", id);
			throw new EntityNotFoundException("Customer with ID " + id + " not found");
		}

		String restaurantIds = "";
		for (RestaurantDto restaurantDto : listOfRestaurantsDto.getRestaurants()) {
			Restaurant restaurant = restaurantService.findById(restaurantDto.getRestaurantId());

			// Check if the restaurant is found
			if (restaurant == null) {
				logger.warn("Restaurant with ID {} not found.", restaurantDto.getRestaurantId());
				throw new EntityNotFoundException(
						"Restaurant with ID " + restaurantDto.getRestaurantId() + " not found");
			}

			restaurantIds += restaurantDto.getRestaurantId() + "-";
		}

		String retrievedRestaurants = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("customer" + id + "Favorites")) {
					retrievedRestaurants = cookie.getValue();
					break;
				}
			}
		}

		// Append new restaurant IDs to the existing ones
		retrievedRestaurants += restaurantIds;
		restaurantIds=retrievedRestaurants;
		ResponseCookie cookie = ResponseCookie.from("customer" + id + "Favorites", retrievedRestaurants).httpOnly(true)
				.path("/").sameSite("None").build();

		response.addHeader("Set-Cookie", cookie.toString());

		// Log the successful addition of favorite restaurants
		logger.info("Customer's favorite restaurants updated successfully for customer ID: {}", id);

		return new ResponseEntity<>("Customer's favorite restaurants retrieved successfully.", HttpStatus.OK);
	}

	/**
	 * Removes a restaurant from the customer's favorites and updates the cookie.
	 *
	 * @param restaurantId The ID of the restaurant to remove from favorites.
	 * @param id           The ID of the customer.
	 * @param request      The HTTP request containing the current cookies.
	 * @param response     The HTTP response to set the updated cookie.
	 * @return A ResponseEntity with a success message or an error message and an
	 *         HTTP status code.
	 * @throws EntityNotFoundException if the customer is not found with the
	 *                                 specified ID.
	 */
	@Operation(summary = "Remove restaurant from customer favorites", description = "Removes a specified restaurant from the customer's favorites and updates the corresponding cookie.")
	@DeleteMapping("/{id}/favorites/{restaurantId}")
	public ResponseEntity<String> removeRestaurantFromCustomerFavorites(@PathVariable int restaurantId,
			@PathVariable int id, HttpServletRequest request, HttpServletResponse response) {

		// Log the method call with the customer ID and restaurant ID
		logger.info(
				"removeRestaurantFromCustomerFavorites method called for customer with ID: {} and restaurant with ID: {}",
				id, restaurantId);

		Customer customer = customerService.findById(id);

		// Check if the customer is found
		if (customer == null) {
			logger.warn("Customer with ID {} not found.", id);
			throw new EntityNotFoundException("Customer with ID " + id + " not found");
		}

		String retrievedRestaurants = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("customer" + id + "Favorites")) {
					retrievedRestaurants = cookie.getValue();
					break;
				}
			}
		} else {
			// Log if the cookie is not found
			logger.warn("Customer's cookie was not found for customer ID: {}", id);
			return new ResponseEntity<>("Customer's cookie was not found.", HttpStatus.NOT_FOUND);
		}

		// Remove the restaurant ID from the favorites list
		String restaurantIds = retrievedRestaurants.replace(restaurantId + "-", "");
		ResponseCookie cookie = ResponseCookie.from("customer" + id + "Favorites", restaurantIds).httpOnly(true)
				.path("/").build();

		response.addHeader("Set-Cookie", cookie.toString());

		// Log the successful removal of the restaurant from favorites
		logger.info("Restaurant with ID {} removed from customer's favorites successfully for customer ID: {}",
				restaurantId, id);

		return new ResponseEntity<>("Restaurant removed from customer's favorites successfully.", HttpStatus.OK);
	}

}