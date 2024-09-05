package com.fooddeliverysystem.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerDto {
	private int customerId;
	@NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
	private String name;
	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email not valid")
	private String email;
	@NotBlank(message = "Phone number is mandatory")
    @Size(min = 10, max = 11, message = "Phone number must be between 10-11 characters")
	private String phone;
	private List<DeliveryAddressDto> addresses;
	
	public List<DeliveryAddressDto> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<DeliveryAddressDto> addresses) {
		this.addresses = addresses;
	}
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
}
