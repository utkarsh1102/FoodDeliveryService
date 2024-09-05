package com.fooddeliverysystem.rest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fooddeliverysystem.dao.DeliveryDriverDao;
import com.fooddeliverysystem.entity.DeliveryDriver;
import com.fooddeliverysystem.service.DeliveryDriverServiceImpl;

public class DeliveryDriverServiceTest {

    @Mock
    private DeliveryDriverDao deliveryDriverDao;

    @InjectMocks
    private DeliveryDriverServiceImpl deliveryDriverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test method for findById
    @Test
    void testFindById() {
        // Mock data
        DeliveryDriver deliveryDriver = new DeliveryDriver();
        deliveryDriver.setDriverId(1);
        when(deliveryDriverDao.findById(1)).thenReturn(Optional.of(deliveryDriver));

        // Call service method
        DeliveryDriver result = deliveryDriverService.findById(1);

        // Assert the result is as expected
        assertEquals(deliveryDriver, result);
    }

    // Test method for findAll
    @Test
    void testFindAll() {
        // Mock data
        List<DeliveryDriver> driverList = new ArrayList<>();
        driverList.add(new DeliveryDriver());
        when(deliveryDriverDao.findAll()).thenReturn(driverList);

        // Call service method
        List<DeliveryDriver> result = deliveryDriverService.findAll();

        // Assert the result is as expected
        assertEquals(driverList.size(), result.size());
    }

    // Test method for save
    @Test
    void testSave() {
        // Mock data
        DeliveryDriver deliveryDriver = new DeliveryDriver();
        when(deliveryDriverDao.save(deliveryDriver)).thenReturn(deliveryDriver);

        // Call service method
        DeliveryDriver result = deliveryDriverService.save(deliveryDriver);

        // Assert the result is as expected
        assertEquals(deliveryDriver, result);
    }
}

