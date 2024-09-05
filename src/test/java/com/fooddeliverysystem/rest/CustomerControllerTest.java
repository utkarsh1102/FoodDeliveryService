package com.fooddeliverysystem.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddeliverysystem.dto.CustomerDto;
import com.fooddeliverysystem.dto.DeliveryAddressDto;
import com.fooddeliverysystem.dto.ListOfRestaurantsDto;
import com.fooddeliverysystem.dto.RestaurantDto;
import com.fooddeliverysystem.entity.Customer;
import com.fooddeliverysystem.entity.DeliveryAddress;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;
import com.fooddeliverysystem.service.CustomerService;
import com.fooddeliverysystem.service.DeliveryAddressService;
import com.fooddeliverysystem.service.RestaurantService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerService customerService;

	@MockBean
	private DeliveryAddressService deliveryAddressService;

	@MockBean
	private RestaurantService restaurantService;

	@Autowired
	private ObjectMapper objectMapper;

	private Customer customer;
	private DeliveryAddress address;
	private Order order;
	private Rating rating;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		customer = new Customer();
		customer.setCustomerId(1);
		customer.setName("Jane Doe");
		customer.setEmail("jane.doe@example.com");
		customer.setPhone("1234567890");

		address = new DeliveryAddress();
		address.setAddressId(1);
		address.setAddressLine1("123 Main St");
		address.setCity("Cityville");
		address.setState("Stateville");
		address.setPostal("12345");

		order = new Order();
		order.setOrderId(1);
		order.setRatings(Arrays.asList(new Rating()));

		customer.setAddresses(Arrays.asList(address));
		customer.setOrders(Arrays.asList(order));

		rating = new Rating();
		rating.setRatingId(1);
	}

	@Test
	void testFindAll() throws Exception {
		List<Customer> customers = Arrays.asList(customer);

		when(customerService.findAll()).thenReturn(customers);

		mockMvc.perform(get("/api/customers")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(customers)));
	}

	@Test
	void testFindById() throws Exception {
		when(customerService.findById(anyInt())).thenReturn(customer);

		mockMvc.perform(get("/api/customers/{id}", 1)).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(customer)));
	}

	@Test
	void testFindById_NotFound() throws Exception {
		when(customerService.findById(anyInt())).thenThrow(new EntityNotFoundException("Customer with ID 1 not found"));

		mockMvc.perform(get("/api/customers/{id}", 1)).andExpect(status().isNotFound());
	}

	@Test
	void testUpdateCustomer() throws Exception {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setName("Jane Smith");
		customerDto.setEmail("jane.smith@example.com");
		customerDto.setPhone("0987654321");
		customerDto.setAddresses(Arrays.asList(new DeliveryAddressDto()));

		when(customerService.findById(anyInt())).thenReturn(customer);
		when(deliveryAddressService.findById(anyInt())).thenReturn(address);
		when(customerService.save(any(Customer.class))).thenReturn(customer);

		mockMvc.perform(put("/api/customers/{id}", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDto))).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(customer)));
	}

	@Test
	void testUpdateCustomer_NotFound() throws Exception {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setName("Jane Smith");
		customerDto.setEmail("jane.smith@example.com");
		customerDto.setPhone("0987654321");

		when(customerService.findById(anyInt())).thenThrow(new EntityNotFoundException("Customer with ID 1 not found"));

		mockMvc.perform(put("/api/customers/{id}", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDto))).andExpect(status().isNotFound());
	}

	@Test
	void testDeleteCustomer_NotFound() throws Exception {
		when(customerService.findById(anyInt())).thenThrow(new EntityNotFoundException("Customer with ID 1 not found"));

		mockMvc.perform(delete("/api/customers/{id}", 1)).andExpect(status().isNotFound());
	}

	@Test
	void testFindOrdersByCustomerId() throws Exception {
		List<Order> orders = Arrays.asList(order);

		when(customerService.findById(anyInt())).thenReturn(customer);

		mockMvc.perform(get("/api/customers/{id}/orders", 1)).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(orders)));
	}

	@Test
	void testFindOrdersByCustomerId_NotFound() throws Exception {
		when(customerService.findById(anyInt())).thenThrow(new EntityNotFoundException("Customer with ID 1 not found"));

		mockMvc.perform(get("/api/customers/{id}/orders", 1)).andExpect(status().isNotFound());
	}

	@Test
	void testFindRatingsByCustomerId_NotFound() throws Exception {
		when(customerService.findById(anyInt())).thenThrow(new EntityNotFoundException("Customer with ID 1 not found"));

		mockMvc.perform(get("/api/customers/{id}/reviews", 1)).andExpect(status().isNotFound());
	}

	@Test
	void testAddRestaurantsToCustomerFavorites() throws Exception {
		ListOfRestaurantsDto listOfRestaurantsDto = new ListOfRestaurantsDto();
		listOfRestaurantsDto.setRestaurants(Arrays.asList(new RestaurantDto()));

		Restaurant restaurant = new Restaurant();
		restaurant.setRestaurantId(1);

		when(customerService.findById(anyInt())).thenReturn(customer);
		when(restaurantService.findById(anyInt())).thenReturn(restaurant);

		mockMvc.perform(post("/api/customers/{id}/favorites", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(listOfRestaurantsDto))).andExpect(status().isOk())
				.andExpect(content().string("Customer's favorite restaurants retrieved successfully."));
	}

	@Test
	void testAddRestaurantsToCustomerFavorites_NotFound() throws Exception {
		ListOfRestaurantsDto listOfRestaurantsDto = new ListOfRestaurantsDto();
		listOfRestaurantsDto.setRestaurants(Arrays.asList(new RestaurantDto()));

		when(customerService.findById(anyInt())).thenThrow(new EntityNotFoundException("Customer with ID 1 not found"));

		mockMvc.perform(post("/api/customers/{id}/favorites", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(listOfRestaurantsDto))).andExpect(status().isNotFound());
	}

	@Test
	void testRemoveRestaurantFromCustomerFavorites_NotFound() throws Exception {
		when(customerService.findById(anyInt())).thenThrow(new EntityNotFoundException("Customer with ID 1 not found"));

		mockMvc.perform(delete("/api/customers/{id}/favorites/{restaurantId}", 1, 1)).andExpect(status().isNotFound());
	}
}
