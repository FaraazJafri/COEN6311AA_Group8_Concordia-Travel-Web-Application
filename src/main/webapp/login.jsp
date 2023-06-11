<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="css/loginpage.css">
  <title>Login</title>
</head>
<body>

<div id="logo-container">
  <img src="images/travel%20logo%202.jpg" alt="Logo" id="logo">
</div>

<h1>Concordia Travel Booking System</h1>

<div id="login-container">
  <h2>Login</h2>

  <% if (request.getAttribute("errorMessage") != null) { %>
  <p style="color: red;"><%= request.getAttribute("errorMessage") %></p>
  <% } %>
    <% if (request.getAttribute("successMessage") != null) { %>
  <p style="color: green;"><%= request.getAttribute("successMessage") %></p>
    <% } %>
  <form action="login" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required><br><br>

  <label for="password">Password:</label>
  <input type="password" id="password" name="password" required><br><br>

  <input type="submit" value="Login">
</form>
<p><a href="forgotpassword.jsp">Forgot Password?</a></p>
<p>Don't have an account? <a href="signup.jsp">Signup</a></p>
</body>
</html>
