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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fooddeliverysystem.dto.MenuItemDto;
import com.fooddeliverysystem.dto.RestaurantDto;
import com.fooddeliverysystem.entity.DeliveryAddress;
import com.fooddeliverysystem.entity.MenuItem;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;
import com.fooddeliverysystem.exception.ValidationException;
import com.fooddeliverysystem.service.MenuItemService;
import com.fooddeliverysystem.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
	private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);
	private RestaurantService restaurantService;
	private MenuItemService menuItemService;

	@Autowired
	public RestaurantController(RestaurantService restaurantService, MenuItemService menuItemService) {
		super();
		this.restaurantService = restaurantService;
		this.menuItemService = menuItemService;
	}

	/**
	 * Retrieves a list of all restaurants.
	 *
	 * @return ResponseEntity containing a list of Restaurant objects and HTTP
	 *         status OK.
	 */
	@Operation(summary = "Get all restaurants", description = "Fetches a list of all restaurants available in the system.")
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<List<Restaurant>> findAll() {
		logger.info("Fetching all restaurants");

		List<Restaurant> restaurants = restaurantService.findAll();

		logger.info("Found {} restaurants", restaurants.size());

		return new ResponseEntity<>(restaurants, HttpStatus.OK);
	}

	/**
	 * Retrieves a specific restaurant by its ID.
	 *
	 * @param id the ID of the restaurant to retrieve
	 * @return a ResponseEntity containing the restaurant if found, or a 404 error
	 *         if not found
	 * @throws EntityNotFoundException if the restaurant with the given ID is not
	 *                                 found
	 */
	@Operation(summary = "Find Restaurant by ID", description = "Retrieve a specific restaurant's details using its unique ID.")
	@GetMapping("/{id}")
	public ResponseEntity<Restaurant> findRestaurantById(@PathVariable int id) {
		// Log the request to find a restaurant by ID
		logger.info("Request to find restaurant with ID: {}", id);

		Restaurant restaurant = restaurantService.findById(id);

		// Check if the restaurant is found
		if (restaurant == null) {
			// Log that no restaurant was found and throw exception
			logger.error("No restaurant found with ID: {}", id);
			throw new EntityNotFoundException("No restaurant found with ID " + id);
		} else {
			// Log the success of finding the restaurant
			logger.info("Restaurant found with ID: {}", id);
			return new ResponseEntity<>(restaurant, HttpStatus.OK);
		}
	}

	/**
	 * Adds a new restaurant with the specified details and menu items.
	 *
	 * @param restaurantDto Data transfer object containing details of the
	 *                      restaurant and its menu items.
	 * @return A ResponseEntity containing the created restaurant and an HTTP status
	 *         code.
	 */
	@PostMapping
	@Operation(summary = "Add a new restaurant", description = "Creates a new restaurant with the given details and menu items.")
	public ResponseEntity<Restaurant> addRestaurant(@Valid @RequestBody RestaurantDto restaurantDto,
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

		// Log the incoming request for adding a new restaurant
		logger.info("Request received to add a new restaurant with name: {}", restaurantDto.getName());

		// Create a new Restaurant object
		Restaurant restaurant = new Restaurant();

		// Set the restaurant ID to one more than the last record's ID (Auto-increment
		// simulation)
		restaurant.setRestaurantId(restaurantService.findLastRecord().getRestaurantId() + 1);

		// Set the restaurant details from the received RestaurantDto
		restaurant.setName(restaurantDto.getName());
		restaurant.setAddress(restaurantDto.getAddress());
		restaurant.setPhone(restaurantDto.getPhone());

		// Save the new restaurant to the database
		restaurantService.save(restaurant);

		// Log the successful saving of the restaurant
		logger.info("Restaurant saved with ID: {}", restaurant.getRestaurantId());

		// Initialize an empty list to hold the menu items for the new restaurant
		List<MenuItem> menuItems = new ArrayList<>();

		// Iterate over the menu items provided in the RestaurantDto
		for (MenuItemDto menuItemDto : restaurantDto.getMenuItems()) {
			// Create a new MenuItem object
			MenuItem menuItem = new MenuItem();

			// Set the item ID to one more than the last record's ID (Auto-increment
			// simulation)
			menuItem.setItemId(menuItemService.findLastRecord().getItemId() + 1);

			// Set the menu item details from the received MenuItemDto
			menuItem.setDescription(menuItemDto.getDescription());
			menuItem.setName(menuItemDto.getName());
			menuItem.setPrice(menuItemDto.getPrice());

			// Associate the menu item with the newly created restaurant
			menuItem.setRestaurant(restaurant);

			// Set order items to null (assuming no orders are associated at creation)
			menuItem.setOrderItems(null);

			// Add the menu item to the list
			menuItems.add(menuItem);

			// Save the menu item to the database
			menuItemService.save(menuItem);

			// Log the successful saving of the menu item
			logger.info("Menu item saved with ID: {} for restaurant ID: {}", menuItem.getItemId(),
					restaurant.getRestaurantId());
		}

		// Associate the list of menu items with the restaurant
		restaurant.setMenuItems(menuItems);

		// Set orders and ratings to null (assuming no orders or ratings are associated
		// at creation)
		restaurant.setOrders(null);
		restaurant.setRatings(null);

		// Return the created restaurant with a 201 (Created) status
		logger.info("Restaurant created successfully with ID: {}", restaurant.getRestaurantId());
		return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
	}

	/**
	 * Updates the details of an existing restaurant, including its name, address,
	 * phone, and menu items.
	 *
	 * @param id            The ID of the restaurant to update.
	 * @param restaurantDto Data transfer object containing the updated details of
	 *                      the restaurant.
	 * @return A ResponseEntity containing the updated restaurant and an HTTP status
	 *         code.
	 */
	@PutMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Update an existing restaurant", description = "Updates the details of an existing restaurant by ID, including its name, address, phone, and menu items.")
	public ResponseEntity<Restaurant> updateRestaurant(@PathVariable int id,
			@RequestBody RestaurantDto restaurantDto, BindingResult bindingResult) {

		// Log the start of the update operation
		logger.info("Starting update operation for restaurant with ID {}", id);

		// Find the restaurant by its ID
		Restaurant restaurant = restaurantService.findById(id);

		// If the restaurant is not found, throw an exception
		if (restaurant == null) {
			logger.error("Restaurant with ID {} not found", id);
			throw new EntityNotFoundException("Restaurant with ID " + id + " not found");
		} else {
			// Update the restaurant's name if provided
			if (restaurantDto.getName() != null) {
				logger.info("Updating name of restaurant ID {} to {}", id, restaurantDto.getName());
				restaurant.setName(restaurantDto.getName());
			}

			// Update the restaurant's address if provided
			if (restaurantDto.getAddress() != null) {
				logger.info("Updating address of restaurant ID {}", id);
				restaurant.setAddress(restaurantDto.getAddress());
			}

			// Update the restaurant's phone if provided
			if (restaurantDto.getPhone() != null) {
				logger.info("Updating phone of restaurant ID {}", id);
				restaurant.setPhone(restaurantDto.getPhone());
			}

			// Update the restaurant's menu items
			List<MenuItem> menuItems = new ArrayList<>();
			for (MenuItemDto menuItemDto : restaurantDto.getMenuItems()) {
				// Find the menu item by its ID
				MenuItem menuItem = menuItemService.findById(menuItemDto.getMenuItemId());

				// If the menu item is not found, throw an exception
				if (menuItem == null) {
					logger.error("Menu Item with ID {} not found", menuItemDto.getMenuItemId());
					throw new EntityNotFoundException(
							"Menu Item with ID " + menuItemDto.getMenuItemId() + " not found");
				}

				// Update menu item details
				logger.info("Updating menu item with ID {} for restaurant ID {}", menuItemDto.getMenuItemId(), id);
				menuItem.setRestaurant(restaurant);
				menuItem.setDescription(menuItemDto.getDescription());
				menuItem.setName(menuItemDto.getName());
				menuItem.setPrice(menuItemDto.getPrice());

				// Save the updated menu item
				menuItemService.save(menuItem);
				menuItems.add(menuItem);
			}

			// Set the updated menu items to the restaurant
			restaurant.setMenuItems(menuItems);
		}

		// Save the updated restaurant and return the response
		logger.info("Successfully updated restaurant with ID {}", id);
		return new ResponseEntity<>(restaurantService.save(restaurant), HttpStatus.OK);
	}

	/**
	 * Deletes the restaurant with the specified ID from the system.
	 *
	 * @param id The ID of the restaurant to delete.
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Delete a restaurant by ID", description = "Deletes the restaurant with the specified ID from the system.")
	public void deleteRestaurantById(@PathVariable int id) {
		// Log the start of the delete operation
		logger.info("Attempting to delete restaurant with ID: {}", id);

		// Retrieve the restaurant entity by ID
		Restaurant restaurant = restaurantService.findById(id);

		// Check if the restaurant exists
		if (restaurant == null) {
			// Log if the restaurant is not found
			logger.error("Restaurant with ID {} not found", id);

			// Throw an exception if the restaurant is not found
			throw new EntityNotFoundException("No restaurant found with ID " + id);
		} else {
			// Log successful retrieval of the restaurant
			logger.info("Restaurant with ID {} found, proceeding to delete", id);

			// Delete the restaurant by ID
			restaurantService.deleteById(id);

			// Log successful deletion
			logger.info("Restaurant with ID {} successfully deleted", id);
		}
	}

	/**
	 * Returns a list of menu items for the restaurant identified by the given ID.
	 *
	 * @param id The ID of the restaurant.
	 * @return A list of menu items for the specified restaurant.
	 */
	@GetMapping("/{id}/menu")
	@Operation(summary = "Get menu items for a specific restaurant", description = "Returns a list of menu items for the restaurant identified by the given ID")
	public ResponseEntity<List<MenuItem>> findRestaurantMenuItemsById(@PathVariable int id) {
		logger.info("Fetching menu items for restaurant with ID: {}", id); // Log the incoming request

		// Retrieve the restaurant by its ID
		Restaurant restaurant = restaurantService.findById(id);

		// Check if the restaurant exists
		if (restaurant == null) {
			logger.error("No restaurant found with ID: {}", id); // Log the error
			throw new EntityNotFoundException("No restaurant found with ID " + id); // Throw exception if not found
		} else {
			logger.info("Restaurant found with ID: {}. Returning menu items.", id); // Log success
			// Return the list of menu items associated with the restaurant
			return new ResponseEntity<>(restaurantService.menuItemsById(id), HttpStatus.OK);
		}
	}

	/**
	 * Retrieves a list of reviews for a specific restaurant identified by the given
	 * ID.
	 *
	 * @param id The ID of the restaurant.
	 * @return A list of reviews for the specified restaurant.
	 */
	@GetMapping("/{id}/reviews")
	@Operation(summary = "Get reviews for a specific restaurant", description = "Retrieve a list of reviews for a restaurant specified by its ID. If no restaurant is found with the given ID, an EntityNotFoundException is thrown.")
	public ResponseEntity<List<Rating>> findRestaurantReviewsById(@PathVariable int id) {
		// Log entry into the method
		logger.info("Request received to find reviews for restaurant with ID: {}", id);

		// Retrieve the restaurant by ID
		Restaurant restaurant = restaurantService.findById(id);

		// If no restaurant is found, log the error and throw an exception
		if (restaurant == null) {
			logger.error("No restaurant found with ID: {}", id);
			throw new EntityNotFoundException("No restaurant found with ID " + id);
		}

		// Log successful retrieval of reviews
		logger.info("Returning reviews for restaurant with ID: {}", id);

		// Return the list of reviews for the restaurant
		return new ResponseEntity<>(restaurantService.reviewsById(id), HttpStatus.OK);
	}

	/**
	 * Retrieves the list of delivery areas served by a specific restaurant.
	 *
	 * @param id the ID of the restaurant
	 * @return a ResponseEntity containing a list of DeliveryAddress objects
	 *         representing the delivery areas served by the specified restaurant or
	 *         a not found error if the restaurant does not exist
	 */
	@Operation(summary = "Get delivery areas served by restaurant", description = "Returns a list of delivery areas that are served by the restaurant with the specified ID")
	@GetMapping("/{id}/delivery-areas")
	public ResponseEntity<List<DeliveryAddress>> findRestaurantDeliveryAreasById(@PathVariable int id) {
		// Logging the start of the method
		logger.info("Finding delivery areas for restaurant with ID: {}", id);

		// Find the restaurant by ID
		Restaurant restaurant = restaurantService.findById(id);

		// Check if the restaurant exists
		if (restaurant == null) {
			logger.error("Restaurant not found with ID: {}", id);
			throw new EntityNotFoundException("No restaurant found with ID " + id);
		} else {
			// Retrieve and return the delivery areas served by the restaurant
			return new ResponseEntity<>(restaurantService.deliveryAddressServedById(id), HttpStatus.OK);
		}
	}

	/**
	 * Adds a new menu item to the specified restaurant.
	 *
	 * @param id          The ID of the restaurant to which the menu item will be
	 *                    added.
	 * @param menuItemDto The details of the menu item to be added.
	 * @return The newly created MenuItem with HTTP status 201 Created.
	 */
	@PostMapping("/{id}/menu")
	@Operation(summary = "Add a menu item to a restaurant", description = "Adds a new menu item to the specified restaurant by restaurant ID.")
	public ResponseEntity<MenuItem> addRestaurantMenuItemsById(@PathVariable int id,
			@Valid @RequestBody MenuItemDto menuItemDto, BindingResult bindingResult) {

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

		// Fetch the restaurant by ID
		Restaurant restaurant = restaurantService.findById(id);
		MenuItem menuItem = null;

		// Check if the restaurant exists
		if (restaurant == null) {
			logger.error("No restaurant found with ID " + id);
			throw new EntityNotFoundException("No restaurant found with ID " + id);
		} else {
			// Create a new MenuItem and set its properties from the MenuItemDto
			menuItem = new MenuItem();
			menuItem.setRestaurant(restaurant); // Associate the menu item with the restaurant
			restaurant.getMenuItems().add(menuItem); // Add the menu item to the restaurant's menu
			menuItem.setItemId(menuItemService.findLastRecord().getItemId() + 1); // Generate new item ID
			menuItem.setDescription(menuItemDto.getDescription());
			menuItem.setName(menuItemDto.getName());
			menuItem.setPrice(menuItemDto.getPrice());
			menuItem.setOrderItems(null); // Initialize order items to null as it's not provided

			// Save the updated restaurant entity with the new menu item
			logger.info("Adding new menu item to restaurant with ID " + id);
			restaurantService.save(restaurant);
		}

		// Save the menu item and return the response
		logger.info(
				"New menu item added successfully with ID " + menuItem.getItemId() + " to restaurant with ID " + id);
		return new ResponseEntity<>(menuItemService.save(menuItem), HttpStatus.CREATED);
	}

	/**
	 * Updates a specific menu item for a restaurant.
	 *
	 * @param itemId      The ID of the menu item to be updated.
	 * @param id          The ID of the restaurant to which the menu item belongs.
	 * @param menuItemDto The details of the menu item to update.
	 * @return The updated MenuItem with HTTP status 200 OK.
	 */
	@PutMapping("/{id}/menu/{itemId}")
	@Operation(summary = "Update a specific menu item for a restaurant", description = "Updates the details of a menu item identified by itemId for a restaurant identified by id.")
	public ResponseEntity<MenuItem> updateRestaurantMenuItemsById(@PathVariable int itemId, @PathVariable int id,
			@Valid @RequestBody MenuItemDto menuItemDto, BindingResult bindingResult) {

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

		// Log entry into the method
		logger.info("Updating menu item with ID {} for restaurant with ID {}", itemId, id);

		// Find the restaurant by ID
		Restaurant restaurant = restaurantService.findById(id);
		MenuItem menuItem = null;

		// Check if the restaurant exists
		if (restaurant == null) {
			// Log error if restaurant is not found
			logger.error("Restaurant with ID {} not found", id);
			throw new EntityNotFoundException("No restaurant found with ID " + id);
		} else {
			// Find the menu item by itemId
			menuItem = menuItemService.findById(itemId);

			// Check if the menu item exists
			if (menuItem == null) {
				// Log error if menu item is not found
				logger.error("Menu item with ID {} not found", itemId);
				throw new EntityNotFoundException("Item not found at ID " + itemId);
			} else {
				// Update menu item details if they are provided in the request body
				if (menuItemDto.getDescription() != null) {
					menuItem.setDescription(menuItemDto.getDescription());
				}
				if (menuItemDto.getName() != null) {
					menuItem.setName(menuItemDto.getName());
				}
				if (menuItemDto.getPrice() != 0) {
					menuItem.setPrice(menuItemDto.getPrice());
				}
				// Log successful update
				logger.info("Menu item with ID {} for restaurant ID {} updated successfully", itemId, id);
			}
		}
		// Save the updated menu item and return the response
		return new ResponseEntity<>(menuItemService.save(menuItem), HttpStatus.OK);
	}

	/**
	 * Deletes a specific menu item from a restaurant.
	 *
	 * @param itemId The ID of the menu item to be deleted.
	 * @param id     The ID of the restaurant from which the menu item will be
	 *               deleted.
	 */
	@DeleteMapping("/{id}/menu/{itemId}")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Delete a menu item", description = "Deletes a specific menu item by its ID from a restaurant by its ID.")
	public void deleteRestaurantMenuItemsById(@PathVariable int itemId, @PathVariable int id) {
		// Log the request to delete a menu item
		logger.info("Request received to delete menu item with ID {} from restaurant with ID {}", itemId, id);

		// Find the restaurant by ID
		Restaurant restaurant = restaurantService.findById(id);
		if (restaurant == null) {
			// Log and throw exception if restaurant is not found
			logger.error("No restaurant found with ID {}", id);
			throw new EntityNotFoundException("No restaurant found with ID " + id);
		} else {
			// Log the deletion action
			logger.info("Deleting menu item with ID {} from restaurant with ID {}", itemId, id);

			// Delete the menu item by ID
			restaurantService.deleteMenuItemsById(itemId);

			// Log the successful deletion
			logger.info("Successfully deleted menu item with ID {} from restaurant with ID {}", itemId, id);
		}
	}

}