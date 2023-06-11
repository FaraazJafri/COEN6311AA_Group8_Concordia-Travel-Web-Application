<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="css/modifypackage.css">
  <title>Modify Package Form</title>
</head>
<body>

<jsp:include page="menu.jsp" />


<h2>Modify Package</h2>

<%
  // Retrieve the travelPackages list from the request attribute
   TravelPackage selectedPackage = (TravelPackage) request.getAttribute("travelPackage");
%>

<div class="form-container">
  <form action="ModifyPackageServlet" method="POST">
    <input type="text" name="packageId" value="<%= selectedPackage.getPackageId() %>" readonly class="form-field">
    <label for="name">Package Name:</label>
    <input type="text" id="name" name="name" value="<%= selectedPackage.getName() %>" class="form-field">

    <label for="description">Package Description:</label>
    <textarea id="description" name="description" class="form-field"><%= selectedPackage.getDescription() %></textarea>

<%--    <label for="price">Package Price:</label>--%>
<%--    <input type="number" id="price" name="price" value="<%= selectedPackage.getPrice() %>" class="form-field">--%>

    <h2>Flights:</h2>
    <ul>
      <% for (Flight flight : selectedPackage.getFlights()) { %>
      <li>
        <label for="flight<%= flight.getFlightId() %>">Flight:</label>
        <input type="text" id="flight<%= flight.getFlightId() %>" name="flights" value="<%= flight.getFlightId() %>" readonly class="form-field">
        <label for="airline<%= flight.getFlightId() %>">Airline:</label>
        <input type="text" name="airlines" value="<%= flight.getAirline() %>" class="form-field">
        <label for="departure<%= flight.getFlightId() %>">Departure:</label>
        <input type="datetime-local" name="departures" value="<%= flight.getDeparture() %>" class="form-field">
        <label for="arrival<%= flight.getFlightId() %>">Arrival:</label>
        <input type="datetime-local" name="arrivals" value="<%= flight.getArrival() %>" class="form-field">
        <label for="flightPrice<%= flight.getFlightId() %>">Price:</label>
        <input type="text" name="prices" value="<%= flight.getPrice() %>" class="form-field">
      </li>
      <% } %>
    </ul>
    <h2>Hotels:</h2>
    <ul>
      <% for (Hotel hotel : selectedPackage.getHotels()) { %>
      <li>
        <label for="hotel<%= hotel.getHotelId() %>">Hotel:</label>
        <input type="text" id="hotel<%= hotel.getHotelId() %>" name="hotels" value="<%= hotel.getHotelId() %>" readonly class="form-field">
        <label for="hotelName<%= hotel.getHotelId() %>">Name:</label>
        <input type="text" name="hotelNames" value="<%= hotel.getName() %>" class="form-field">
        <label for="location<%= hotel.getHotelId() %>">Location:</label>
        <input type="text" name="locations" value="<%= hotel.getLocation() %>" class="form-field">
        <label for="hotelPrice<%= hotel.getHotelId() %>">Price:</label>
        <input type="text" name="hotelPrices" value="<%= hotel.getPrice() %>" class="form-field">
      </li>
      <% } %>
    </ul>
    <h2>Activities:</h2>
    <ul>
      <% for (Activity activity : selectedPackage.getActivities()) { %>
      <li>
        <label for="activity<%= activity.getActivityId() %>">Activity:</label>
        <input type="text" id="activity<%= activity.getActivityId() %>" name="activities" value="<%= activity.getActivityId() %>" readonly class="form-field">
        <label for="activityName<%= activity.getActivityId() %>">Name:</label>
        <input type="text" name="activityNames" value="<%= activity.getName() %>" class="form-field">
        <label for="activityDescription<%= activity.getActivityId() %>">Description:</label>
        <input type="text" name="descriptions" value="<%= activity.getDescription() %>" class="form-field">
        <label for="activityPrice<%= activity.getActivityId() %>">Price:</label>
        <input type="text" name="activityPrices" value="<%= activity.getPrice() %>" class="form-field">
      </li>
      <% } %>
    </ul>

    <input type="submit" value="Save Changes" class="form-button">
  </form>

</div>
</body>
</html>
