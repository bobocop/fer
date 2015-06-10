<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<body bgcolor="
<%
	String color = (String) request.getSession().getAttribute("pickedBgCol");
	if(color == null) {
		out.print("white");	// white
	} else {
		out.print(color);
	}
%>
">
<h1>The requested page is currently not available.</h1>
<br>
<a href="/aplikacija2/index.jsp">Back to index</a>
</body></html>