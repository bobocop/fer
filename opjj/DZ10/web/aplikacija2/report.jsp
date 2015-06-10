<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<% 
	String color = (String) request.getSession().getAttribute("pickedBgCol");
	out.print(hr.fer.zemris.java.dz10.servleti.TagGenerator.generateBodyBGColor(color));
%>
<h1>OS usage</h1>
<p>Here are the results of OS usage in survey that we completed:</p>
<br>
<img src="/aplikacija2/reportimage">
</body>
</html>