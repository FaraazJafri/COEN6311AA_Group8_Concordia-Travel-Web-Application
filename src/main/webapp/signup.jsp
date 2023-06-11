<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/signup.css">
    <title>Sign Up</title>
</head>
<body>

<% if (request.getAttribute("errorMessage") != null) { %>
<p style="color: red;"><%= request.getAttribute("errorMessage") %>
</p>
<% } %>

<div id="logo-container">
    <img src="images/travel%20logo%202.jpg" alt="Logo" id="logo">
</div>

<h1>Concordia Travel Booking System</h1>
<div id="signup-container">
<form action="signup" method="post">
    <label for="firstName">First Name:</label>
    <input type="text" id="firstName" name="firstName" required><br><br>

    <label for="lastName">Last Name:</label>
    <input type="text" id="lastName" name="lastName" required><br><br>

    <label for="dateOfBirth">Date of Birth:</label>
    <input type="date" id="dateOfBirth" name="dateOfBirth" required><br><br>

    <label for="email">Email Address:</label>
    <input type="email" id="email" name="email" required><br><br>

    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required><br><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br><br>

    <label for="phoneNumber">Phone Number:</label>
    <input type="text" id="phoneNumber" name="phoneNumber" required><br><br>

    <label for="age">Age:</label>
    <input type="number" id="age" name="age" required><br><br>

    <label for="gender">Gender:</label>
    <select id="gender" name="gender">
        <option value="Male">Male</option>
        <option value="Female">Female</option>
        <option value="Other">Other</option>
    </select><br><br>

    <input type="submit" value="Sign Up">
</form>
<p>Already have an account? <a href="login.jsp">Login</a></p>

</div>
</body>
</html>
