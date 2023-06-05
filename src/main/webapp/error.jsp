<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="css/error.css">
  <title>Error</title>
</head>
<body>
<h1 id="error-heading">Error</h1>
<p id="error-message">An error has occurred:</p>

<p class="error-message"><%= request.getAttribute("errorMessage") %></p>
<p class="error-code">Error code: <%= request.getAttribute("errorCode") %></p>

</body>
</html>
