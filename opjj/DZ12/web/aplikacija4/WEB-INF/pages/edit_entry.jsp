<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_14.model.BlogEntry,
				java.text.SimpleDateFormat" %>
<%
		boolean ownerLoggedIn = (request.getAttribute("owner") != null);
		BlogEntry entry = (BlogEntry) request.getAttribute("entry");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<html>
<head><title>Edit <%= entry.getTitle() %></title></head>
<body>
<% if(!ownerLoggedIn) { %>
	<h1>Permission denied</h1>
	<p>Only the owner can edit this entry!</p>
<% } else { %>
	<h1>Edit entry:</h1>
	<form action="edit" enctype="application/x-www-form-urlencoded" method="POST">
  		Title: <br><input maxlength="200" type="text" name ="title" accept="charset=UTF-8" value="<%= entry.getTitle() %>"><br>
  		Text: <br><textarea maxlength="4096" name ="text" cols="80" rows="20"><%= entry.getText() %></textarea><br><br> 
  		<input type="hidden" name="lastModifiedOn" value="<%= dateFormat.format(new java.util.Date()) %>">
  		<input type="hidden" name="entryID" value="<%= entry.getId().toString() %>">
  		<input type="submit" value="Confirm changes"/>
  	</form>
<% } %>
  	<p><a href="/aplikacija4/">Back to index</a></p>
</body>
</html>