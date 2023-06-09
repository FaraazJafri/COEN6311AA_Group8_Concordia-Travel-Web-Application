<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/forgotpassword.css">
    <title>Forgot Password</title>
</head>
<body>

<div id="logo-container">
    <img src="images/travel%20logo%202.jpg" alt="Logo" id="logo">
</div>

<h1>Travel Packages</h1>

<% if (request.getAttribute("errorMessage") != null) { %>
<p style="color: red;"><%= request.getAttribute("errorMessage") %></p>
<% } %>
<form action="forgotpassword" method="post">
    <h2>Forgot Password</h2>
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required><br><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br><br>

    <label for="phone">Phone Number:</label>
    <input type="text" id="phone" name="phone" required><br><br>

    <input type="submit" value="Submit">
</form>
</body>
</html>
