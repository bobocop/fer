<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="hr.fer.zemris.java.tecaj_13.web.HTTPSupport"%>
<%@page import="hr.fer.zemris.java.tecaj_13.webforms.UnosForm"%>
<%
  UnosForm uf = (UnosForm) request.getAttribute("model.object");
  String path = (String) request.getAttribute("path");
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<html>
  <body>
  <h1><%= HTTPSupport.escapeForTagAttribute((String) request.getAttribute("action")) + " record:" %></h1>
  	<form action="<%= HTTPSupport.escapeForTagAttribute((String) request.getAttribute("savepath")) %>" enctype="application/x-www-form-urlencoded" method="GET">
  	Title:  <br><input type="text" name ="title" accept="charset=UTF-8" value="<%= HTTPSupport.escapeForTagAttribute(uf.getTitle()) %>"/><br>
  	Message: <br><textarea cols="48" rows="4" name ="message"><%= HTTPSupport.escapeForHTMLBody(uf.getMessage()) %></textarea><br>
  	User E-Mail: <br> <input type="text" name ="userEMail" value="<%= HTTPSupport.escapeForTagAttribute(uf.getUserEMail()) %>"/><br>
  	<input type="hidden" name ="createdOn" value="<%= dateFormat.format(new java.util.Date())%>"/><br>
  	<input type="hidden" name ="id" value="<%= uf.getId() != null ? uf.getId() : "" %>"/>
  	<input type="submit" value="Submit"/>
  	</form>
  	<% if(uf.hasError()) { %>
  	<font color="red">The following errors have occured:</font><br>
  	<%= HTTPSupport.escapeForHTMLBody(uf.getErrorFor("title"))+"<br>" %>
  	<%= HTTPSupport.escapeForHTMLBody(uf.getErrorFor("message"))+"<br>" %>
	<%= HTTPSupport.escapeForHTMLBody(uf.getErrorFor("userEMail"))+"<br>" %>  
	<% } %>	
	<br>
  </body>
</html>