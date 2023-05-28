<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Search Travel Packages</title>
</head>
<body>
<jsp:include page="menu.jsp" />

<h1>Search Travel Packages</h1>
<form action="SearchPriceServlet" method="GET">
  <label for="minPrice">Minimum Price:</label>
  <input type="number" id="minPrice" name="minPrice" required><br><br>

  <label for="maxPrice">Maximum Price:</label>
  <input type="number" id="maxPrice" name="maxPrice" required><br><br>

  <input type="submit" value="Search">
</form>
</body>
</html>

