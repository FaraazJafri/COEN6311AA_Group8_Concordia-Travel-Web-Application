<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.notficationModels.Notification" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/homepage.css">
    <link rel="stylesheet" href="/webjars/simplebar/5.3.4/simplebar.min.css"/>
    <script src="/webjars/simplebar/5.3.4/simplebar.min.js"></script>

</head>
<body>
<div id="logo-container">
    <img src="images/travelLogo.jpg" alt="Logo" id="logo">
</div>

<h1>Concordia Travel Booking System</h1>

<div id="user-info">
    Welcome: <%= session.getAttribute("fullName") %> (logged in as <%= session.getAttribute("role") %>)
</div>

<jsp:include page="/notifications"/>

<div id="container">
    <ul id="menu">
        <li><a href="homepage.jsp">Home</a></li>
        <li><a href="#">Packages</a>
            <ul>
                <% if (session.getAttribute("role").equals("Customer")) { %>
                <li><a href="CreateCustomPackage">Create a custom package</a></li>
                <li><a href="UserPackagesServlet">Display custom packages</a></li>
                <li><a href="ModifyCustomPackage">Modify a custom package</a></li>
                <li><a href="RemoveCustomPackage">Remove a custom package</a></li>
                <% } %>
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
                <% if (session.getAttribute("role").equals("Customer")) { %>
                <li><a href="BookCustomPackageServlet">Book a custom package</a></li>
                <% } %>
                <li><a href="CustomerBookingsServlet">View your bookings</a></li>
                <li><a href="CancelBookingServlet">Cancel your bookings</a></li>
                <li><a href="ModifyBookingServlet">Modify your bookings</a></li>
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
        <% if (session.getAttribute("role").equals("Admin") || session.getAttribute("role").equals("Agent")) { %>
        <li><a href="#">Reports</a>
            <ul>
                <li><a href="report">View Report</a></li>
            </ul>
        </li>
        <% } %>
        <li><a href="#">My Space</a>
            <ul>
                <li><a href="ViewCartServlet">View Cart</a></li>
            </ul>
        </li>
        <li><a href="login.jsp">Logout</a></li>
        <li>
            <div id="notifications-menu">
                <a href="#">
                    <img src="images/notification-icon.png" alt="Notifications" id="notification-icon"
                         style="width: 12px; height: 12px;">
                </a>
                <ul id="notifications-dropdown">
                    <% List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
                        if (notifications != null && !notifications.isEmpty()) {
                            for (Notification notification : notifications) { %>
                    <li><span class="notification-text"
                              style="color: white; font-family: 'Helvetica Neue', 'Helvetica', Arial; font-size: 12px"><%= notification.getMessage() %></span>
                        <a href="DeleteNotificationServlet?notificationId=<%= notification.getNotificationId() %>">Delete</a>
                    </li>
                    <% }
                    } else { %>
                    <li><span class="notification-text"
                              style="color: white; font-family: 'Helvetica Neue', 'Helvetica', Arial; font-size: 12px"> No notifications available!</span>
                    </li>
                    <% } %>
                </ul>
            </div>
        </li>
    </ul>
</div>

<%
    String notificationDeleted = (String) request.getAttribute("notificationDeleted");
    if (notificationDeleted != null) {
%>
<span class="notification-text"
      style="color: green; font-family: 'Helvetica Neue', 'Helvetica', Arial; font-size: 12px"><%= notificationDeleted %></span>
<% } else %>


</body>
</html>
