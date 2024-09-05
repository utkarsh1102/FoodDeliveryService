package com.fooddeliverysystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fooddeliverysystem.dto.CouponDto;
import com.fooddeliverysystem.dto.CustomerDto;
import com.fooddeliverysystem.dto.DeliveryDriverDto;
import com.fooddeliverysystem.dto.MenuItemDto;
import com.fooddeliverysystem.dto.OrderDto;
import com.fooddeliverysystem.dto.OrderItemDto;
import com.fooddeliverysystem.dto.RestaurantDto;
import com.fooddeliverysystem.entity.Coupon;
import com.fooddeliverysystem.entity.Customer;
import com.fooddeliverysystem.entity.DeliveryDriver;
import com.fooddeliverysystem.entity.MenuItem;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.entity.Restaurant;
import com.fooddeliverysystem.service.CouponService;
import com.fooddeliverysystem.service.MenuItemService;
import com.fooddeliverysystem.service.OrderService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/order")
public class OrderMvc {
	private MenuItemService menuItemService;
	private CouponService couponService;
	private OrderService orderService;
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	public OrderMvc(MenuItemService menuItemService, OrderService orderService, CouponService couponService) {
		super();
		this.menuItemService = menuItemService;
		this.orderService = orderService;
		this.couponService = couponService;
	}

	@GetMapping
	public String showOrdersList(Model model) {
		List<Order> orders = orderService.findAll();
		model.addAttribute("orders", orders);
		return "order/order-list";
	}
	
	@GetMapping("/search")
	public String showOrderById(@RequestParam("id") int id, Model model) {
		try {
			ResponseEntity<Order> response = restTemplate
					.getForEntity("http://localhost:8080/api/orders/" + id, Order.class);
			List<Order> orders = new ArrayList<>();
			orders.add(response.getBody());
			model.addAttribute("orders", orders);
			return "order/order-list";
		} catch (HttpClientErrorException e) {
			// Handle HTTP errors (4xx)
			model.addAttribute("error", "Order not found");
			List<Order> orders = orderService.findAll();
			model.addAttribute("orders", orders);
			return "order/order-list";
		}

	}
	
	@GetMapping("/deleteOrder")
	public String deleteOrder(@RequestParam("orderId") int orderId, Model model) {
		restTemplate.delete("http://localhost:8080/api/orders/" + orderId);
		List<Order> orders = orderService.findAll();
		model.addAttribute("orders", orders);
		return "order/order-list";
	}

	@GetMapping("/showChangeOrderStatus")
	public String showChangeOrderStatus(@RequestParam("orderId") int orderId, Model model) {
		Order order = restTemplate.getForObject("http://localhost:8080/api/orders/" + orderId, Order.class);
		model.addAttribute("order", order);
		return "order/change-order-status-form";
	}

