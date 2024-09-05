package com.fooddeliverysystem.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DeliveryDriverDto {
	private int deliveryDriverId;
	@NotBlank(message = "Name is mandatory")
	@Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
	private String name;

	@NotBlank(message = "Phone number is mandatory")
	@Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
	private String phone;

	@NotBlank(message = "Vehicle is mandatory")
	@Size(min = 2, max = 20, message = "Vehicle description must be between 2 and 20 characters")
	private String vehicle;
	@NotBlank(message = "Location is mandatory")
	@Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
	private String location;
	private List<OrderDto> orders;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getDeliveryDriverId() {
		return deliveryDriverId;
	}

	public void setDeliveryDriverId(int deliveryDriverId) {
		this.deliveryDriverId = deliveryDriverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public List<OrderDto> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDto> orders) {
		this.orders = orders;
	}

}
