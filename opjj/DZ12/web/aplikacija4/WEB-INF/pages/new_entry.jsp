<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.text.SimpleDateFormat" %>
<%
	boolean ownerLoggedIn = (request.getAttribute("owner") != null);
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<html>
<head><title>Add new entry</title></head>
<body>
<% if(!ownerLoggedIn) { %>
	<h1>Permission denied</h1>
	<p>Only the owner can add an entry!</p>
<% } else { %>
	<h1>Add entry:</h1>
	<form action="new" enctype="application/x-www-form-urlencoded" method="POST">
  		Title: <br><input maxlength="200" type="text" name ="title" accept="charset=UTF-8"><br>
  		Text: <br><textarea maxlength="4096" name ="text" cols="80" rows="20"></textarea><br><br>
  		<input type="hidden" name="createdOn" value="<%= dateFormat.format(new java.util.Date()) %>">
  		<input type="submit" value="Submit"/>
  	</form>
<% } %>
  	<p><a href="/aplikacija4/">Back to index</a></p>
</body>
</html>