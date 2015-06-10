<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_14.utility.HTTPSupport" %>
<%@page import="hr.fer.zemris.java.tecaj_14.webforms.UserUnosForm" %>

<% 
	UserUnosForm uuf = (UserUnosForm) request.getAttribute("model.uuf"); 
	uuf.setPassword(null);	// always reset the password field
%>

<html>
  <body>
  <h1>New user registration</h1>
  	<form action="register" enctype="application/x-www-form-urlencoded" method="POST">
  	Nick: <br> <input type="text" name ="nick" accept="charset=UTF-8" value="<%= HTTPSupport.escapeForTagAttribute(uuf.getNick()) %>"/><br>
  	E-Mail: <br> <input type="text" name ="email" accept="charset=UTF-8" value="<%= HTTPSupport.escapeForTagAttribute(uuf.getEmail()) %>"/> * <br>
  	Password: <br> <input type="password" name ="password" accept="charset=UTF-8"><br>
  	First name:  <br> <input type="text" name ="firstName" accept="charset=UTF-8" value="<%= HTTPSupport.escapeForTagAttribute(uuf.getFirstName()) %>"/> * <br>
  	Last name: <br> <input type="text" name ="lastName" accept="charset=UTF-8" value="<%= HTTPSupport.escapeForTagAttribute(uuf.getLastName()) %>"/> * <br>
  	<br><font size="1.5"><i>The fields marked with an * are optional.</i></font><br><br>
  	<input type="submit" value="Submit"/>
  	</form>
  	<% String nickInUse = (String) request.getAttribute("nick.used"); %>
  	<%= "<font color=\"red\">" + ((nickInUse != null) ? nickInUse : "") + "</font>"  %>
  	<% if(uuf.hasErrors()) { %>
  	<font color="red">The following errors have occured:</font><br>
  		<%= (String) request.getAttribute("nick.used") %> 
  		<br>
  		<% for(String errName : uuf.getErrorNames()) { %>
  		<%= HTTPSupport.escapeForHTMLBody(uuf.getErrorFor(errName))+"<br>" %>
  		<% } %>
	<% } %>
	<br>
  	<a href="/aplikacija4">Back to index</a>
  </body>
</html>