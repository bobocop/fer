<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<% 
	String color = (String) request.getSession().getAttribute("pickedBgCol");
	out.print(hr.fer.zemris.java.dz10.servleti.TagGenerator.generateBodyBGColor(color));
%>
<h1>A table of squares:</h1>
<table border="1">
<%
	int[] results = (int[]) request.getAttribute("sqResults");
	for(int i = 0; i < results.length-1; i++) {
		out.write("<tr><td>" + (results[results.length-1]+i) 
				+ "</td><td>" + results[i] + "</td></tr>");
	}
%>
</table>
</body>
</html>
