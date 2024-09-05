package com.fooddeliverysystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.fooddeliverysystem.dto.DeliveryDriverDto;
import com.fooddeliverysystem.entity.DeliveryDriver;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.entity.Restaurant;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/driver")
public class DeliveryDriverMvc {

    // Logger instance for logging purposes
    private static final Logger logger = LoggerFactory.getLogger(DeliveryDriverMvc.class);

    // RestTemplate instance for making HTTP requests to other services
    RestTemplate restTemplate = new RestTemplate();
    @GetMapping("/search")
	public String showRestaurantById(@RequestParam("id") int id, Model model) {
		try {
			ResponseEntity<DeliveryDriver> response = restTemplate
					.getForEntity("http://localhost:8080/api/drivers/" + id, DeliveryDriver.class);
			List<DeliveryDriver> deliveryDrivers = new ArrayList<>();
			deliveryDrivers.add(response.getBody());
			model.addAttribute("deliveryDrivers", deliveryDrivers);
			return "driver/driver-list";
		} catch (HttpClientErrorException e) {
			// Handle HTTP errors (4xx)
			model.addAttribute("error", "Driver not found");
			List<DeliveryDriver> deliveryDrivers = restTemplate.getForObject("http://localhost:8080/api/drivers",
					List.class);
			model.addAttribute("deliveryDrivers", deliveryDrivers);
			return "driver/driver-list";
		}

	}
    // Method to show the list of delivery drivers
    @GetMapping
    public String showDeliveryDriverList(Model model) {
        logger.info("Fetching list of delivery drivers");
        List<DeliveryDriver> deliveryDrivers = restTemplate.getForObject("http://localhost:8080/api/drivers",
                List.class);
        model.addAttribute("deliveryDrivers", deliveryDrivers);
        return "driver/driver-list";
    }

    // Method to show the orders associated with a specific delivery driver
    @GetMapping("/showDriverOrders")
    public String showDriverOrders(@RequestParam("driverId") int driverId, Model model) {
        logger.info("Fetching orders for delivery driver with ID: {}", driverId);
        List<Order> orders = restTemplate.getForObject("http://localhost:8080/api/drivers/" + driverId + "/orders",
                List.class);
        DeliveryDriver deliveryDriver = restTemplate.getForObject("http://localhost:8080/api/drivers/" + driverId,
                DeliveryDriver.class);
        model.addAttribute("deliveryDriver", deliveryDriver);
        model.addAttribute("orders", orders);
        return "driver/orders-by-driver";
    }

    // Method to set the delivery location for a specific driver
    @GetMapping("/setDriverDeliveryLocation")
    public String setDriverDeliveryLocation(@RequestParam("driverId") int driverId, Model model) {
        logger.info("Setting delivery location for driver with ID: {}", driverId);
        DeliveryDriverDto deliveryDriverDto = new DeliveryDriverDto();
        deliveryDriverDto.setDeliveryDriverId(driverId);
        model.addAttribute("deliveryDriverDto", deliveryDriverDto);
        return "driver/driver-form-for-location";
    }

    // Method to process the form submission for setting the delivery location of a driver
    @PostMapping("/processSetLocationForDriver")
    public String processSetLocationForDriver(@Valid @ModelAttribute("deliveryDriverDto") DeliveryDriverDto deliveryDriverDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred while setting delivery location for driver with ID: {}", deliveryDriverDto.getDeliveryDriverId());
            deliveryDriverDto.setDeliveryDriverId(deliveryDriverDto.getDeliveryDriverId());
            model.addAttribute("deliveryDriverDto", deliveryDriverDto);
            return "driver/driver-form-for-location";
        }

        logger.info("Processing delivery location update for driver with ID: {}", deliveryDriverDto.getDeliveryDriverId());
        HttpEntity<DeliveryDriverDto> requestEntity = new HttpEntity<>(deliveryDriverDto);

        // Make the PUT request to update the delivery location of the driver
        ResponseEntity<DeliveryDriver> responseEntity = restTemplate.exchange(
                "http://localhost:8080/api/drivers/" + deliveryDriverDto.getDeliveryDriverId() + "/location",
                HttpMethod.PUT,
                requestEntity,
                DeliveryDriver.class
        );

        // Extract the updated delivery driver from the response
        DeliveryDriver deliveryDriver = responseEntity.getBody();
        model.addAttribute("deliveryDriver", deliveryDriver);

        logger.info("Fetching updated list of delivery drivers after location update");
        List<DeliveryDriver> deliveryDrivers = restTemplate.getForObject("http://localhost:8080/api/drivers",
                List.class);
        model.addAttribute("deliveryDrivers", deliveryDrivers);
        return "driver/driver-list";
    }
}