	@PostMapping("/processOrderStatusChange")
	public String processOrderStatusChange(@Valid @ModelAttribute("order") Order order, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("order", order);
			return "order/change-order-status-form";
		}
		OrderDto orderDto = new OrderDto();
		orderDto.setOrderStatus(order.getOrderStatus());
		HttpEntity<OrderDto> requestEntity = new HttpEntity<>(orderDto);
		// Make the PUT request and get the response as a Product
		ResponseEntity<Order> responseEntity = restTemplate.exchange(
				"http://localhost:8080/api/orders/" + order.getOrderId() + "/status", HttpMethod.PUT, requestEntity,
				Order.class);
		// Extract the updated product from the response
		Order updatedOrder = responseEntity.getBody();
		model.addAttribute("order", updatedOrder);
		List<Order> orders = orderService.findAll();
		model.addAttribute("orders", orders);
		return "order/order-list";
	}

	@GetMapping("/showNewOrderForm")
	public String showNewOrderForm(Model model) {
		List<MenuItem> menuItems = menuItemService.findAll();
		List<Coupon> coupons = couponService.findAll();
		List<Customer> customers = restTemplate.getForObject("http://localhost:8080/api/customers", List.class);
		List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants", List.class);
		List<DeliveryDriver> deliveryDrivers = restTemplate.getForObject("http://localhost:8080/api/drivers",
				List.class);
		model.addAttribute("orderDto", new OrderDto());
		model.addAttribute("coupons", coupons);
		model.addAttribute("menuItems", menuItems);
		model.addAttribute("customers", customers);
		model.addAttribute("restaurants", restaurants);
		model.addAttribute("deliveryDrivers", deliveryDrivers);
		return "order/new-order-form";
	}

	@PostMapping("/showMenuItemsSelection")
	public String showMenuItemsSelection(@RequestParam(name = "customerId", required = false) Integer customerId,
			@RequestParam(name = "restaurantId", required = false) Integer restaurantId,
			@Valid @ModelAttribute("orderDto") OrderDto orderDto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			if (customerId == null || restaurantId == null)
				model.addAttribute("error", "You must select at least one customer/restaurant item.");
			List<MenuItem> menuItems = menuItemService.findAll();
			List<Coupon> coupons = couponService.findAll();
			List<Customer> customers = restTemplate.getForObject("http://localhost:8080/api/customers", List.class);
			List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants",
					List.class);
			List<DeliveryDriver> deliveryDrivers = restTemplate.getForObject("http://localhost:8080/api/drivers",
					List.class);
			model.addAttribute("orderDto", orderDto);
			model.addAttribute("coupons", coupons);
			model.addAttribute("menuItems", menuItems);
			model.addAttribute("customers", customers);
			model.addAttribute("restaurants", restaurants);
			model.addAttribute("deliveryDrivers", deliveryDrivers);
			return "order/new-order-form";
		}
		if (customerId == null || restaurantId == null) {
			model.addAttribute("error", "You must select at least one customer/restaurant item.");
			List<MenuItem> menuItems = menuItemService.findAll();
			List<Coupon> coupons = couponService.findAll();
			List<Customer> customers = restTemplate.getForObject("http://localhost:8080/api/customers", List.class);
			List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants",
					List.class);
			List<DeliveryDriver> deliveryDrivers = restTemplate.getForObject("http://localhost:8080/api/drivers",
					List.class);
			model.addAttribute("orderDto", orderDto);
			model.addAttribute("coupons", coupons);
			model.addAttribute("menuItems", menuItems);
			model.addAttribute("customers", customers);
			model.addAttribute("restaurants", restaurants);
			model.addAttribute("deliveryDrivers", deliveryDrivers);
			return "order/new-order-form";
		}
		CustomerDto customerDto = new CustomerDto();
		RestaurantDto restaurantDto = new RestaurantDto();
		customerDto.setCustomerId(customerId);
		restaurantDto.setRestaurantId(restaurantId);
		Restaurant restaurant = restTemplate.getForObject("http://localhost:8080/api/restaurants/" + restaurantId,
				Restaurant.class);
		List<MenuItem> menuItems = restTemplate
				.getForObject("http://localhost:8080/api/restaurants/" + restaurantId + "/menu", List.class);
		Customer customer = restTemplate.getForObject("http://localhost:8080/api/customers/" + customerId,
				Customer.class);
		orderDto.setCustomer(customerDto);
		orderDto.setRatings(new ArrayList<>());
		orderDto.setRestaurant(restaurantDto);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("customer", customer);
		model.addAttribute("orderDto", orderDto);
		model.addAttribute("menuItems", menuItems);
		List<Coupon> coupons = couponService.findAll();
		model.addAttribute("coupons", coupons);
		return "order/new-order-form-menu-selection";
	}

	@PostMapping("/placeOrder")
	public String placeOrder(@RequestParam("customerId") int customerId, @RequestParam("restaurantId") int restaurantId,
			@RequestParam(name="itemIds", required = false) List<String> itemIds, @RequestParam(name="quantities", required = false) List<String> quantities,
			@RequestParam(name="couponIds", required = false) List<String> couponIds, @ModelAttribute("orderDto") OrderDto orderDto,
			Model model) {
		if(itemIds == null) {
			model.addAttribute("error", "You must select at least one item to order.");
			List<MenuItem> menuItems = restTemplate
					.getForObject("http://localhost:8080/api/restaurants/" + restaurantId + "/menu", List.class);
			Restaurant restaurant = restTemplate.getForObject("http://localhost:8080/api/restaurants/" + restaurantId,
					Restaurant.class);
			model.addAttribute("restaurant", restaurant);
			Customer customer = restTemplate.getForObject("http://localhost:8080/api/customers/" + customerId,
					Customer.class);
			model.addAttribute("customer", customer);
			model.addAttribute("orderDto", orderDto);
			model.addAttribute("menuItems", menuItems);
			List<Coupon> coupons = couponService.findAll();
			model.addAttribute("coupons", coupons);
			return "order/new-order-form-menu-selection";
		}
		CustomerDto customerDto = new CustomerDto();
		RestaurantDto restaurantDto = new RestaurantDto();
		customerDto.setCustomerId(customerId);
		restaurantDto.setRestaurantId(restaurantId);
		orderDto.setCustomer(customerDto);
		orderDto.setRatings(new ArrayList<>());
		orderDto.setRestaurant(restaurantDto);
		List<OrderItemDto> items = new ArrayList<>();
		List<Integer> quantities2 = new ArrayList<>();
		for (String quantity : quantities) {
			if (quantity.isBlank())
				continue;
			else
				quantities2.add(Integer.parseInt(quantity));
		}
		for (int i = 0; i < itemIds.size(); i++) {
			OrderItemDto orderItemDto = new OrderItemDto();
			MenuItemDto menuItemDto = new MenuItemDto();
			menuItemDto.setMenuItemId(Integer.parseInt(itemIds.get(i)));
			orderItemDto.setMenuItem(menuItemDto);
			orderItemDto.setQuantity(quantities2.get(i));
			items.add(orderItemDto);
		}
		orderDto.setItems(items);
		List<CouponDto> coupons = new ArrayList<>();
		if(couponIds == null) {
			orderDto.setCoupons(new ArrayList<>());
		}
		else {
			for (String couponId : couponIds) {
				CouponDto couponDto = new CouponDto();
				couponDto.setCouponId(Integer.parseInt(couponId));
				coupons.add(couponDto);
			}
		}
		orderDto.setCoupons(coupons);
		orderDto.setRatings(new ArrayList<>());
		System.out.println(orderDto);
		Order order = restTemplate.postForObject("http://localhost:8080/api/orders", orderDto, Order.class);
		model.addAttribute("order", order);
		List<Order> orders = orderService.findAll();
		model.addAttribute("orders", orders);
		return "order/order-list";
	}

	@GetMapping("/showAssginDeliveryDriver")
	public String showAssginDeliveryDriver(@RequestParam(name="orderId") int orderId, Model model) {
		List<DeliveryDriver> deliveryDrivers = restTemplate.getForObject("http://localhost:8080/api/drivers",
				List.class);
		model.addAttribute("deliveryDrivers", deliveryDrivers);
		model.addAttribute("orderId", orderId);
		return "order/assign-delivery-driver";
	}

	@PostMapping("/processDeliveryDriverAssignment")
	public String processDeliveryDriverAssignment(@RequestParam("orderId") int orderId,
			@RequestParam(name="driverId", required = false) Integer driverId, Model model) {
		if(driverId == null) {
			model.addAttribute("error", "You must select at least one delivery driver to deliver order.");
			List<DeliveryDriver> deliveryDrivers = restTemplate.getForObject("http://localhost:8080/api/drivers",
					List.class);
			model.addAttribute("deliveryDrivers", deliveryDrivers);
			model.addAttribute("orderId", orderId);
			return "order/assign-delivery-driver";
		}
		HttpEntity<Void> requestEntity = new HttpEntity<>(null);
		ResponseEntity<DeliveryDriver> responseEntity = restTemplate.exchange(
				"http://localhost:8080/api/orders/" + orderId + "/assignDriver/" + driverId, HttpMethod.PUT,
				requestEntity, DeliveryDriver.class);
		// Extract the updated product from the response
		DeliveryDriver deliveryDriver = responseEntity.getBody();
		List<Order> orders = orderService.findAll();
		model.addAttribute("deliveryDriver", deliveryDriver);
		model.addAttribute("orders", orders);
		return "order/order-list";
	}
}
