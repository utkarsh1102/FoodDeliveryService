package com.fooddeliverysystem.service;

import java.util.List;

import com.fooddeliverysystem.entity.Customer;

/**
 * Service interface for managing customers. This interface defines methods for
 * performing operations related to customer entities.
 */
public interface CustomerService {

	/**
	 * Saves a customer to the database.
	 *
	 * This method persists the provided Customer object. If the customer already
	 * exists, it will be updated; otherwise, a new record will be created.
	 *
	 * @param customer The Customer object to be saved.
	 * @return The saved Customer object, including any updates or new
	 *         database-generated fields.
	 */
	Customer save(Customer customer);

	/**
	 * Retrieves the most recently added customer from the database.
	 *
	 * This method queries the database to find the customer with the highest ID,
	 * which represents the most recently added customer.
	 *
	 * @return The most recently added Customer object, or null if no customers are
	 *         found.
	 */
	Customer findLastRecord();

	/**
	 * Finds a customer by its ID.
	 *
	 * This method retrieves a Customer from the database using its unique ID. If no
	 * customer is found with the provided ID, the method returns null.
	 *
	 * @param id The unique ID of the customer to be retrieved.
	 * @return A Customer object if found, otherwise null.
	 */
	Customer findById(int id);

	/**
	 * Retrieves a list of all customers from the database.
	 *
	 * This method queries the database to find all customer records.
	 *
	 * @return A list of Customer objects.
	 */
	List<Customer> findAll();

	/**
	 * Deletes a customer by its ID.
	 *
	 * This method removes the customer with the specified ID from the database.
	 *
	 * @param id The unique ID of the customer to be deleted.
	 */
	void deleteById(int id);
}
