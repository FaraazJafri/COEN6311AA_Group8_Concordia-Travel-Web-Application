<%@ page import="com.example.coen_mp_concordiatravelwebapplication.models.userModels.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="css/modifyagentdetailsform.css">
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

<% User agent = (User) request.getAttribute("agent");
  String message = (String) request.getAttribute("message");
  if (message != null) {
    if (message.equals("Agent details modified successfully")) { %>
<p class="success"><%= message %></p>
<% } else { %>
<p class="error"><%= message %></p>
<% } %>
<% } %>

<form action="ModifyAgentDetailsFormServlet" method="POST">
  <input type="text" name="userId" value="<%= agent.getUserId() %>" readonly class="form-field"><br><br>

  <label for="firstName">First Name:</label>
  <input type="text" id="firstName" name="firstName" value="<%= agent.getFirstName() %>" required><br><br>

  <label for="lastName">Last Name:</label>
  <input type="text" id="lastName" name="lastName" value="<%= agent.getLastName() %>" required><br><br>

  <label for="dateOfBirth">Date of Birth:</label>
  <input type="date" id="dateOfBirth" name="dateOfBirth" value="<%= agent.getDateOfBirth() %>" required><br><br>

  <label for="email">Email Address:</label>
  <input type="email" id="email" name="email" value="<%= agent.getEmail() %>" required><br><br>

  <label for="username">Username:</label>
  <input type="text" id="username" name="username" value="<%= agent.getUsername() %>" readonly class="form-field"><br><br>

  <label for="phoneNumber">Phone Number:</label>
  <input type="text" id="phoneNumber" name="phoneNumber" value="<%= agent.getPhoneNumber() %>" required><br><br>

  <label for="age">Age:</label>
  <input type="number" id="age" name="age" value="<%= agent.getAge() %>" required><br><br>

  <label for="gender">Gender:</label>
  <select id="gender" name="gender">
    <option value="Male" <%= agent.getGender().equals("Male") ? "selected" : "" %>>Male</option>
    <option value="Female" <%= agent.getGender().equals("Female") ? "selected" : "" %>>Female</option>
    <option value="Other" <%= agent.getGender().equals("Other") ? "selected" : "" %>>Other</option>
  </select><br><br>

  <input type="submit" value="Update Agent Details">
</form>
</body>
</html>
