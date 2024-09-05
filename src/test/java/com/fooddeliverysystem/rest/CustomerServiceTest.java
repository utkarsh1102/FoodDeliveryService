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

import com.fooddeliverysystem.dao.CustomerDao;
import com.fooddeliverysystem.entity.Customer;
import com.fooddeliverysystem.service.CustomerServiceImpl;

public class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test method for findLastRecord
    @Test
    void testFindLastRecord() {
        // Mock data
        Customer customer = new Customer();
        customer.setCustomerId(10);
        when(customerDao.findFirstByOrderByCustomerIdDesc()).thenReturn(customer);

        // Call service method
        Customer result = customerService.findLastRecord();

        // Assert the result is as expected
        assertEquals(customer, result);
    }

    // Test method for findById
    @Test
    void testFindById() {
        // Mock data
        Customer customer = new Customer();
        customer.setCustomerId(1);
        when(customerDao.findById(1)).thenReturn(Optional.of(customer));

        // Call service method
        Customer result = customerService.findById(1);

        // Assert the result is as expected
        assertEquals(customer, result);
    }
    // Test method for findAll
    @Test
    void testFindAll() {
        // Mock data
        List<Customer> customerList = new ArrayList<>();
        customerList.add(new Customer());
        when(customerDao.findAll()).thenReturn(customerList);

        // Call service method
        List<Customer> result = customerService.findAll();

        // Assert the result is as expected
        assertEquals(customerList.size(), result.size());
    }

    // Test method for save
    @Test
    void testSave() {
        // Mock data
        Customer customer = new Customer();
        when(customerDao.save(customer)).thenReturn(customer);

        // Call service method
        Customer result = customerService.save(customer);

        // Assert the result is as expected
        assertEquals(customer, result);
    }

    // Test method for deleteById
    @Test
    void testDeleteById() {
        // Call service method
        customerService.deleteById(1);

        // Verify that the delete method was called
        verify(customerDao, times(1)).deleteById(1);
    }
}
