<%@ page import="java.util.List" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Booking" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.TravelPackage" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Flight" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Hotel" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.Activity" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.userModels.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="css/modifybooking.css">
  <title>Modify Booking</title>
</head>
<body>
<jsp:include page="menu.jsp"/>

<div id="modify-booking" class="center">
  <h2>Modify Booking</h2>

  <% if (session.getAttribute("role").equals("Admin") || session.getAttribute("role").equals("Agent")) { %>
  <form action="CustomerBookingsServlet" method="POST">
    <label for="customerId">Select a Customer:</label>
    <select id="customerId" name="customerId">
      <option value="" selected>-- Select Customer --</option>
      <% List<User> customers = (List<User>) request.getAttribute("customers");
        if (customers != null && !customers.isEmpty()) {
          for (User customer : customers) { %>
      <option value="<%= customer.getUserId() %>"> Id:<%= customer.getUserId() %> Name: <%= customer.getFirstName() %> <%= customer.getLastName() %>
      </option>
      <% }
      } %>
    </select>
    <input type="hidden" name="condition" value="Modify">
    <input type="submit" value="Get Customer Bookings">
  </form>

  <hr>

  <% String selectedCustomer = (String) request.getAttribute("selectedCustomer");
    if (selectedCustomer != null) { %>
  <p class="booking-title">Bookings for <%= selectedCustomer %>:</p>
  <% } %>
  <% } %>

  <% if (session.getAttribute("role").equals("Customer") ) { %>
  <h2>Your bookings:</h2>
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

  <form action="ModifyBookingServlet" method="POST">
    <label for="bookingId">Select a Booking:</label>
    <select id="bookingId" name="bookingId">
      <option value="" selected>-- Select Booking --</option>
      <% List<Booking> bookings = (List<Booking>) request.getAttribute("customerBookings");
        if (bookings != null && !bookings.isEmpty()) {
          for (Booking booking : bookings) { %>
      <option value="<%= booking.getBookingId() %>"><%= booking.getBookingId() %></option>
      <% }}%>

    </select>
    <br><br>
    <label for="packageId">Package ID:</label>
    <input type="text" id="packageId" name="packageId" required><br><br>
    <label for="departureDate">Departure Date:</label>
    <input type="datetime-local" id="departureDate" name="departureDate" required><br><br>
    <input type="hidden" id="customerIdToModify" name="customerId" value="<%= bookings != null && !bookings.isEmpty() ? bookings.get(0).getCustomerId() : "" %>">
    <input type="submit" value="Modify Booking">
  </form>
</div>


<h2>Available Travel Packages</h2>

<% List<TravelPackage> travelPackages = (List<TravelPackage>) request.getAttribute("travelPackages");
  if (travelPackages != null && !travelPackages.isEmpty()) { %>
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
  <% for (TravelPackage travelPackage : travelPackages) { %>
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
