<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Remove Travel Package</title>
  <link rel="stylesheet" type="text/css" href="css/displaypackage.css">
</head>
<body>
<h1>Remove Travel Package</h1>

<form action="RemovePackageDisplayServlet" method="post">
  <label for="packageId">Enter the Package ID to delete:</label>
  <input type="text" id="packageId" name="packageId">
  <input type="submit" value="Delete">
</form>

<h2>Travel Packages</h2>

<% List<TravelPackage> travelPackages = (List<TravelPackage>) request.getAttribute("travelPackages");
  if (travelPackages != null && !travelPackages.isEmpty()) { %>
<table>
  <tr>
    <th>Package ID</th>
    <th>Name</th>
    <th>Description</th>
    <th>Price</th>
    <th>Flights</th>
    <th>Hotels</th>
    <th>Activities</th>
  </tr>
  <% for (TravelPackage travelPackage : travelPackages) { %>
  <tr>
    <td><%= travelPackage.getPackageId() %></td>
    <td><%= travelPackage.getName() %></td>
    <td><%= travelPackage.getDescription() %></td>
    <td><%= travelPackage.getPrice() %></td>
    <td>
      <ul>
        <% List<Flight> flights = travelPackage.getFlights();
          if (flights != null && !flights.isEmpty()) {
            for (Flight flight : flights) { %>
        <li><strong>Flight ID:</strong> <%= flight.getFlightId() %></li>
        <li><strong>Airline:</strong> <%= flight.getAirline() %></li>
        <li><strong>Departure:</strong> <%= flight.getDeparture() %></li>
        <li><strong>Arrival:</strong> <%= flight.getArrival() %></li>
        <li><strong>Price:</strong> <%= flight.getPrice() %></li>
        <%     }
        } else { %>
        <li>No flights found.</li>
        <% } %>
      </ul>
    </td>
    <td>
      <ul>
        <% List<Hotel> hotels = travelPackage.getHotels();
          if (hotels != null && !hotels.isEmpty()) {
            for (Hotel hotel : hotels) { %>
        <li><strong>Hotel ID:</strong> <%= hotel.getHotelId() %></li>
        <li><strong>Name:</strong> <%= hotel.getName() %></li>
        <li><strong>Location:</strong> <%= hotel.getLocation() %></li>
        <li><strong>Price:</strong> <%= hotel.getPrice() %></li>
        <%     }
        } else { %>
        <li>No hotels found.</li>
        <% } %>
      </ul>
    </td>
    <td>
      <ul>
        <% List<Activity> activities = travelPackage.getActivities();
          if (activities != null && !activities.isEmpty()) {
            for (Activity activity : activities) { %>
        <li><strong>Activity ID:</strong> <%= activity.getActivityId() %></li>
        <li><strong>Name:</strong> <%= activity.getName() %></li>
        <li><strong>Description:</strong> <%= activity.getDescription() %></li>
        <li><strong>Price:</strong> <%= activity.getPrice() %></li>
        <%     }
        } else { %>
        <li>No activities found.</li>
        <% } %>
      </ul>
    </td>
  </tr>
  <% } %>
</table>
<% } else { %>
<p>No travel packages found.</p>
<% } %>

</body>
</html>
