<%@ page import="java.util.List" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.packageModels.*" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/bookpackage.css">
    <title>Book a Package</title>
</head>
<body>

<jsp:include page="menu.jsp" />

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

<h2>Customers</h2>

<table>
    <thead>
    <tr>
        <th>Customer ID</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Age</th>
        <th>Gender</th>
        <th>Phone Number</th>
        <th>Email ID</th>
    </tr>
    </thead>
    <tbody>
    <% List<Customer> customers = (List<Customer>) request.getAttribute("customers");
        if (customers != null && !customers.isEmpty()) {
            for (Customer customer : customers) { %>
    <tr>
        <td><%= customer.getCustomerId() %></td>
        <td><%= customer.getFirstName() %></td>
        <td><%= customer.getLastName() %></td>
        <td><%= customer.getAge() %></td>
        <td><%= customer.getGender() %></td>
        <td><%= customer.getPhoneNumber() %></td>
        <td><%= customer.getEmailId() %></td>
    </tr>
    <%      }
    } else { %>
    <tr>
        <td colspan="7">No customers found.</td>
    </tr>
    <% } %>
    </tbody>
</table>

<h2>Make a Booking</h2>

<form action="BookPackageServlet" method="POST">
    <label for="packageId">Package ID:</label>
    <input type="text" id="packageId" name="packageId" required><br><br>
    <label for="customerId">Customer ID:</label>
    <input type="text" id="customerId" name="customerId" required><br><br>
    <label for="departureDate">Departure Date:</label>
    <input type="datetime-local" id="departureDate" name="departureDate" required><br><br>
    <input type="submit" value="Book Package">
</form>
</body>
</html>
