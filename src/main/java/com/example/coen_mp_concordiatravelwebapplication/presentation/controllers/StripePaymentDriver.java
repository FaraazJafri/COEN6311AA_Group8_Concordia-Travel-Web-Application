package com.example.coen_mp_concordiatravelwebapplication.presentation.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class StripePaymentDriver {

    public static void main(String[] args) {
        // Set your Stripe API key
        Stripe.apiKey = "sk_test_51NICzTDdDLNfwNaxborqZ7HglbK68LcQd2wrX49DyKs90oI6RsksSd27zNfQmGeHzPET9KOmoZNiEsmZ21xWIaaz00MEZ3XwoB";

        // Card details
        String cardNumber = "4000000000009995";
        String cardExpMonth = "12";
        String cardExpYear = "2024";
        String cardCvc = "123";

        // Create a PaymentIntent
        PaymentIntentCreateParams.Builder builder = new PaymentIntentCreateParams.Builder()
                .setAmount(1000L) // Payment amount in cents
                .setCurrency("usd")
                .setDescription("Test Payment")
                .setPaymentMethod("card");

        // Set the card details
        builder.putExtraParam("payment_method_data[type]", "card");
        builder.putExtraParam("payment_method_data[card][number]", cardNumber);
        builder.putExtraParam("payment_method_data[card][exp_month]", cardExpMonth);
        builder.putExtraParam("payment_method_data[card][exp_year]", cardExpYear);
        builder.putExtraParam("payment_method_data[card][cvc]", cardCvc);

        PaymentIntentCreateParams params = builder.build();

        try {
            // Create the PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            System.out.println("PaymentIntent created with ID: " + paymentIntent.getId());
        } catch (StripeException e) {
            e.printStackTrace();
            System.out.println("PaymentIntent creation failed: " + e.getMessage());
        }
    }
}
