<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Travel Packages</title>
  <link rel="stylesheet" type="text/css" href="css/displaypackage.css">
</head>
<body>
<jsp:include page="menu.jsp" />

<% List<TravelPackage> searchResults = (List<TravelPackage>) request.getAttribute("searchResults");
  if (searchResults != null && !searchResults.isEmpty()) { %>
<table>
  <thead>
  <tr>
    <th>Package ID</th>
    <th>Name</th>
    <th>Description</th>
    <th>Price</th>
    <th>Flights</th>
    <th>Hotels</th>
    <th>Activities</th>
  </tr>
  </thead>
  <tbody>
  <% for (TravelPackage travelPackage : searchResults) { %>
  <tr>
    <td><%= travelPackage.getPackageId() %></td>
    <td><%= travelPackage.getName() %></td>
    <td><%= travelPackage.getDescription() %></td>
    <td><%= travelPackage.getPrice() %></td>
    <td>
      <% List<Flight> flights = travelPackage.getFlights();
        if (flights != null && !flights.isEmpty()) { %>
      <ul>
        <% for (Flight flight : flights) { %>
        <li><strong>Flight ID:</strong> <%= flight.getFlightId() %></li>
        <li><strong>Airline:</strong> <%= flight.getAirline() %></li>
        <li><strong>Departure:</strong> <%= flight.getDeparture() %></li>
        <li><strong>Arrival:</strong> <%= flight.getArrival() %></li>
        <li><strong>Price:</strong> <%= flight.getPrice() %></li>
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
        <li><strong>Hotel ID:</strong> <%= hotel.getHotelId() %></li>
        <li><strong>Name:</strong> <%= hotel.getName() %></li>
        <li><strong>Location:</strong> <%= hotel.getLocation() %></li>
        <li><strong>Price:</strong> <%= hotel.getPrice() %></li>
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
        <li><strong>Activity ID:</strong> <%= activity.getActivityId() %></li>
        <li><strong>Name:</strong> <%= activity.getName() %></li>
        <li><strong>Description:</strong> <%= activity.getDescription() %></li>
        <li><strong>Price:</strong> <%= activity.getPrice() %></li>
        <% } %>
      </ul>
      <% } else { %>
      No activities found.
      <% } %>
    </td>
  </tr>
  <% } %>
  </tbody>
</table>
<% } else { %>
<p>No travel packages found.</p>
<% } %>
</body>
</html>
