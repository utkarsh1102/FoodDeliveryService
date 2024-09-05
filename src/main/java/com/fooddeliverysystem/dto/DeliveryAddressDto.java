package com.fooddeliverysystem.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;

public class DeliveryAddressDto {
	private int addressId;
	@NotBlank(message = "Address Line 1 is mandatory")
	@Size(max = 100, message = "Address Line 1 must be less than 100 characters")
	private String addressLine1;

	@Size(max = 100, message = "Address Line 2 must be less than 100 characters")
	private String addressLine2;

	@NotBlank(message = "City is mandatory")
	@Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
	private String city;

	@NotBlank(message = "State is mandatory")
	@Size(min = 2, max = 20, message = "State must be between 2 and 20 characters")
	private String state;

	@NotBlank(message = "Postal code is mandatory")
    @Size(min = 5, max = 10, message = "Postal code must be between 5 and 10 characters")
    private String postal;

	private CustomerDto customer;

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public CustomerDto getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}
}
