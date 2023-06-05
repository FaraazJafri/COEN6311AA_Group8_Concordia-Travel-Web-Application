<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="css/homepage.css">
</head>
<body>
<div id="logo-container">
  <img src="images/travel%20logo%202.jpg" alt="Logo" id="logo">
</div>

<h1>Travel Packages</h1>

<div id="user-info">
    Welcome: <%= session.getAttribute("fullName") %> (logged in as <%= session.getAttribute("role") %>)
</div>

<div id="container">
  <ul id="menu">
    <li><a href="homepage.jsp">Home</a></li>
    <li><a href="#">Packages</a>
      <ul>
        <% if (session.getAttribute("role").equals("Admin") || session.getAttribute("role").equals("Agent")) { %>
        <li><a href="createpackage.jsp">Create a package</a></li>
        <li><a href="RemovePackageDisplayServlet">Remove a package</a></li>
        <li><a href="ModifyPackageDisplayServlet">Modify a package</a></li>
        <% } %>
        <li><a href="DisplayPackagesServlet">Display all packages</a></li>
      </ul>
    </li>
    <li><a href="#">Bookings</a>
      <ul>
        <li><a href="BookPackageServlet">Book a Package</a></li>
        <li><a href="CustomerBookingsServlet">View your bookings</a></li>
        <li><a href="CancelBookingServlet">Cancel your bookings</a></li>
        <li><a href="ModifyBookingServlet">Modify your bookings</a> </li>
      </ul>
    </li>
    <% if (session.getAttribute("role").equals("Admin") || session.getAttribute("role").equals("Agent")) { %>
    <li><a href="#">Customer</a>
      <ul>
        <li><a href="addcustomer.jsp">Add a Customer</a></li>
        <li><a href="LinkCustomerServlet">Link a Customer</a></li>
      </ul>
    </li>
    <% } %>
    <% if (session.getAttribute("role").equals("Admin")) { %>
    <li><a href="#">Agent</a>
      <ul>
        <li><a href="AddAgentServlet">Add an Agent</a></li>
        <li><a href="RemoveAgentServlet">Remove an Agent</a></li>
        <li><a href="ModifyAgentDetailsServlet">Modify Agent Details</a></li>
      </ul>
    </li>
    <% } %>
    <li><a href="#">Search</a>
      <ul>
        <li><a href="searchlocation.jsp">Search package by location</a></li>
        <li><a href="searchprice.jsp">Search package by price</a></li>
      </ul>
    </li>
    <li><a href="login.jsp">Logout</a></li>
  </ul>
</div>



</body>
</html>
