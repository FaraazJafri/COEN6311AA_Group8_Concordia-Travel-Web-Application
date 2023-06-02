<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="css/showbookings.css">
  <title>Cancel Booking</title>
</head>
<body>

<jsp:include page="menu.jsp" />

<h2>Customer Bookings</h2>

<form action="CustomerBookingsServlet" method="POST">
  <label for="customerId">Select a Customer:</label>
  <select id="customerId" name="customerId">
    <option value="" selected>-- Select Customer --</option>
    <% List<Customer> customers = (List<Customer>) request.getAttribute("customers");
      if (customers != null && !customers.isEmpty()) {
        for (Customer customer : customers) { %>
    <option value="<%= customer.getCustomerId() %>"><%= customer.getFirstName() %> <%= customer.getLastName() %></option>
    <% }
    } %>
  </select>
  <input type="hidden" name="condition" value="Cancel">
  <input type="submit" value="Get Customer Bookings">
</form>

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
      <th>Action</th> <!-- Added column for cancellation action -->
    </tr>
    </thead>
    <tbody>
    <% for (Booking booking : customerBookings) { %>
    <tr>
      <td><%= booking.getBookingId() %></td>
      <td><%= booking.getPackageId() %></td>
      <td><%= booking.getDepartureDate() %></td>
      <td>
        <form action="CancelBookingServlet" method="POST">
          <input type="hidden" name="bookingId" value="<%= booking.getBookingId() %>">
          <input type="hidden" name="customerId" value="<%= booking.getCustomerId() %>">
          <input type="submit" value="Cancel Booking" class="cancel-booking-button">
        </form>
      </td>
    </tr>
    <% } %>
    </tbody>
  </table>
</div>
<% } else { %>
<p>No bookings found for the selected customer.</p>
<% } %>
</body>
</html>
