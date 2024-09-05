package com.fooddeliverysystem.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddeliverysystem.dto.OrderDto;
import com.fooddeliverysystem.entity.Coupon;
import com.fooddeliverysystem.entity.Customer;
import com.fooddeliverysystem.entity.DeliveryDriver;
import com.fooddeliverysystem.entity.MenuItem;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.entity.OrderItem;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;
import com.fooddeliverysystem.service.CouponService;
import com.fooddeliverysystem.service.CustomerService;
import com.fooddeliverysystem.service.DeliveryDriverService;
import com.fooddeliverysystem.service.MenuItemService;
import com.fooddeliverysystem.service.OrderItemService;
import com.fooddeliverysystem.service.OrderService;
import com.fooddeliverysystem.service.RatingService;
import com.fooddeliverysystem.service.RestaurantService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@MockBean
	private CustomerService customerService;

	@MockBean
	private RestaurantService restaurantService;

	@MockBean
	private DeliveryDriverService deliveryDriverService;

	@MockBean
	private MenuItemService menuItemService;

	@MockBean
	private OrderItemService orderItemService;

	@MockBean
	private RatingService ratingService;

	@MockBean
	private CouponService couponService;

	@Autowired
	private ObjectMapper objectMapper;

	@InjectMocks
	private OrderController orderController;

	private Order order;
	private Customer customer;
	private Restaurant restaurant;
	private DeliveryDriver deliveryDriver;
	private MenuItem menuItem;
	private OrderItem orderItem;
	private Rating rating;
	private Coupon coupon;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		customer = new Customer();
		customer.setCustomerId(1);

		restaurant = new Restaurant();
		restaurant.setRestaurantId(1);

		deliveryDriver = new DeliveryDriver();
		deliveryDriver.setDriverId(1);
		deliveryDriver.setOrders(new ArrayList<>()); // Initialize orders list

		menuItem = new MenuItem();
		menuItem.setItemId(1);

		order = new Order();
		order.setOrderId(1);
		order.setCustomer(customer);
		order.setRestaurant(restaurant);

		orderItem = new OrderItem();
		orderItem.setOrderItemId(1);
		orderItem.setMenuItem(menuItem);
		orderItem.setOrder(order);

		rating = new Rating();
		rating.setRatingId(1);
		rating.setRestaurant(restaurant);
		rating.setOrder(order);

		coupon = new Coupon();
		coupon.setCouponId(1);

		List<OrderItem> orderItems = Arrays.asList(orderItem);
		List<Rating> ratings = Arrays.asList(rating);
		List<Coupon> coupons = Arrays.asList(coupon);

		order.setItems(orderItems);
		order.setRatings(ratings);
		order.setCoupons(coupons);
	}

	@Test
	void testFindOrderById() throws Exception {
		when(orderService.findById(anyInt())).thenReturn(order);

		mockMvc.perform(get("/api/orders/{id}", 1)).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(order)));
	}

	@Test
	void testFindOrderById_NotFound() throws Exception {
		when(orderService.findById(anyInt())).thenThrow(new EntityNotFoundException("Order not found at ID 1"));

		mockMvc.perform(get("/api/orders/{id}", 1)).andExpect(status().isNotFound());
	}

	@Test
	void testChangeOrderStatus() throws Exception {
		OrderDto orderDto = new OrderDto();
		orderDto.setOrderStatus("Completed");

		when(orderService.findById(anyInt())).thenReturn(order);
		when(orderService.save(any(Order.class))).thenReturn(order);

		mockMvc.perform(put("/api/orders/{id}/status", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDto))).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(order)));
	}

	@Test
	void testChangeOrderStatus_NotFound() throws Exception {
		OrderDto orderDto = new OrderDto();
		orderDto.setOrderStatus("Completed");

		when(orderService.findById(anyInt())).thenThrow(new EntityNotFoundException("Order not found at ID 1"));

		mockMvc.perform(put("/api/orders/{id}/status", 1).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderDto))).andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteOrder() throws Exception {
		// Prepare mock data
		Order order = new Order();
		order.setOrderId(1);

		// Mock service call
		when(orderService.findById(1)).thenReturn(order);

		// Perform DELETE request
		mockMvc.perform(delete("/api/orders/{id}", 1)).andExpect(status().isNoContent());

		verify(orderService, times(1)).deleteById(1);
	}

	@Test
	void testDeleteOrder_NotFound() throws Exception {
		when(orderService.findById(anyInt())).thenThrow(new EntityNotFoundException("Order not found at ID 1"));

		mockMvc.perform(delete("/api/orders/{id}", 1)).andExpect(status().isNotFound());
	}

	@Test
	void testAssignDriver() throws Exception {
		when(orderService.findById(anyInt())).thenReturn(order);
		when(deliveryDriverService.findById(anyInt())).thenReturn(deliveryDriver);
		when(orderService.save(any(Order.class))).thenReturn(order);
		when(deliveryDriverService.save(any(DeliveryDriver.class))).thenReturn(deliveryDriver);

		mockMvc.perform(put("/api/orders/{orderId}/assignDriver/{driverId}", 1, 1)).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(deliveryDriver)));
	}

	@Test
	void testAssignDriver_OrderNotFound() throws Exception {
		when(orderService.findById(anyInt())).thenThrow(new EntityNotFoundException("Order with ID 1 not found"));

		mockMvc.perform(put("/api/orders/{orderId}/assignDriver/{driverId}", 1, 1)).andExpect(status().isNotFound());
	}

	@Test
	void testAssignDriver_DriverNotFound() throws Exception {
		when(orderService.findById(anyInt())).thenReturn(order);
		when(deliveryDriverService.findById(anyInt()))
				.thenThrow(new EntityNotFoundException("Delivery Driver with ID 1 not found"));

		mockMvc.perform(put("/api/orders/{orderId}/assignDriver/{driverId}", 1, 1)).andExpect(status().isNotFound());
	}
}
