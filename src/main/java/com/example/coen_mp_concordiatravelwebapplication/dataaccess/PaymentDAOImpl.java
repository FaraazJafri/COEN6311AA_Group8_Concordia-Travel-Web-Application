package com.example.coen_mp_concordiatravelwebapplication.dataaccess;

import com.example.coen_mp_concordiatravelwebapplication.config.STRIPECONFIG;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class PaymentDAOImpl implements PaymentDAO {


    @Override
    public String processPayment(String totalPrice, String cardNumberOfCustomer) {
        // Retrieve the Stripe secret key
        String secretKey = STRIPECONFIG.SECRET_KEY;

        // Set your Stripe API key
        Stripe.apiKey = secretKey;

        try {


            double paymentAmount = Double.parseDouble(totalPrice);
            long paymentAmountInCents = convertDollarsToCents((long) paymentAmount);

            String cardNumber = cardNumberOfCustomer.trim();
            cardNumber = cardNumber.replaceAll("\\s", "");


            if (cardNumber.equals(STRIPECONFIG.successCardNumber)) {
                PaymentIntentCreateParams confirmParams = PaymentIntentCreateParams.builder()
                        .setCurrency("cad")
                        .setAmount((long) paymentAmountInCents)
                        .setPaymentMethod("pm_card_visa")
                        .setConfirm(true)
                        .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                        .build();

                PaymentIntent paymentIntent = PaymentIntent.create(confirmParams);


                return paymentIntent.getStatus();

            } else if (cardNumber.equals(STRIPECONFIG.declineCardNumber)) {
                PaymentIntentCreateParams confirmParams = PaymentIntentCreateParams.builder()
                        .setCurrency("cad")
                        .setAmount((long) paymentAmountInCents)
                        .setPaymentMethod("pm_card_visa_chargeDeclined")
                        .setConfirm(true)
                        .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                        .build();

                PaymentIntent paymentIntent = PaymentIntent.create(confirmParams);

                return paymentIntent.getStatus();
            } else if (cardNumber.equals(STRIPECONFIG.insufficientBalanceCard)) {
                PaymentIntentCreateParams confirmParams = PaymentIntentCreateParams.builder()
                        .setCurrency("cad")
                        .setAmount((long) paymentAmountInCents)
                        .setPaymentMethod("pm_card_visa_chargeDeclinedInsufficientFunds")
                        .setConfirm(true)
                        .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                        .build();

                PaymentIntent paymentIntent = PaymentIntent.create(confirmParams);

                return paymentIntent.getStatus();
            }


        } catch (StripeException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "Invalid Card";
    }

    long convertDollarsToCents(long amountInDollars) {
        long amountInCents = Math.round(amountInDollars * 100);
        return amountInCents;
    }

}

