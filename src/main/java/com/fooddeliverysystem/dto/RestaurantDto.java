package com.fooddeliverysystem.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RestaurantDto {
	private int restaurantId;
	@NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
	private String name;
	@NotBlank(message = "Address is mandatory")
    @Size(min = 2, max = 100, message = "Address must be between 2 and 100 characters")
	private String address;
	@NotBlank(message = "Phone number is mandatory")
    @Size(min = 10, max = 11, message = "Phone number must be between 10-11 characters")
	private String phone;
	private List<MenuItemDto> menuItems;
	
	public int getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<MenuItemDto> getMenuItems() {
		return menuItems;
	}
	public void setMenuItems(List<MenuItemDto> menuItems) {
		this.menuItems = menuItems;
	}
}
