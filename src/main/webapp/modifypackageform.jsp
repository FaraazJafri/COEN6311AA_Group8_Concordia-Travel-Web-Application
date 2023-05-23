<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <title>Modify Package Form</title>
</head>
<body>
<h1>Modify Package</h1>

<%
  // Retrieve the travelPackages list from the request attribute
  TravelPackage selectedPackage = (TravelPackage) request.getAttribute("travelPackage");
%>

<form action="ModifyPackageServlet" method="POST">
  <input type="hidden" name="packageId" value="<%= selectedPackage.getPackageId() %>">

  <label for="name">Package Name:</label>
  <input type="text" id="name" name="name" value="<%= selectedPackage.getName() %>">

  <label for="description">Package Description:</label>
  <textarea id="description" name="description"><%= selectedPackage.getDescription() %></textarea>

  <label for="price">Package Price:</label>
  <input type="number" id="price" name="price" value="<%= selectedPackage.getPrice() %>">

  <h2>Flights:</h2>
  <ul>
    <% for (Flight flight : selectedPackage.getFlights()) { %>
    <li>
      <label for="flight<%= flight.getFlightId() %>">Flight:</label>
      <input type="text" id="flight<%= flight.getFlightId() %>" name="flights" value="<%= flight.getFlightId() %>" disabled>
      <!-- Include other flight information fields here -->
    </li>
    <% } %>
  </ul>

  <h2>Hotels:</h2>
  <ul>
    <% for (Hotel hotel : selectedPackage.getHotels()) { %>
    <li>
      <label for="hotel<%= hotel.getHotelId() %>">Hotel:</label>
      <input type="text" id="hotel<%= hotel.getHotelId() %>" name="hotels" value="<%= hotel.getHotelId() %>" disabled>
      <!-- Include other hotel information fields here -->
    </li>
    <% } %>
  </ul>

  <h2>Activities:</h2>
  <ul>
    <% for (Activity activity : selectedPackage.getActivities()) { %>
    <li>
      <label for="activity<%= activity.getActivityId() %>">Activity:</label>
      <input type="text" id="activity<%= activity.getActivityId() %>" name="activities" value="<%= activity.getActivityId() %>" disabled>
      <!-- Include other activity information fields here -->
    </li>
    <% } %>
  </ul>

  <input type="submit" value="Save Changes">
</form>
</body>
</html>
