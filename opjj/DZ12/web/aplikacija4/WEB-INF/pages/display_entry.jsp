<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import=
	"hr.fer.zemris.java.tecaj_14.model.BlogEntry,
	hr.fer.zemris.java.tecaj_14.utility.JSPFunc,
	hr.fer.zemris.java.tecaj_14.utility.HTTPSupport" 
%>
	<%
		boolean ownerLoggedIn = request.getAttribute("owner") != null;
		BlogEntry entry = (BlogEntry) request.getAttribute("entry");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	%>
<html>
	<head><title>View entry <%= entry.getTitle() %></title></head>
	<body>
		<%= 
		JSPFunc.formatEntryAsHTML(entry) 
		+ "<b>-------</b><br>" 
		+ JSPFunc.formatEntryCommentsAsHTML(entry)
		+ "<b>-------</b><br>"
		%>
		<% if(ownerLoggedIn) { %>
			<p><a href="/aplikacija4/servleti/author/
					<%= entry.getCreator().getNick() %>
					/edit?id=<%= entry.getId() %>">Edit this entry</a></p>
		<% } %>
		<h3>Add comment: </h3>
		<form action="addcomment" enctype="application/x-www-form-urlencoded" method="POST">
  		E-Mail: <br> <input type="text" name ="usersEMail" accept="charset=UTF-8"><br>
  		Message: <br> <textarea maxlength="4096" name ="message" cols="40" rows="5"></textarea><br><br>
  		<input type="hidden" name="postedOn" value="<%= dateFormat.format(new java.util.Date()) %>">
  		<input type="hidden" name="entryID" value="<%= entry.getId().toString() %>">
  		<input type="submit" value="Submit"/>
  		</form>
  		<br>
  		<a href="/aplikacija4">Back to index</a>
	</body>
</html>