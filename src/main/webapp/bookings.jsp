<%@ page import="java.util.List" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Bookings" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer" %><%--
  Created by IntelliJ IDEA.
  User: shive
  Date: 2023-05-22
  Time: 4:35 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="css/showbookings.css">
  <title>Customer Bookings</title>
</head>
<body>
<div id="logo-container">
  <img src="images/travel%20logo%202.jpg" alt="Logo" id="logo">
</div>

<h1>Travel Packages</h1>

<div id="container">
  <ul id="menu">
    <li><a href="homepage.jsp">Home</a></li>
    <li><a href="#">Packages</a>
      <ul>
        <li><a href="createpackage.jsp">Create a package</a></li>
        <li><a href="DisplayPackagesServlet">Display all packages</a></li>
        <li><a href="RemovePackageDisplayServlet">Remove a package</a></li>
        <li><a href="ModifyPackageDisplayServlet">Modify a package</a></li>
      </ul>
    </li>
    <li><a href="#">Bookings</a>
      <ul>
        <li><a href="BookPackageServlet">Book a Package</a></li>
        <li><a href="CustomerBookingsServlet">View your bookings</a></li>
        <li><a href="#">Modify your bookings</a></li>
      </ul>
    </li>
    <li><a href="#">Customer</a>
      <ul>
        <li><a href="addcustomer.jsp">Add a Customer</a></li>
      </ul>
    </li>
  </ul>
</div>

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
  <input type="submit" value="Get Customer Bookings">
</form>

<hr>



<% String selectedCustomer = (String) request.getAttribute("selectedCustomer");
  if (selectedCustomer != null) { %>
<p class="booking-title">Here is your booking <%= selectedCustomer %>:</p>
<% } %>

<% List<Bookings> customerBookings = (List<Bookings>) request.getAttribute("customerBookings");
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
    <% for (Bookings booking : customerBookings) { %>
    <tr>
      <td><%= booking.getBookingId() %></td>
      <td><%= booking.getPackageId() %></td>
      <td><%= booking.getDepartureDate() %></td>
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
