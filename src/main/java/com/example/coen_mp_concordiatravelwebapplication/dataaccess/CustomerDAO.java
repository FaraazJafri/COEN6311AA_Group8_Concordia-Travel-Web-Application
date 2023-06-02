package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;

import java.util.List;

public interface CustomerDAO {
    void addCustomer(Customer customer);

    List<Customer> getAllCustomers();
}
