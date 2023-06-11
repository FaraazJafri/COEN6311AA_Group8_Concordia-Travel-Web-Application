<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="css/homepage.css">
  <title>Package Modified Successfully</title>
</head>
<body>
<jsp:include page="menu.jsp" />

<h1><%= request.getAttribute("heading") %></h1>
<p><%= request.getAttribute("message") %></p>
<p><a href="homepage.jsp">Go to Homepage</a></p>
</body>
</html>