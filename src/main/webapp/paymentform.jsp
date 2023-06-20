<!DOCTYPE html>
<html>
<head>
    <title>Checkout</title>
    <script src="https://js.stripe.com/v3/"></script
    <script src="checkout.js" defer></script>
    <style>
        #error-message {
            color: red;
        }
    </style>
</head>
<body>
<h1>Payment Form</h1>
<form id="payment-form">
    <div>
        <label for="card-element">Card Details</label>
        <div id="card-element">
            <!-- Stripe Card Element will be inserted here -->
        </div>
    </div>
    <button id="submit">Submit Payment</button>
    <div id="error-message"></div>
</form>

<script>
    const stripe = Stripe('pk_test_51NICzTDdDLNfwNaxJ9iBbcIRnaZEmZuBCK6X3JW3hSeHOvygb1FWqYXJ006dgGsPQxntwymrqarBLNgo9cGDp8dp00xb0IpoOt');
    const elements = stripe.elements();

    const cardElement = elements.create('card');
    cardElement.mount('#card-element');

    const form = document.getElementById('payment-form');
    const errorMessage = document.getElementById('error-message');

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const { paymentMethod, error } = await stripe.createPaymentMethod({
            type: 'card',
            card: cardElement,
        });

        if (error) {
            errorMessage.textContent = error.message;
        } else {
            // Send the payment method ID to your server
            // for further processing (e.g., creating a charge)
            console.log(paymentMethod);
            // ... You can handle the payment method ID here ...

            // Optionally, you can submit the form to your server
            // and handle the payment processing there
            // form.submit();
        }
    });
</script>
</body>
</html>
