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

import com.fooddeliverysystem.dto.MenuItemDto;
import com.fooddeliverysystem.dto.RestaurantDto;
import com.fooddeliverysystem.entity.DeliveryAddress;
import com.fooddeliverysystem.entity.MenuItem;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;
import com.fooddeliverysystem.service.CouponService;
import com.fooddeliverysystem.service.MenuItemService;
import com.fooddeliverysystem.service.OrderService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/restaurant")
public class RestaurantMvc {
	private MenuItemService menuItemService;
	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	public RestaurantMvc(MenuItemService menuItemService) {
		super();
		this.menuItemService = menuItemService;
	}

	@GetMapping
	public String showRestaurantList(Model model) {
		List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants", List.class);
		model.addAttribute("restaurants", restaurants);
		return "restaurant/restaurant-list";
	}

	@GetMapping("/search")
	public String showRestaurantById(@RequestParam("id") int id, Model model) {
		try {
			ResponseEntity<Restaurant> response = restTemplate
					.getForEntity("http://localhost:8080/api/restaurants/" + id, Restaurant.class);
			List<Restaurant> restaurants = new ArrayList<>();
			restaurants.add(response.getBody());
			model.addAttribute("restaurants", restaurants);
			return "restaurant/restaurant-list";
		} catch (HttpClientErrorException e) {
			// Handle HTTP errors (4xx)
			model.addAttribute("error", "Restaurant not found");
			List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants",
					List.class);
			model.addAttribute("restaurants", restaurants);
			return "restaurant/restaurant-list";
		}

	}

	@GetMapping("/showNewRestaurantForm")
	public String showNewRestaurantForm(Model model) {
		model.addAttribute("restaurantDto", new RestaurantDto());
		model.addAttribute("menuItems", menuItemService.findAll());
		model.addAttribute("itemIds", new ArrayList<>());
		return "restaurant/new-restaurant-form";
	}

	@PostMapping("/processNewRestaurantForm")
	public String processNewRestaurantForm(@RequestParam(name = "itemIds", required = false) List<String> itemIds,
			Model model, @Valid @ModelAttribute("restaurantDto") RestaurantDto restaurantDto,
			BindingResult bindingResult) {
		if (itemIds == null || itemIds.isEmpty()) {
			model.addAttribute("error", "You must select at least one menu item.");
			model.addAttribute("restaurantDto", restaurantDto);
			model.addAttribute("menuItems", menuItemService.findAll());
			model.addAttribute("itemIds", new ArrayList<>());
			return "restaurant/new-restaurant-form"; // Return the form with an error message
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("restaurantDto", restaurantDto);
			model.addAttribute("menuItems", menuItemService.findAll());
			model.addAttribute("itemIds", new ArrayList<>());
			return "restaurant/new-restaurant-form";
		}
		List<MenuItemDto> menuItems = new ArrayList<>();
		for (String itemId : itemIds) {
			MenuItem menuItem = menuItemService.findById(Integer.parseInt(itemId));
			MenuItemDto menuItemDto = new MenuItemDto();
			menuItemDto.setMenuItemId(menuItem.getItemId());
			menuItemDto.setName(menuItem.getName());
			menuItemDto.setDescription(menuItem.getDescription());
			menuItemDto.setPrice(menuItem.getPrice());
			menuItemDto.setOrderItems(null);
			menuItems.add(menuItemDto);
		}
		restaurantDto.setMenuItems(menuItems);
		Restaurant restaurant = restTemplate.postForObject("http://localhost:8080/api/restaurants", restaurantDto,
				Restaurant.class);
		model.addAttribute("restaurant", restaurant);
		List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants", List.class);
		model.addAttribute("restaurants", restaurants);
		return "restaurant/restaurant-list";
	}

	@PostMapping("/processUpdateRestaurantForm")
	public String processUpdateRestaurantForm(@RequestParam(name = "itemIds", required = false) List<String> itemIds,
			Model model, @Valid @ModelAttribute("restaurantDto") RestaurantDto restaurantDto,
			BindingResult bindingResult) {
		if (itemIds == null || itemIds.isEmpty()) {
			model.addAttribute("error", "You must select at least one menu item.");
			model.addAttribute("restaurantDto", restaurantDto);
			model.addAttribute("menuItems", menuItemService.findAll());
			model.addAttribute("itemIds", itemIds);
			return "restaurant/new-restaurant-form"; // Return the form with an error message
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("restaurantDto", restaurantDto);
			model.addAttribute("menuItems", menuItemService.findAll());
			model.addAttribute("itemIds", itemIds);
			return "restaurant/new-restaurant-form";
		}
		List<MenuItemDto> menuItems = new ArrayList<>();
		for (String itemId : itemIds) {
			MenuItem menuItem = menuItemService.findById(Integer.parseInt(itemId));
			MenuItemDto menuItemDto = new MenuItemDto();
			menuItemDto.setMenuItemId(menuItem.getItemId());
			menuItemDto.setName(menuItem.getName());
			menuItemDto.setDescription(menuItem.getDescription());
			menuItemDto.setPrice(menuItem.getPrice());
			menuItemDto.setOrderItems(null);
			menuItems.add(menuItemDto);
		}
		restaurantDto.setMenuItems(menuItems);
		HttpEntity<RestaurantDto> requestEntity = new HttpEntity<>(restaurantDto);
		// Make the PUT request and get the response as a Product
		ResponseEntity<Restaurant> responseEntity = restTemplate.exchange(
				"http://localhost:8080/api/restaurants/" + restaurantDto.getRestaurantId(), HttpMethod.PUT,
				requestEntity, Restaurant.class);
		// Extract the updated product from the response
		Restaurant restaurant = responseEntity.getBody();
		model.addAttribute("restaurant", restaurant);
		List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants", List.class);
		model.addAttribute("restaurants", restaurants);
		return "restaurant/restaurant-list";
	}

	@GetMapping("/showUpdateFormForRestaurant")
	public String showUpdateFormForRestaurant(@RequestParam("restaurantId") int restaurantId, Model model) {
		Restaurant restaurant = restTemplate.getForObject("http://localhost:8080/api/restaurants/" + restaurantId,
				Restaurant.class);
		RestaurantDto restaurantDto = new RestaurantDto();
		restaurantDto.setAddress(restaurant.getAddress());
		List<Integer> itemIds = new ArrayList<>();
		for (MenuItem menuItem : restaurant.getMenuItems()) {
			itemIds.add(menuItem.getItemId());
		}
		restaurantDto.setName(restaurant.getName());
		restaurantDto.setPhone(restaurant.getPhone());
		restaurantDto.setRestaurantId(restaurant.getRestaurantId());
		model.addAttribute("itemIds", itemIds);
		model.addAttribute("restaurantDto", restaurantDto);
		model.addAttribute("menuItems", menuItemService.findAll());
		return "restaurant/new-restaurant-form-update-only";
	}

	@GetMapping("/showMenuForRestaurant")
	public String showMenuForRestaurant(@RequestParam("restaurantId") int restaurantId, Model model) {
		List<MenuItem> menuItems = restTemplate
				.getForObject("http://localhost:8080/api/restaurants/" + restaurantId + "/menu", List.class);
		Restaurant restaurant = restTemplate.getForObject("http://localhost:8080/api/restaurants/" + restaurantId,
				Restaurant.class);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("menuItems", menuItems);
		model.addAttribute("restaurantId", restaurantId);
		return "restaurant/menu-for-restaurant";
	}

	@GetMapping("/deleteRestaurant")
	public String deleteRestaurant(@RequestParam("restaurantId") int restaurantId, Model model) {
		restTemplate.delete("http://localhost:8080/api/restaurants/" + restaurantId);
		List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants", List.class);
		model.addAttribute("restaurants", restaurants);
		return "restaurant/restaurant-list";
	}

	@GetMapping("/showNewItemForm")
	public String showNewItemForm(@RequestParam("restaurantId") int restaurantId, Model model) {
		model.addAttribute("menuItemDto", new MenuItemDto());
		model.addAttribute("restaurantId", restaurantId);
		return "restaurant/new-item-form-for-restaurant.html";
	}

	@PostMapping("/processNewItemFormForRestaurant")
	public String processNewItemFormForRestaurant(@RequestParam("restaurantId") int restaurantId, Model model,
			@Valid @ModelAttribute("menuItemDto") MenuItemDto menuItemDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("menuItemDto", menuItemDto);
			model.addAttribute("restaurantId", restaurantId);
			return "restaurant/new-item-form-for-restaurant.html";
		}
		MenuItem menuItem = restTemplate.postForObject(
				"http://localhost:8080/api/restaurants/" + restaurantId + "/menu", menuItemDto, MenuItem.class);
		model.addAttribute("menuItem", menuItem);
		List<MenuItem> menuItems = restTemplate
				.getForObject("http://localhost:8080/api/restaurants/" + restaurantId + "/menu", List.class);
		Restaurant restaurant = restTemplate.getForObject("http://localhost:8080/api/restaurants/" + restaurantId,
				Restaurant.class);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("menuItems", menuItems);
		model.addAttribute("restaurantId", restaurantId);
		return "restaurant/menu-for-restaurant";
	}

	@PostMapping("/processUpdateItemFormForRestaurant")
	public String processUpdateItemFormForRestaurant(@RequestParam("itemId") int itemId,
			@RequestParam("restaurantId") int restaurantId, Model model,
			@Valid @ModelAttribute("menuItemDto") MenuItemDto menuItemDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("menuItemDto", menuItemDto);
			model.addAttribute("restaurantId", restaurantId);
			return "restaurant/new-item-form-for-restaurant-update-only.html";
		}
		// MenuItem menuItem =
		// restTemplate.postForObject("http://localhost:8080/api/restaurants/" +
		// restaurantId + "/menu", menuItemDto, MenuItem.class);
		HttpEntity<MenuItemDto> requestEntity = new HttpEntity<>(menuItemDto);
		// Make the PUT request and get the response as a Product
		ResponseEntity<MenuItem> responseEntity = restTemplate.exchange(
				"http://localhost:8080/api/restaurants/" + restaurantId + "/menu/" + itemId, HttpMethod.PUT,
				requestEntity, MenuItem.class);
		// Extract the updated product from the response
		MenuItem menuItem = responseEntity.getBody();
		model.addAttribute("menuItem", menuItem);
		List<MenuItem> menuItems = restTemplate
				.getForObject("http://localhost:8080/api/restaurants/" + restaurantId + "/menu", List.class);
		Restaurant restaurant = restTemplate.getForObject("http://localhost:8080/api/restaurants/" + restaurantId,
				Restaurant.class);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("menuItems", menuItems);
		model.addAttribute("restaurantId", restaurantId);
		return "restaurant/menu-for-restaurant";
	}

	@GetMapping("/showUpdateFormForItem")
	public String showUpdateFormForItem(@RequestParam("restaurantId") int restaurantId,
			@RequestParam("itemId") int itemId, Model model) {
		MenuItem menuItem = menuItemService.findById(itemId);
		MenuItemDto menuItemDto = new MenuItemDto();
		menuItemDto.setMenuItemId(menuItem.getItemId());
		menuItemDto.setDescription(menuItem.getDescription());
		menuItemDto.setName(menuItem.getName());
		menuItemDto.setPrice(menuItem.getPrice());
		model.addAttribute("menuItemDto", menuItemDto);
		model.addAttribute("restaurantId", restaurantId);
		return "restaurant/new-item-form-for-restaurant-update-only.html";
	}

	@GetMapping("/deleteItem")
	public String deleteItem(@RequestParam("restaurantId") int restaurantId, @RequestParam("itemId") int itemId,
			Model model) {
		restTemplate.delete("http://localhost:8080/api/restaurants/" + restaurantId + "/menu/" + itemId);
		Restaurant restaurant = restTemplate.getForObject("http://localhost:8080/api/restaurants/" + restaurantId,
				Restaurant.class);
		List<MenuItem> menuItems = restTemplate
				.getForObject("http://localhost:8080/api/restaurants/" + restaurantId + "/menu", List.class);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("menuItems", menuItems);
		model.addAttribute("restaurantId", restaurantId);
		return "restaurant/menu-for-restaurant";
	}

	@GetMapping("/showReviewsForRestaurant")
	public String showReviewsForRestaurant(@RequestParam("restaurantId") int restaurantId, Model model) {
		List<Rating> ratings = restTemplate
				.getForObject("http://localhost:8080/api/restaurants/" + restaurantId + "/reviews", List.class);
		Restaurant restaurant = restTemplate.getForObject("http://localhost:8080/api/restaurants/" + restaurantId,
				Restaurant.class);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("ratings", ratings);
		return "restaurant/reviews-for-restaurant";
	}

	@GetMapping("/showDeliveryAreasForRestaurant")
	public String showDeliveryAreasForRestaurant(@RequestParam("restaurantId") int restaurantId, Model model) {
		List<DeliveryAddress> deliveryAddresses = restTemplate
				.getForObject("http://localhost:8080/api/restaurants/" + restaurantId + "/delivery-areas", List.class);
		Restaurant restaurant = restTemplate.getForObject("http://localhost:8080/api/restaurants/" + restaurantId,
				Restaurant.class);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("deliveryAddresses", deliveryAddresses);
		return "restaurant/delivery-addresses-for-restaurant";
	}
}
