package com.fooddeliverysystem.dto;

public class OrderItemDto {
	private OrderDto order;
	private MenuItemDto menuItem;
	private int quantity;

	public OrderDto getOrder() {
		return order;
	}
	public void setOrder(OrderDto order) {
		this.order = order;
	}
	public MenuItemDto getMenuItem() {
		return menuItem;
	}
	public void setMenuItem(MenuItemDto menuItem) {
		this.menuItem = menuItem;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
