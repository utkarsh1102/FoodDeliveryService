package com.fooddeliverysystem.controller;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fooddeliverysystem.dto.CouponDto;
import com.fooddeliverysystem.dto.CustomerDto;
import com.fooddeliverysystem.dto.DeliveryDriverDto;
import com.fooddeliverysystem.dto.MenuItemDto;
import com.fooddeliverysystem.dto.OrderDto;
import com.fooddeliverysystem.dto.OrderItemDto;
import com.fooddeliverysystem.dto.RatingDto;
import com.fooddeliverysystem.dto.RestaurantDto;
import com.fooddeliverysystem.entity.Coupon;
import com.fooddeliverysystem.entity.Customer;
import com.fooddeliverysystem.entity.DeliveryAddress;
import com.fooddeliverysystem.entity.DeliveryDriver;
import com.fooddeliverysystem.entity.MenuItem;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.entity.OrderItem;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;
import com.fooddeliverysystem.service.CouponService;
import com.fooddeliverysystem.service.MenuItemService;
import com.fooddeliverysystem.service.OrderService;

@Controller
public class IndexMvc {
	private RestTemplate restTemplate = new RestTemplate();
	@GetMapping
	public String showHome(Model model) {
		List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants", List.class);
		model.addAttribute("restaurants", restaurants);
		return "restaurant/restaurant-list"; 
	}
}
