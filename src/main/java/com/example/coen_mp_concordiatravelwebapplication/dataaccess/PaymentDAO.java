package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

public interface PaymentDAO {


    String processPayment(String totalPrice, String cardNumberOfCustomer);


}
