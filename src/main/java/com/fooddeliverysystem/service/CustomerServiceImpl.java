package com.fooddeliverysystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fooddeliverysystem.dao.CustomerDao;
import com.fooddeliverysystem.entity.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	private CustomerDao customerDao;

	@Autowired
	public CustomerServiceImpl(CustomerDao customerDao) {
		super();
		this.customerDao = customerDao;
	}

	@Override
	/**
	 * Retrieve the most recently added customer based on the highest customer ID.
	 * 
	 * @return The most recently added customer, or null if no customers exist.
	 */
	public Customer findLastRecord() {
		logger.info("Attempting to find the most recently added customer.");

		// Retrieve the customer with the highest ID
		Customer lastCustomer = customerDao.findFirstByOrderByCustomerIdDesc();

		if (lastCustomer == null) {
			logger.warn("No customers found in the database.");
		} else {
			logger.info("Most recent customer found with ID: {}", lastCustomer.getCustomerId());
		}

		return lastCustomer;
	}

	/**
	 * Find a customer by their ID.
	 * 
	 * @param id The ID of the customer to find.
	 * @return The customer with the specified ID, or null if no customer is found.
	 */
	@Override
	public Customer findById(int id) {
		logger.info("Attempting to find customer with ID: {}", id);

		// Retrieve the customer from the database
		Customer customer = customerDao.findById(id).orElse(null);

		if (customer == null) {
			logger.warn("No customer found with ID: {}", id);
		} else {
			logger.info("Customer found with ID: {}", id);
		}

		return customer;
	}

	/**
	 * Retrieve all customers from the database.
	 * 
	 * @return A list of all customers.
	 */
	@Override
	public List<Customer> findAll() {
		logger.info("Fetching all customers from the database.");

		List<Customer> customers = customerDao.findAll();

		logger.info("Number of customers fetched: {}", customers.size());

		return customers;
	}

	/**
	 * Save a customer to the database.
	 * 
	 * @param customer The customer object to be saved.
	 * @return The saved customer object with updated details (e.g., generated ID).
	 */
	@Override
	public Customer save(Customer customer) {
		logger.info("Attempting to save customer: {}", customer);

		Customer savedCustomer = customerDao.save(customer);

		logger.info("Customer saved successfully with ID: {}", savedCustomer.getCustomerId());

		return savedCustomer;
	}

	/**
	 * Delete a customer from the database by their ID.
	 * 
	 * @param id The ID of the customer to be deleted.
	 */
	@Override
	public void deleteById(int id) {
		logger.info("Attempting to delete customer with ID: {}", id);

		// Perform the deletion
		customerDao.deleteById(id);

		logger.info("Customer with ID {} deleted successfully.", id);
	}

}
