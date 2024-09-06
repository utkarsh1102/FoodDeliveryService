package com.fooddeliverysystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fooddeliverysystem.dto.CustomerDto;
import com.fooddeliverysystem.dto.DeliveryAddressDto;
import com.fooddeliverysystem.dto.ListOfRestaurantsDto;
import com.fooddeliverysystem.dto.OrderDto;
import com.fooddeliverysystem.dto.RestaurantDto;
import com.fooddeliverysystem.entity.Customer;
import com.fooddeliverysystem.entity.DeliveryAddress;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class CustomerMvc {

    // RestTemplate instance for making HTTP requests
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Display a list of all customers.
     * @param model the Model object to add attributes to
     * @return the name of the view template
     */
    @GetMapping
    public String showCustomerList(Model model) {
        // Fetch the list of customers from the REST API
        List<Customer> customers = restTemplate.getForObject("http://localhost:8080/api/customers", List.class);
        // Add the list of customers to the model
        model.addAttribute("customers", customers);
        return "customer/customer-list";
    }
    
    @GetMapping("/search")
	public String showCustomerById(@RequestParam("id") int id, Model model) {
		try {
			ResponseEntity<Customer> response = restTemplate
					.getForEntity("http://localhost:8080/api/customers/" + id, Customer.class);
			List<Customer> customers = new ArrayList<>();
			customers.add(response.getBody());
			model.addAttribute("customers", customers);
			return "customer/customer-list";
		} catch (HttpClientErrorException e) {
			// Handle HTTP errors (4xx)
			model.addAttribute("error", "Customer not found");
			List<Customer> customers = restTemplate.getForObject("http://localhost:8080/api/customers",
					List.class);
			model.addAttribute("customers", customers);
			return "customer/customer-list";
		}

	}

    /**
     * Display orders placed by a specific customer.
     * 
     * @param customerId the ID of the customer
     * @param model the Model object to add attributes to
     * @return the name of the view template
     */
    @GetMapping("/showOrdersByCustomer")
    public String showOrdersByCustomer(@RequestParam("customerId") int customerId, Model model) {
        // Fetch orders of the customer from the REST API
        List<Order> orders = restTemplate.getForObject("http://localhost:8080/api/customers/" + customerId + "/orders", List.class);
        // Fetch customer details from the REST API
        Customer customer = restTemplate.getForObject("http://localhost:8080/api/customers/" + customerId, Customer.class);
        // Add the customer and orders to the model
        model.addAttribute("customer", customer);
        model.addAttribute("orders", orders);
        return "customer/orders-by-customer";
    }

    /**
     * Delete a customer by their ID and refresh the customer list.
     * 
     * @param customerId the ID of the customer to delete
     * @param model the Model object to add attributes to
     * @return the name of the view template
     */
    @GetMapping("/deleteCustomer")
    public String deleteCustomer(@RequestParam("customerId") int customerId, Model model) {
        // Delete the customer using the REST API
        restTemplate.delete("http://localhost:8080/api/customers/" + customerId);
        // Fetch the updated list of customers
        List<Customer> customers = restTemplate.getForObject("http://localhost:8080/api/customers", List.class);
        // Add the updated list to the model
        model.addAttribute("customers", customers);
        return "customer/customer-list";
    }

    /**
     * Display reviews (ratings) given by a specific customer.
     * 
     * @param customerId the ID of the customer
     * @param model the Model object to add attributes to
     * @return the name of the view template
     */
    @GetMapping("/showReviewsByCustomer")
    public String showReviewsByCustomer(@RequestParam("customerId") int customerId, Model model) {
        // Fetch reviews (ratings) given by the customer
        List<Rating> ratings = restTemplate.getForObject("http://localhost:8080/api/customers/" + customerId + "/reviews", List.class);
        // Fetch customer details from the REST API
        Customer customer = restTemplate.getForObject("http://localhost:8080/api/customers/" + customerId, Customer.class);
        // Add the customer and ratings to the model
        model.addAttribute("customer", customer);
        model.addAttribute("ratings", ratings);
        return "customer/reviews-by-customer";
    }

    /**
     * Show the update form for a specific customer.
     * 
     * @param customerId the ID of the customer to update
     * @param model the Model object to add attributes to
     * @return the name of the view template
     */
    @GetMapping("/updateCustomer")
    public String updateCustomer(@RequestParam("customerId") int customerId, Model model) {
        // Fetch customer details from the REST API
        Customer customer = restTemplate.getForObject("http://localhost:8080/api/customers/" + customerId, Customer.class);
        // Create and populate a CustomerDto object
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customerId);
        customerDto.setEmail(customer.getEmail());
        customerDto.setName(customer.getName());
        customerDto.setPhone(customer.getPhone());
        List<DeliveryAddressDto> addresses = new ArrayList<>();
        for (DeliveryAddress address : customer.getAddresses()) {
            DeliveryAddressDto addressDto = new DeliveryAddressDto();
            addressDto.setAddressId(address.getAddressId());
            addressDto.setAddressLine1(address.getAddressLine1());
            addressDto.setAddressLine2(address.getAddressLine2());
            addressDto.setCity(address.getCity());
            addressDto.setCustomer(customerDto);
            addressDto.setPostal(address.getPostal());
            addressDto.setState(address.getState());
            addresses.add(addressDto);
        }
        customerDto.setAddresses(addresses);
        // Add the CustomerDto to the model
        model.addAttribute("customerDto", customerDto);
        return "customer/update-customer-form";
    }

    /**
     * Process the customer update form and send updated data to the REST API.
     * 
     * @param customerDto the DTO containing updated customer data
     * @param bindingResult the result of the validation process
     * @param model the Model object to add attributes to
     * @return the name of the view template
     */
    @PostMapping("/processUpdateCustomerForm")
    public String processUpdateCustomerForm(@Valid @ModelAttribute("customerDto") CustomerDto customerDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the update form
            model.addAttribute("customerDto", customerDto);
            return "customer/update-customer-form";
        }
        // Prepare the HTTP request entity with the updated customer data
        HttpEntity<CustomerDto> requestEntity = new HttpEntity<>(customerDto);
        // Make the PUT request to update the customer data
        ResponseEntity<Customer> responseEntity = restTemplate.exchange(
            "http://localhost:8080/api/customers/" + customerDto.getCustomerId(), 
            HttpMethod.PUT, 
            requestEntity, 
            Customer.class
        );
        // Extract the updated customer from the response
        Customer customer = responseEntity.getBody();
        // Add the updated customer and customer list to the model
        model.addAttribute("customer", customer);
        List<Customer> customers = restTemplate.getForObject("http://localhost:8080/api/customers", List.class);
        model.addAttribute("customers", customers);
        return "customer/customer-list";
    }

    /**
     * Show the form to select restaurants to add to the customer's favorites.
     * 
     * @param customerId the ID of the customer
     * @param model the Model object to add attributes to
     * @return the name of the view template
     */
    @GetMapping("/addRestaurantsToCustomerFavorites")
    public String addRestaurantsToCustomerFavorites(@RequestParam("customerId") int customerId, Model model) {
        // Fetch the list of restaurants from the REST API
        List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants", List.class);
        // Add the list of restaurants and customer ID to the model
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("customerId", customerId);
        return "customer/select-customer-favorites-form";
    }

    /**
     * Process the form for selecting restaurants to add to the customer's favorites.
     * 
     * @param customerId the ID of the customer
     * @param restaurantIds the list of selected restaurant IDs
     * @param model the Model object to add attributes to
     * @return the name of the view template
     */
    @PostMapping("/processCustomerFavoriteRestaurants")
    public String processCustomerFavoriteRestaurants(@RequestParam(name="customerId") int customerId, @RequestParam(name = "restaurantIds", required = false) List<String> restaurantIds, Model model) {
        if (restaurantIds == null) {
            // If no restaurants are selected, show an error message
            model.addAttribute("error", "You must select at least one restaurant.");
            List<Restaurant> restaurants = restTemplate.getForObject("http://localhost:8080/api/restaurants", List.class);
            model.addAttribute("restaurants", restaurants);
            model.addAttribute("customerId", customerId);
            return "customer/select-customer-favorites-form";
        }
        // Prepare the DTO for the list of selected restaurants
        ListOfRestaurantsDto listOfRestaurantsDto = new ListOfRestaurantsDto();
        List<RestaurantDto> restaurants = new ArrayList<>();
        for (String restaurantId : restaurantIds) {
            RestaurantDto restaurantDto = new RestaurantDto();
            restaurantDto.setRestaurantId(Integer.parseInt(restaurantId));
            restaurants.add(restaurantDto);
        }
        listOfRestaurantsDto.setRestaurants(restaurants);
        // Make the POST request to add the selected restaurants to customer favorites
        String message = restTemplate.postForObject("http://localhost:8080/api/customers/" + customerId + "/favorites", listOfRestaurantsDto, String.class);
        System.out.println(message);
        // Add the message and updated customer list to the model
        model.addAttribute("message", message);
        List<Customer> customers = restTemplate.getForObject("http://localhost:8080/api/customers", List.class);
        model.addAttribute("customers", customers);
        return "customer/customer-list";
    }
}
