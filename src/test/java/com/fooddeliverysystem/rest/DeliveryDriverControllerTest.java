package com.fooddeliverysystem.rest;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddeliverysystem.dto.DeliveryDriverDto;
import com.fooddeliverysystem.entity.DeliveryDriver;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.service.DeliveryDriverService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(DeliveryDriverController.class)
public class DeliveryDriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryDriverService deliveryDriverService;

    @Autowired
    private ObjectMapper objectMapper;

    private DeliveryDriver driver;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Initialize and set properties for driver
        driver = new DeliveryDriver();
        driver.setDriverId(1);
        driver.setName("John Doe");

        // Initialize orders for the driver
        Order order = new Order(); // Initialize and set order properties if needed
        List<Order> orders = Arrays.asList(order);
        driver.setOrders(orders); // Set orders for the driver
    }


    @Test
    void testFindAll() throws Exception {
        List<DeliveryDriver> drivers = Arrays.asList(driver);

        when(deliveryDriverService.findAll()).thenReturn(drivers);

        mockMvc.perform(get("/api/drivers"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(drivers)));
    }

    @Test
    void testFindById() throws Exception {
        when(deliveryDriverService.findById(anyInt())).thenReturn(driver);

        mockMvc.perform(get("/api/drivers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(driver)));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(deliveryDriverService.findById(anyInt())).thenThrow(new EntityNotFoundException("Delivery Driver with ID 1 not found"));

        mockMvc.perform(get("/api/drivers/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateLocation() throws Exception {
        DeliveryDriverDto deliveryDriverDto = new DeliveryDriverDto();
        deliveryDriverDto.setLocation("Los Angeles");

        when(deliveryDriverService.findById(anyInt())).thenReturn(driver);

        mockMvc.perform(put("/api/drivers/{id}/location", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deliveryDriverDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(driver)));
    }

    @Test
    void testUpdateLocation_NotFound() throws Exception {
        DeliveryDriverDto deliveryDriverDto = new DeliveryDriverDto();
        deliveryDriverDto.setLocation("Los Angeles");

        when(deliveryDriverService.findById(anyInt())).thenThrow(new EntityNotFoundException("Delivery Driver with ID 1 not found"));

        mockMvc.perform(put("/api/drivers/{id}/location", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deliveryDriverDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindOrdersByDeliveryDriver() throws Exception {
        Order order = new Order(); // Initialize and set order properties
        List<Order> orders = Arrays.asList(order);

        // Set the orders for the delivery driver
        driver.setOrders(orders);

        // Mock the service to return the driver
        when(deliveryDriverService.findById(anyInt())).thenReturn(driver);

        mockMvc.perform(get("/api/drivers/{id}/orders", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orders)));
    }


    @Test
    void testFindOrdersByDeliveryDriver_NotFound() throws Exception {
        when(deliveryDriverService.findById(anyInt())).thenThrow(new EntityNotFoundException("Delivery Driver with ID 1 not found"));

        mockMvc.perform(get("/api/drivers/{id}/orders", 1))
                .andExpect(status().isNotFound());
    }
}

