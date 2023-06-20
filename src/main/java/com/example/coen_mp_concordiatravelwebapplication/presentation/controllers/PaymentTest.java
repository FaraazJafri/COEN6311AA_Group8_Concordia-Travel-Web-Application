package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentMethodCreateParams;

public class PaymentTest {
    public static void main(String[] args) {
        String cardNumber = "4000000000000002";
        String expMonth = "12";
        String expYear = "2024";
        String cvc = "123";

        // Set your Stripe API key
        Stripe.apiKey = "sk_test_51NICzTDdDLNfwNaxborqZ7HglbK68LcQd2wrX49DyKs90oI6RsksSd27zNfQmGeHzPET9KOmoZNiEsmZ21xWIaaz00MEZ3XwoB";

        PaymentMethodCreateParams.CardDetails card = PaymentMethodCreateParams.CardDetails.builder()
                .setNumber(cardNumber)
                .setExpMonth(Long.valueOf(expMonth))
                .setExpYear(Long.valueOf(expYear))
                .setCvc(cvc)
                .build();

        PaymentMethodCreateParams paymentMethodCreateParams = PaymentMethodCreateParams.builder()
                .setType(PaymentMethodCreateParams.Type.CARD)
                .setCard(card)
                .build();

        try {
            PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodCreateParams);
            System.out.println("Payment method created: " + paymentMethod.getId());

            // Proceed with the payment flow

        } catch (StripeException e) {
            e.printStackTrace();
            System.out.println("Payment failed: " + e.getMessage());

            // Handle the StripeException and display appropriate error message
        }
    }
}
