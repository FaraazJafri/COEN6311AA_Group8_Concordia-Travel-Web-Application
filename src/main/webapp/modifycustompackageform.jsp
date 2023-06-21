<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Activities, Flights, and Hotels</title>
    <link rel="stylesheet" type="text/css" href="css/bookpackage.css">
</head>
<body>
<jsp:include page="menu.jsp"/>

<form action="ModifyCustomPackageForm" method="post">
    <%String packageId = request.getParameter("packageId");%>
    <input type="hidden" id="packageId" name="packageId" value="${packageId}">

    <label for="activityIds">Activity IDs (comma-separated):</label>
    <input type="text" id="activityIds" name="activityIds"><br><br>
    <label for="flightIds">Flight IDs (comma-separated):</label>
    <input type="text" id="flightIds" name="flightIds"><br><br>
    <label for="hotelIds">Hotel IDs (comma-separated):</label>
    <input type="text" id="hotelIds" name="hotelIds"><br><br>
    <input type="submit" value="Submit">
</form>

<% List<Activity> activities = (List<Activity>) request.getAttribute("activities");
    List<Flight> flights = (List<Flight>) request.getAttribute("flights");
    List<Hotel> hotels = (List<Hotel>) request.getAttribute("hotels");
    if (activities != null && !activities.isEmpty()) { %>
<h1>Activities</h1>
<table>
    <thead>
    <tr>
        <th>Activity ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Price</th>
    </tr>
    </thead>
    <tbody>
    <% for (Activity activity : activities) { %>
    <tr>
        <td><%= activity.getActivityId() %>
        </td>
        <td><%= activity.getName() %>
        </td>
        <td><%= activity.getDescription() %>
        </td>
        <td><%= activity.getPrice() %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>

<% if (flights != null && !flights.isEmpty()) { %>
<h1>Flights</h1>
<table>
    <thead>
    <tr>
        <th>Flight ID</th>
        <th>Airline</th>
        <th>Arrival</th>
        <th>Departure</th>
        <th>Price</th>
    </tr>
    </thead>
    <tbody>
    <% for (Flight flight : flights) { %>
    <tr>
        <td><%= flight.getFlightId() %>
        </td>
        <td><%= flight.getAirline() %>
        </td>
        <td><%= flight.getArrival() %>
        </td>
        <td><%= flight.getDeparture() %>
        </td>
        <td><%= flight.getPrice() %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>

<% if (hotels != null && !hotels.isEmpty()) { %>
<h1>Hotels</h1>
<table>
    <thead>
    <tr>
        <th>Hotel ID</th>
        <th>Name</th>
        <th>Location</th>
        <th>Price</th>
    </tr>
    </thead>
    <tbody>
    <% for (Hotel hotel : hotels) { %>
    <tr>
        <td><%= hotel.getHotelId() %>
        </td>
        <td><%= hotel.getName() %>
        </td>
        <td><%= hotel.getLocation() %>
        </td>
        <td><%= hotel.getPrice() %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>
</body>
</html>
