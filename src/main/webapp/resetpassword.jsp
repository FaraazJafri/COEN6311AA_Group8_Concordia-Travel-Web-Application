<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="css/resetpassword.css">
  <title>Reset Password</title>
</head>
<body>

<div id="logo-container">
  <img src="images/travel%20logo%202.jpg" alt="Logo" id="logo">
</div>

<h1>Travel Packages</h1>

<% if (request.getAttribute("errorMessage") != null) { %>
<p class="error-message"><%= request.getAttribute("errorMessage") %></p>
<% } %>
<form action="resetpassword" method="post">
  <h2>Reset Password</h2>
  <input type="hidden" id="userID" name="userID" value="<%= request.getAttribute("userID") %>">
  <label for="password">New Password:</label>
  <input type="password" id="password" name="password" required><br><br>

  <label for="confirmPassword">Confirm Password:</label>
  <input type="password" id="confirmPassword" name="confirmPassword" required><br><br>

  <input type="submit" value="Reset Password">
</form>
</body>
</html>
