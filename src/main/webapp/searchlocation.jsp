<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 24-05-2023
  Time: 19:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Search Packages</title>
</head>
<body>
<jsp:include page="menu.jsp" />

<h1>Search Packages by Location</h1>
<form action="SearchLocationServlet" method="GET">
  <label for="location">Location:</label>
  <input type="text" id="location" name="location">
  <button type="submit">Search</button>
</form>
</body>
</html>
