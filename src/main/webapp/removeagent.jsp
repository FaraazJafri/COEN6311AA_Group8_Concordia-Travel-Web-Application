<%@ page import="java.util.List" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.bookingModels.Customer" %>
<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.userModels.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="css/showbookings.css">
  <style>
    .success {
      color: green;
    }
    .error {
      color: red;
    }
  </style>
  <title>Remove Agent</title>
</head>
<body>
<jsp:include page="menu.jsp"/>

<h2>Remove Travel Agent</h2>

<% String message = (String) request.getAttribute("message"); %>
<% if (message != null) { %>
<% if (message.equals("Agent removed successfully")) { %>
<p class="success"><%= message %></p>
<% } else { %>
<p class="error"><%= message %></p>
<% } %>
<% } %>

<form action="RemoveAgentServlet" method="POST">
  <label for="customerId">Select a Customer:</label>
  <select id="customerId" name="customerId">
    <option value="" selected>-- Select Customer --</option>
    <% List<User> customers = (List<User>) request.getAttribute("customers");
      if (customers != null && !customers.isEmpty()) {
        for (User user : customers) { %>
    <option value="<%= user.getUserId() %>"><%= user.getFirstName() %> <%= user.getLastName() %>
    </option>
    <% }
    } %>
  </select>
  <input type="hidden" name="condition" value="View">
  <input type="submit" value="Remove Agent Role">
</form>
</body>
</html>
