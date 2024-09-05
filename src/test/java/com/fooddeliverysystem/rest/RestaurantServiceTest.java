package com.fooddeliverysystem.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fooddeliverysystem.dao.MenuItemDao;
import com.fooddeliverysystem.dao.RestaurantDao;
import com.fooddeliverysystem.entity.Customer;
import com.fooddeliverysystem.entity.DeliveryAddress;
import com.fooddeliverysystem.entity.MenuItem;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.entity.Rating;
import com.fooddeliverysystem.entity.Restaurant;
import com.fooddeliverysystem.service.RestaurantServiceImpl;

public class RestaurantServiceTest {

    @Mock
    private RestaurantDao restaurantDao;

    @Mock
    private MenuItemDao menuItemDao;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test method for findAll
    @Test
    void testFindAll() {
        // Mock data
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant());
        when(restaurantDao.findAll()).thenReturn(restaurantList);

        // Call service method
        List<Restaurant> result = restaurantService.findAll();

        // Assert the result is as expected
        assertEquals(restaurantList.size(), result.size());
    }

    // Test method for findById
    @Test
    void testFindById() {
        // Mock data
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(1);
        when(restaurantDao.findById(1)).thenReturn(Optional.of(restaurant));

        // Call service method
        Restaurant result = restaurantService.findById(1);

        // Assert the result is as expected
        assertEquals(restaurant, result);
    }

    // Test method for save
    @Test
    void testSave() {
        // Mock data
        Restaurant restaurant = new Restaurant();
        when(restaurantDao.save(restaurant)).thenReturn(restaurant);

        // Call service method
        Restaurant result = restaurantService.save(restaurant);

        // Assert the result is as expected
        assertEquals(restaurant, result);
    }

    // Test method for deleteById
    @Test
    void testDeleteById() {
        // Call service method
        restaurantService.deleteById(1);

        // Verify that the delete method was called
        verify(restaurantDao, times(1)).deleteById(1);
    }

    // Test method for menuItemsById
    @Test
    void testMenuItemsById() {
        // Mock data
        Restaurant restaurant = new Restaurant();
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem());
        restaurant.setMenuItems(menuItems);
        when(restaurantDao.findById(1)).thenReturn(Optional.of(restaurant));

        // Call service method
        List<MenuItem> result = restaurantService.menuItemsById(1);

        // Assert the result is as expected
        assertEquals(menuItems.size(), result.size());
    }

    // Test method for reviewsById
    @Test
    void testReviewsById() {
        // Mock data
        Restaurant restaurant = new Restaurant();
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating());
        restaurant.setRatings(ratings);
        when(restaurantDao.findById(1)).thenReturn(Optional.of(restaurant));

        // Call service method
        List<Rating> result = restaurantService.reviewsById(1);

        // Assert the result is as expected
        assertEquals(ratings.size(), result.size());
    }

    // Test method for deliveryAddressServedById
    @Test
    void testDeliveryAddressServedById() {
        // Mock data
        Restaurant restaurant = new Restaurant();
        Order order = new Order();
        Customer customer = new Customer();
        DeliveryAddress address = new DeliveryAddress();
        List<DeliveryAddress> addresses = new ArrayList<>();
        addresses.add(address);
        customer.setAddresses(addresses);
        order.setCustomer(customer);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        restaurant.setOrders(orders);
        when(restaurantDao.findById(1)).thenReturn(Optional.of(restaurant));

        // Call service method
        List<DeliveryAddress> result = restaurantService.deliveryAddressServedById(1);

        // Assert the result is as expected
        assertEquals(addresses.size(), result.size());
    }

    // Test method for findLastRecord
    @Test
    void testFindLastRecord() {
        // Mock data
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(10);
        when(restaurantDao.findFirstByOrderByRestaurantIdDesc()).thenReturn(restaurant);

        // Call service method
        Restaurant result = restaurantService.findLastRecord();

        // Assert the result is as expected
        assertEquals(restaurant, result);
    }

    // Test method for saveMenuItemsById
    @Test
    void testSaveMenuItemsById() {
        // Mock data
        Restaurant restaurant = new Restaurant();
        restaurant.setMenuItems(new ArrayList<>()); // Initialize the list to avoid NullPointerException
        
        MenuItem menuItem = new MenuItem();
        menuItem.setItemId(1);
        
        when(menuItemDao.findFirstByOrderByItemIdDesc()).thenReturn(menuItem);
        when(menuItemDao.save(menuItem)).thenReturn(menuItem);
        when(restaurantDao.save(restaurant)).thenReturn(restaurant);

        // Call service method
        MenuItem result = restaurantService.saveMenuItemsById(restaurant, menuItem);

        // Assert the result is as expected
        assertEquals(menuItem, result);
        assertEquals(2, menuItem.getItemId()); // Since the next ID should be incremented by 1
    }
    // Test method for deleteMenuItemsById
    @Test
    void testDeleteMenuItemsById() {
        // Mock data
        MenuItem menuItem = new MenuItem();
        menuItem.setItemId(1);
        when(menuItemDao.findById(1)).thenReturn(Optional.of(menuItem));

        // Call service method
        restaurantService.deleteMenuItemsById(1);

        // Verify that the delete method was called
        verify(menuItemDao, times(1)).deleteById(1);
    }
}


