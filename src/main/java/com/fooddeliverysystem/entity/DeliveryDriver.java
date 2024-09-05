package com.fooddeliverysystem.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "deliverydrivers")
public class DeliveryDriver {
	@Id
	@Column(name = "driver_id")
	private int driverId;

	@Column(name = "driver_name")
	private String name;
	@Column(name = "driver_phone")
	private String phone;
	@Column(name = "driver_vehicle")
	private String vehicle;
	@OneToMany(mappedBy = "deliveryDriver", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orders;

	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
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

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
}
