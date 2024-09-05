package com.fooddeliverysystem.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.fooddeliverysystem.dao.OrderDao;
import com.fooddeliverysystem.entity.Order;
import com.fooddeliverysystem.service.OrderServiceImpl;

public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test method for findLastRecord
    @Test
    void testFindLastRecord() {
        // Mock data
        Order order = new Order();
        order.setOrderId(10);
        when(orderDao.findFirstByOrderByOrderIdDesc()).thenReturn(order);

        // Call service method
        Order result = orderService.findLastRecord();

        // Assert the result is as expected
        assertEquals(order, result);
    }

    // Test method for findById
    @Test
    void testFindById() {
        // Mock data
        Order order = new Order();
        order.setOrderId(1);
        when(orderDao.findById(1)).thenReturn(Optional.of(order));

        // Call service method
        Order result = orderService.findById(1);

        // Assert the result is as expected
        assertEquals(order, result);
    }

    @Test
    void testFindByIdNotFound() {
        // Mock repository method to return empty
        when(orderDao.findById(1)).thenReturn(Optional.empty());

        // Call service method
        Order result = orderService.findById(1);

        // Assert the result is null
        assertNull(result);
    }

    // Test method for findAll
    @Test
    void testFindAll() {
        // Mock data
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order());
        when(orderDao.findAll()).thenReturn(orderList);

        // Call service method
        List<Order> result = orderService.findAll();

        // Assert the result is as expected
        assertEquals(orderList.size(), result.size());
    }

    // Test method for save
    @Test
    void testSave() {
        // Mock data
        Order order = new Order();
        when(orderDao.save(order)).thenReturn(order);

        // Call service method
        Order result = orderService.save(order);

        // Assert the result is as expected
        assertEquals(order, result);
    }

    // Test method for deleteById
    @Test
    void testDeleteById() {
        // Call service method
        orderService.deleteById(1);

        // Verify that the delete method was called
        verify(orderDao, times(1)).deleteById(1);
    }
}

