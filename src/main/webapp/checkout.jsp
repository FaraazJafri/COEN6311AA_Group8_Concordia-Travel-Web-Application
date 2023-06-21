<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <title>Accept a payment</title>
    <link rel="stylesheet" href="css/checkout.css"/>
    <script src="https://js.stripe.com/v3/"></script>

</head>
<body>

<jsp:include page="menu.jsp"/>

<%-- Retrieve values from GET request --%>
<% String customerId = (String) request.getAttribute("selectedCustomer"); %>
<% String agentId = (String) request.getAttribute("selectedAgent"); %>
<% String[] packageIds = (String[]) request.getAttribute("packageIds"); %>
<% String condition = (String) request.getAttribute("condition"); %>

<form id="payment-form" action="processPayment" method="post">

    <div>
        <label for="card-number">Card Number</label>
        <input type="text" id="card-number" name="cardNumber" required pattern="[0-9]{16}"
               title="Please enter a valid 16-digit card number."/>
    </div>
    <div>
        <label for="card-expiry">Card Expiry</label>
        <input type="text" id="card-expiry" name="cardExpiry" required pattern="(0[1-9]|1[0-2])\/[0-9]{2}"
               title="Please enter a valid expiry date in the format MM/YY."/>
    </div>
    <div>
        <label for="card-cvc">Card CVC</label>
        <input type="text" id="card-cvc" name="cardCVC" required pattern="[0-9]{3}"
               title="Please enter a valid 3-digit CVC number."/>
    </div>

    <div>
        <label for="payment-amount">Payment Amount:</label>
        <span id="payment-amount">${paymentAmount}</span>
        <input type="hidden" name="paymentAmount" value="${paymentAmount}"/>

        <input type="hidden" name="customerId" value="<%= customerId %>">
        <input type="hidden" name="agentId" value="<%= agentId %>">
        <% for (String packageId : packageIds) { %>
        <input type="hidden" name="packageIds" value="<%= packageId %>">
        <% } %>
        <input type="hidden" name="condition" value="<%= condition%>">

    </div>

    <button id="submit">
        <%--        <div class="spinner hidden" id="spinner"></div>--%>
        <span id="button-text">Pay now</span>
    </button>
    <div id="payment-message" class="hidden"></div>
</form>

</body>
</html>
