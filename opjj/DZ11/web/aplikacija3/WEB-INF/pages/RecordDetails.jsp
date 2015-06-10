<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_13.model.Unos"%>
<%@page import="hr.fer.zemris.java.tecaj_13.web.HTTPSupport" %>
<%@page import="java.util.List"%>

<html>
	<body>
		<h1>Details for record <%= request.getAttribute("id") %></h1>
			Title: <%= HTTPSupport.escapeForHTMLBody((String) request.getAttribute("title")) %><br>
			Message: <%= HTTPSupport.escapeForHTMLBody((String) request.getAttribute("message")) %><br>
			Created On: <%= HTTPSupport.escapeForHTMLBody((String) request.getAttribute("createdOn")) %><br>
			User E-Mail: <%= HTTPSupport.escapeForHTMLBody((String) request.getAttribute("userEMail")) %><br>
		<br>
		<a href="<%= "unos/edit?id=" + request.getAttribute("id") %>">Edit this record</a>
	</body>
</html>