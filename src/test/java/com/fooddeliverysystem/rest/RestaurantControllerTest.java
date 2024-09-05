package com.fooddeliverysystem.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
import com.fooddeliverysystem.dto.MenuItemDto;
import com.fooddeliverysystem.dto.RestaurantDto;
import com.fooddeliverysystem.entity.MenuItem;
import com.fooddeliverysystem.entity.Restaurant;
import com.fooddeliverysystem.service.MenuItemService;
import com.fooddeliverysystem.service.RestaurantService;
 
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {
 
    @Autowired
    private MockMvc mockMvc;
 
    @MockBean
    private RestaurantService restaurantService;
 
    @MockBean
    private MenuItemService menuItemService;
 
    @Autowired
    private ObjectMapper objectMapper;
 
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    public void testFindAllRestaurants() throws Exception {
        // Prepare mock data
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1);
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test St");
        restaurant.setPhone("1234567890");
 
        List<Restaurant> restaurants = Arrays.asList(restaurant);
 
        // Mock service call
        when(restaurantService.findAll()).thenReturn(restaurants);
 
        // Perform GET request
        mockMvc.perform(get("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Restaurant"));
    }
 
    @Test
    public void testFindRestaurantById() throws Exception {
        // Prepare mock data
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1);
        restaurant.setName("Test Restaurant");
 
        // Mock service call
        when(restaurantService.findById(1)).thenReturn(restaurant);
 
        // Perform GET request
        mockMvc.perform(get("/api/restaurants/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"));
    }
 
    @Test
    public void testAddRestaurant() throws Exception {
        // Prepare mock data
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("New Restaurant");
        restaurantDto.setAddress("123 Test St");
        restaurantDto.setPhone("1234567890");
        MenuItemDto menuItemDto = new MenuItemDto();
        menuItemDto.setName("Pasta");
        menuItemDto.setPrice(10.0);
        restaurantDto.setMenuItems(Arrays.asList(menuItemDto));
 
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1);
        restaurant.setName("New Restaurant");
 
        MenuItem menuItem = new MenuItem();
        menuItem.setItemId(1); // Set a non-null itemId
        menuItem.setName("Pasta");
        menuItem.setPrice(10.0);
        restaurant.setMenuItems(Arrays.asList(menuItem));
 
        // Mock service calls
        when(restaurantService.save(any(com.fooddeliverysystem.entity.Restaurant.class))).thenReturn(restaurant);
        when(restaurantService.findLastRecord()).thenReturn(restaurant);
        // Mock the MenuItemService to return a MenuItem with a non-null itemId
        MenuItem lastMenuItem = new MenuItem();
        lastMenuItem.setItemId(1); // Mock itemId
        when(menuItemService.findLastRecord()).thenReturn(lastMenuItem);
 
        // Perform POST request
        mockMvc.perform(post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Restaurant"));
    }
 
 
    @Test
    public void testUpdateRestaurant() throws Exception {
        // Prepare mock data
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setName("Updated Restaurant");
        restaurantDto.setMenuItems(new ArrayList<>()); // Ensure menuItems is initialized
 
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1);
        restaurant.setName("Original Restaurant");
 
        // Mock service call
        when(restaurantService.findById(1)).thenReturn(restaurant);
        when(restaurantService.save(any(Restaurant.class))).thenReturn(restaurant);
 
        // Perform PUT request
        mockMvc.perform(put("/api/restaurants/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Restaurant")); // Updated value should be reflected
    }
 
 
    @Test
    public void testDeleteRestaurant() throws Exception {
        // Prepare mock data
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1);
        restaurant.setName("Test Restaurant");
 
        // Mock service call
        when(restaurantService.findById(1)).thenReturn(restaurant);
 
        // Perform DELETE request
        mockMvc.perform(delete("/api/restaurants/{id}", 1))
                .andExpect(status().isOk());
 
        verify(restaurantService, times(1)).deleteById(1);
    }

 
    @Test
    public void testUpdateRestaurantMenuItemsById() throws Exception {
        // Prepare mock data
        MenuItemDto menuItemDto = new MenuItemDto();
        menuItemDto.setName("Pizza");
        menuItemDto.setDescription("Delicious pizza");
        menuItemDto.setPrice(10.0);
 
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1);
 
        MenuItem menuItem = new MenuItem();
        menuItem.setItemId(1);
        menuItem.setName("Pasta");
 
        // Mock service calls
        when(restaurantService.findById(1)).thenReturn(restaurant);
        when(menuItemService.findById(1)).thenReturn(menuItem);
        when(menuItemService.save(any(MenuItem.class))).thenReturn(menuItem);
 
        // Perform PUT request to update menu item
        mockMvc.perform(put("/api/restaurants/1/menu/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }
 
    @Test
    public void testDeleteRestaurantMenuItemsById() throws Exception {
        // Prepare mock data
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1);
 
        // Mock service call
        when(restaurantService.findById(1)).thenReturn(restaurant);
 
        // Perform DELETE request
        mockMvc.perform(delete("/api/restaurants/{id}/menu/{itemId}", 1, 1))
                .andExpect(status().isOk());
 
        verify(restaurantService, times(1)).deleteMenuItemsById(1);
    }
}