package com.fooddeliverysystem.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@Column(name = "customer_id")
	private int customerId;
	
	@Column(name = "customer_name")
	private String name;
	@Column(name = "customer_email")
	private String email;
	@Column(name = "customer_phone")
	private String phone;
	
	@OneToMany (mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orders;
	
	@OneToMany (mappedBy = "customer", cascade = CascadeType.ALL)
	private List<DeliveryAddress> addresses;
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public List<DeliveryAddress> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<DeliveryAddress> addresses) {
		this.addresses = addresses;
	}
}
