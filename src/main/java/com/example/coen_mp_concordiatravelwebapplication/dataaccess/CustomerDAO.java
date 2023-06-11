package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking;
import com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer;

import java.util.List;

public interface CustomerDAO {
    void addCustomer(Customer customer);

    List<Customer> getAllCustomers();

    Customer getSelectedCustomer(String customerId);

    List<Booking> getSeletedCustomerBookings(String customerId);


    boolean linkCustomerToAgent(int parseInt, int agentId);
    List<Customer> getCustomersByAgentId(int agentId);

}
