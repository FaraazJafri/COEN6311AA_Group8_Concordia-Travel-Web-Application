<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Reset Password</title>
</head>
<body>
<h1>Reset Password</h1>
<% if (request.getAttribute("errorMessage") != null) { %>
<p style="color: red;"><%= request.getAttribute("errorMessage") %></p>
<% } %>
<form action="resetpassword" method="post">
  <input type="hidden" id="userID" name="userID" value="<%= request.getAttribute("userID") %>">
  <label for="password">New Password:</label>
  <input type="password" id="password" name="password" required><br><br>

  <label for="confirmPassword">Confirm Password:</label>
  <input type="password" id="confirmPassword" name="confirmPassword" required><br><br>

  <input type="submit" value="Reset Password">
</form>
</body>
</html>
