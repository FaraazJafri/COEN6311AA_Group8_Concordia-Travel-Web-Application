<%@ page import="java.util.List" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.userModels.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/showbookings.css">
    <title>Customer Bookings</title>
</head>
<body>
<jsp:include page="menu.jsp"/>

<h2>Customer Bookings</h2>
<% if (session.getAttribute("role").equals("Admin") || session.getAttribute("role").equals("Agent")) { %>
<form action="CustomerBookingsServlet" method="POST">
    <label for="customerId">Select a Customer:</label>
    <select id="customerId" name="customerId">
        <option value="" selected>-- Select Customer --</option>
        <% List<User> customers = (List<User>) request.getAttribute("customers");
            if (customers != null && !customers.isEmpty()) {
                for (User customer : customers) { %>
        <option value="<%= customer.getUserId() %>"><%= customer.getFirstName() %> <%= customer.getLastName() %>
        </option>
        <% }
        } %>
    </select>
    <input type="hidden" name="condition" value="View">
    <input type="submit" value="Get Customer Bookings">
</form>
<% } %>
<hr>


<% String selectedCustomer = (String) request.getAttribute("selectedCustomer");
    if (selectedCustomer != null) { %>
<p class="booking-title">Here is your booking <%= selectedCustomer %>:</p>
<% } %>

<% List<Booking> customerBookings = (List<Booking>) request.getAttribute("customerBookings");
    if (customerBookings != null && !customerBookings.isEmpty()) { %>
<div class="customer-bookings">
    <table>
        <thead>
        <tr>
            <th>Booking ID</th>
            <th>Package ID</th>
            <th>Departure Date</th>
        </tr>
        </thead>
        <tbody>
        <% for (Booking booking : customerBookings) { %>
        <tr>
            <td><%= booking.getBookingId() %>
            </td>
            <td><%= booking.getPackageId() %>
            </td>
            <td><%= booking.getDepartureDate() %>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
<% } else { %>
<p>No bookings found.</p>
<% } %>


</body>
</html>
