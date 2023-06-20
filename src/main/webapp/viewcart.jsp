<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.userModels.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart</title>
    <link rel="stylesheet" type="text/css" href="css/viewcart.css">
</head>
<body>
<jsp:include page="menu.jsp"/>

<% if (session.getAttribute("role").equals("Admin") || session.getAttribute("role").equals("Agent")) { %>
<form action="ViewCartServlet" method="POST">
    <label for="customerId">Select a Customer:</label>
    <select id="customerId" name="customerId">
        <option value="" selected>-- Select Customer --</option>
        <% List<User> customers = (List<User>) request.getAttribute("customers");
            if (customers != null && !customers.isEmpty()) {
                for (User customer : customers) { %>
        <option value="<%= customer.getUserId() %>"> Id:<%= customer.getUserId() %>
            Name: <%= customer.getFirstName() %> <%= customer.getLastName() %>
        </option>
        <% }
        } %>
    </select>
    <input type="hidden" name="condition" value="View">
    <input type="submit" value="View Cart">
</form>
<% } %>


<% List<TravelPackage> travelPackages = (List<TravelPackage>) request.getAttribute("travelPackages");
    if (travelPackages != null && !travelPackages.isEmpty()) { %>
<table>
    <thead>
    <tr>
        <th>Package ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Total Price</th>
        <th>Flights</th>
        <th>Hotels</th>
        <th>Activities</th>
        <th>Remove</th>
    </tr>
    </thead>
    <tbody>
    <% for (TravelPackage travelPackage : travelPackages) { %>
    <tr>
        <td><%= travelPackage.getPackageId() %>
        </td>
        <td><%= travelPackage.getName() %>
        </td>
        <td><%= travelPackage.getDescription() %>
        </td>
        <td><%= travelPackage.getPrice() %>
        </td>
        <td>
            <% List<Flight> flights = travelPackage.getFlights();
                if (flights != null && !flights.isEmpty()) { %>
            <ul>
                <% for (Flight flight : flights) { %>
                <li><strong>Flight ID:</strong> <%= flight.getFlightId() %>
                </li>
                <li><strong>Airline:</strong> <%= flight.getAirline() %>
                </li>
                <li><strong>Departure:</strong> <%= flight.getDeparture() %>
                </li>
                <li><strong>Arrival:</strong> <%= flight.getArrival() %>
                </li>
                <li><strong>Price:</strong> <%= flight.getPrice() %>
                </li>
                <% } %>
            </ul>
            <% } else { %>
            No flights found.
            <% } %>
        </td>
        <td>
            <% List<Hotel> hotels = travelPackage.getHotels();
                if (hotels != null && !hotels.isEmpty()) { %>
            <ul>
                <% for (Hotel hotel : hotels) { %>
                <li><strong>Hotel ID:</strong> <%= hotel.getHotelId() %>
                </li>
                <li><strong>Name:</strong> <%= hotel.getName() %>
                </li>
                <li><strong>Location:</strong> <%= hotel.getLocation() %>
                </li>
                <li><strong>Price:</strong> <%= hotel.getPrice() %>
                </li>
                <% } %>
            </ul>
            <% } else { %>
            No hotels found.
            <% } %>
        </td>
        <td>
            <% List<Activity> activities = travelPackage.getActivities();
                if (activities != null && !activities.isEmpty()) { %>
            <ul>
                <% for (Activity activity : activities) { %>
                <li><strong>Activity ID:</strong> <%= activity.getActivityId() %>
                </li>
                <li><strong>Name:</strong> <%= activity.getName() %>
                </li>
                <li><strong>Description:</strong> <%= activity.getDescription() %>
                </li>
                <li><strong>Price:</strong> <%= activity.getPrice() %>
                </li>
                <% } %>
            </ul>
            <% } else { %>
            No activities found.
            <% } %>
        </td>
        <td>
            <form action="RemoveFromCartServlet" method="POST">
                <input type="hidden" name="packageId" value="<%= travelPackage.getPackageId() %>">
                <input type="hidden" name="customerId" value="<%= request.getAttribute("selectedCustomerId") %>">
                <input type="submit" value="Remove">
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>


<% double totalPrice = 0.0;
    for (TravelPackage travelPackage : travelPackages) {
        totalPrice += travelPackage.getPrice();
    } %>


<% double taxRate = 0.15;
    double totalBill = totalPrice + (totalPrice * taxRate); %>

<div class="total-section">
    <p class="total-price"> <%= totalPrice %></p>
    <p class="tax-amount"> <%= String.format("%.2f", totalPrice * 0.15) %></p>
    <p class="total-bill"> <%= totalBill %></p>
</div>

<div class="buttons-container">
    <form action="ProcessPaymentServlet" method="POST">
        <input type="hidden" name="customerId" value="<%= request.getAttribute("selectedCustomerId") %>">
        <input type="hidden" name="agentId" value="<%= request.getAttribute("selectedAgentId") != null ? request.getAttribute("selectedAgentId") : "" %>">
        <% for (TravelPackage travelPackage : travelPackages) { %>
        <input type="hidden" name="packageIds" value="<%= travelPackage.getPackageId() %>">
        <% } %>
        <button class="checkout-button" type="submit">Proceed to Checkout</button>
    </form>
</div>

<% } else { %>
<p class="empty-cart-message">Your Cart is Empty!</p>
<% } %>

<% String removalResult = (String) request.getAttribute("removalResult");
    if (removalResult != null) { %>
<p class="<%= removalResult.equals("Successfully removed the package from the cart.") ? "removed-message" : "failed-message" %>">
    <%= removalResult %>
</p>
<% } %>

</body>
</html>
