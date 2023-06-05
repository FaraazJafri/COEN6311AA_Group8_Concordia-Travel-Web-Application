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
  <title>Modify Agent Details</title>
</head>
<body>
<jsp:include page="menu.jsp"/>

<h2>Modify Agent Details</h2>

<% String message = (String) request.getAttribute("message"); %>
<% if (message != null) { %>
<% if (message.equals("Agent added successfully")) { %>
<p class="success"><%= message %></p>
<% } else { %>
<p class="error"><%= message %></p>
<% } %>
<% } %>

<form action="ModifyAgentDetailsServlet" method="POST">
  <label for="customerId">Select an Agent:</label>
  <select id="customerId" name="customerId">
    <option value="" selected>-- Select Agent --</option>
    <% List<User> agents = (List<User>) request.getAttribute("agents");
      if (agents != null && !agents.isEmpty()) {
        for (User agent : agents) { %>
    <option value="<%= agent.getUserId() %>"><%= agent.getFirstName() %> <%= agent.getLastName() %>
    </option>
    <% }
    } %>
  </select>
  <input type="hidden" name="condition" value="View">
  <input type="submit" value="Modify Agent Details">
</form>
</body>
</html>
