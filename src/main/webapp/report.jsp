<%@ page import="java.util.List" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="css/bookpackage.css">
  <title>Agent Report</title>
</head>
<body>

<jsp:include page="menu.jsp"/>
<h2>Agent Report</h2>

<% List<Booking> bookings = (List<Booking>) request.getAttribute("bookings"); %>
<% List<TravelPackage> packages = (List<TravelPackage>) request.getAttribute("packages"); %>

<% if (bookings != null && !bookings.isEmpty()) { %>
<h3>Bookings:</h3>
<table>
  <tr>
    <th>Booking ID</th>
    <th>Customer ID</th>
    <th>Package ID</th>
    <th>Package Name</th>
    <th>Package Description</th>
    <th>Package Price</th>
  </tr>
  <% double totalAmount = 0.0; %>
  <% for (Booking booking : bookings) { %>
  <tr>
    <td><%= booking.getBookingId() %></td>
    <td><%= booking.getCustomerId() %></td>
    <td><%= booking.getPackageId() %></td>
    <%-- Get package information based on package ID --%>
    <% TravelPackage packageInfo = null; %>
    <% for (TravelPackage travelPackage : packages) {
      if (travelPackage.getPackageId().equals(booking.getPackageId())) {
        packageInfo = travelPackage;
        break;
      }
    } %>
    <% if (packageInfo != null) { %>
    <td><%= packageInfo.getName() %></td>
    <td><%= packageInfo.getDescription() %></td>
    <td><%= packageInfo.getPrice() %></td>
    <% totalAmount += packageInfo.getPrice(); %>
    <% } else { %>
    <td>N/A</td>
    <td>N/A</td>
    <td>N/A</td>
    <% } %>
  </tr>
  <% } %>
</table>
<p>Total Revenue: <%= totalAmount %></p>
<% } else { %>
<p>No bookings found.</p>
<% } %>

</body>
<style>
  p {
    font-family: Arial, sans-serif;
    font-size: 18px;
    color: #333333;
    margin: 0;
    padding: 10px;
    background-color: lightgray;
    border: 1px solid #cccccc;
    border-radius: 4px;
  }
</style>

</html>
