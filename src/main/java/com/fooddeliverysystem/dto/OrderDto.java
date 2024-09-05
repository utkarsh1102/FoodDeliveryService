package com.fooddeliverysystem.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrderDto {
	@NotNull(message = "Order Date is mandatory")
	private LocalDateTime orderDate;
	private CustomerDto customer;
	private RestaurantDto restaurant;
	private DeliveryDriverDto deliveryDriver;
	private List<OrderItemDto> items;
	private List<RatingDto> ratings;
	private List<CouponDto> coupons;
	@NotBlank(message = "Order Status is mandatory")
	@Size(min = 2, max = 10, message = "Order Status must be between 2 and 10 characters")
	private String orderStatus;
	
	public CustomerDto getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}
	public RestaurantDto getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(RestaurantDto restaurant) {
		this.restaurant = restaurant;
	}
	public DeliveryDriverDto getDeliveryDriver() {
		return deliveryDriver;
	}
	public void setDeliveryDriver(DeliveryDriverDto deliveryDriver) {
		this.deliveryDriver = deliveryDriver;
	}
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public List<OrderItemDto> getItems() {
		return items;
	}
	public void setItems(List<OrderItemDto> items) {
		this.items = items;
	}
	public List<RatingDto> getRatings() {
		return ratings;
	}
	public void setRatings(List<RatingDto> ratings) {
		this.ratings = ratings;
	}
	public List<CouponDto> getCoupons() {
		return coupons;
	}
	public void setCoupons(List<CouponDto> coupons) {
		this.coupons = coupons;
	}
	@Override
	public String toString() {
		return "OrderDto [orderDate=" + orderDate + ", customer=" + customer + ", restaurant=" + restaurant
				+ ", deliveryDriver=" + deliveryDriver + ", items=" + items + ", ratings=" + ratings + ", coupons="
				+ coupons + ", orderStatus=" + orderStatus + "]";
	}
	
}
