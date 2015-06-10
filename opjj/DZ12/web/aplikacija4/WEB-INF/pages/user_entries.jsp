<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="
	hr.fer.zemris.java.tecaj_14.model.BlogEntry, 
	hr.fer.zemris.java.tecaj_14.model.BlogComment,
	hr.fer.zemris.java.tecaj_14.model.BlogUser,
	hr.fer.zemris.java.tecaj_14.utility.HTTPSupport"
%>
<%@page import="java.util.List"%>
<%	
	boolean ownerLoggedIn = request.getAttribute("owner") != null;
	List<BlogEntry> entries = (List<BlogEntry>) request.getAttribute("entries");
	String nick = (String) request.getAttribute("nick");
%>
<html>
<head><title>Browse blog entries</title></head>
  <body>
  	<h1><%= HTTPSupport.escapeForHTMLBody(nick) + "'s blog entries" %></h1>
  		<% for(BlogEntry entry : (List<BlogEntry>) entries) { %>
  			<%= "<a href=\"/aplikacija4/servleti/author/"
  						+ HTTPSupport.escapeForTagAttribute(nick) + "/" + entry.getId()
  						+ "\"><i>" + entry.getTitle() + "</i></a>" %>
  			<br>		
  		<% } %>
  		<% if(((List<BlogEntry>) entries).isEmpty()) { %>
  			<p><%= HTTPSupport.escapeForHTMLBody(nick) + " hasn't written anything yet!" %></p>
  		<% } %>
  		<br>
  		<% if(ownerLoggedIn) { %>
  			<a href="<%= "/aplikacija4/servleti/author/" 
  			+ HTTPSupport.escapeForTagAttribute(nick) + "/new" %>">Add new entry</a><br>
  			<br>
  		<% } %>
  		<a href="/aplikacija4/">Back to index</a>
  </body>
</html>