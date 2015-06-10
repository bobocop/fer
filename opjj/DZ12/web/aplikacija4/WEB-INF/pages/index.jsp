<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_14.utility.HTTPSupport,
 				hr.fer.zemris.java.tecaj_14.model.BlogUser,
 				java.util.List"%>
<% 
	Long id = (Long) request.getServletContext().getAttribute("current.user.id");
	boolean loggedIn = (id != null); 
%>
<html>
<head>
<title>A simple blogging service</title>
</head>
<body>
<% if(loggedIn) { %>
	<% 
		String firstName = (String) request.getServletContext().getAttribute("current.user.fn");
		String lastName = (String) request.getServletContext().getAttribute("current.user.ln");
	%>
	<h1>
	Welcome 
	<%= firstName != null ? HTTPSupport.escapeForHTMLBody(firstName) : "" %> 
	<%= lastName != null ? HTTPSupport.escapeForHTMLBody(lastName) : "" %>
	!
	</h1>
	<p><a href="author/
	<%= (String) request.getServletContext().getAttribute("current.user.nick") %>
	">My blog entries</a></p>
	<p><a href="main?logout=true">Log out</a></p>
<% } else { %>
	<h1>
	Not logged in!
	</h1>
	<form action="main" enctype="application/x-www-form-urlencoded" method="POST">
  	Nick: <br> <input type="text" name ="nick" accept="charset=UTF-8"><br>
  	Password: <br> <input type="password" name ="password" accept="charset=UTF-8"><br>
  	<% String noUsrErr = (String) request.getAttribute("err.nouser"); %>
  	<% String invPassErr = (String) request.getAttribute("err.invalidpass"); %>
  	<%= "<font color=\"red\">" + ((noUsrErr != null) ? noUsrErr : "") + "</font>" %>
  	<%= "<font color=\"red\">" + ((invPassErr != null) ? invPassErr : "") + "</font>" %>
  	<br>
  	<input type="submit" value="Submit"/>
  	</form>
  	<p>No account? Register <a href="register">here</a>.</p>
<% } %>
<h3>Registered authors:</h3>
<% for(BlogUser usr : (List<BlogUser>) request.getServletContext().getAttribute("users")) { %>
	<a href="/aplikacija4/servleti/author/
	<%= HTTPSupport.escapeForTagAttribute(usr.getNick()) %>">
	<%= HTTPSupport.escapeForHTMLBody(usr.getNick()) %>
	</a><br>
<% } %>
</body>
</html>