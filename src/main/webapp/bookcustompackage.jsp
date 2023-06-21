<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Custom Package</title>
  <link rel="stylesheet" type="text/css" href="css/bookpackage.css">
</head>
<body>
<jsp:include page="menu.jsp" />


<form action="BookCustomPackageServlet" method="POST">
  <label for="packageId">Package ID:</label>
  <input type="text" id="packageId" name="packageId" required><br><br>
  <% if (session.getAttribute("role").equals("Admin") || session.getAttribute("role").equals("Agent")) { %>
  <label for="customerId">Customer ID:</label>
  <input type="text" id="customerId" name="customerId" required><br><br>
  <% } %>
  <label for="departureDate">Departure Date:</label>
  <input type="datetime-local" id="departureDate" name="departureDate" required><br><br>
  <input type="submit" value="Add to Cart">
</form>

<h3>Available User Packages</h3>
<%
  List<TravelPackage> userPackages = (List<TravelPackage>) request.getAttribute("userPackages");
  if (userPackages != null && !userPackages.isEmpty()) {
%>
<table>
  <thead>
  <tr>
    <th>Package ID</th>
    <th>Activities</th>
    <th>Flights</th>
    <th>Hotels</th>
  </tr>
  </thead>
  <tbody>
  <%
    for (TravelPackage travelPackage : userPackages) {
  %>
  <tr>
    <td><%= travelPackage.getPackageId() %>
    </td>
    <td>
      <ul>
        <%
          List<Activity> activities = travelPackage.getActivities();
          if (activities != null && !activities.isEmpty()) {
            for (Activity activity : activities) {
        %>
        <li>Name: <%= activity.getName() %>
        </li>
        <li>Description: <%= activity.getDescription() %>
        </li>
        <li>Price: <%= activity.getPrice() %>
        </li>
        <%
          }
        } else {
        %>
        <li>No activities found.</li>
        <%
          }
        %>
      </ul>
    </td>
    <td>
      <ul>
        <%
          List<Flight> flights = travelPackage.getFlights();
          if (flights != null && !flights.isEmpty()) {
            for (Flight flight : flights) {
        %>
        <li>Airline: <%= flight.getAirline() %>
        </li>
        <li>Departure: <%= flight.getDeparture() %>
        </li>
        <li>Arrival: <%= flight.getArrival() %>
        </li>
        <li>Price: <%= flight.getPrice() %>
        </li>
        <%
          }
        } else {
        %>
        <li>No flights found.</li>
        <%
          }
        %>
      </ul>
    </td>
    <td>
      <ul>
        <%
          List<Hotel> hotels = travelPackage.getHotels();
          if (hotels != null && !hotels.isEmpty()) {
            for (Hotel hotel : hotels) {
        %>
        <li>Name: <%= hotel.getName() %>
        </li>
        <li>Location: <%= hotel.getLocation() %>
        </li>
        <li>Price: <%= hotel.getPrice() %>
        </li>
        <%
          }
        } else {
        %>
        <li>No hotels found.</li>
        <%
          }
        %>
      </ul>
    </td>
  </tr>
  <%
    }
  %>
  </tbody>
</table>
<%
} else {
%>
<p>No user packages found.</p>
<%
  }
%>
</body>
</html>
