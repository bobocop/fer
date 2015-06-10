<%@page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<% 
	String color = (String) request.getSession().getAttribute("pickedBgCol");
	out.print(hr.fer.zemris.java.dz10.servleti.TagGenerator.generateBodyBGColor(color));
%>
<h1>Home</h1>
<p><a href="colors.jsp">Background color chooser</a></p><br>
<p><a href="squares?a=10&b=20">Get squares in 10-20 range</a></p><br>
<p><a href="stories/funny.jsp">A funny story</a></p><br>
<p><a href="report.jsp">OS usage survey</a></p><br>
<p><a href="powers?a=1&b=100&n=3">Get the powers of 1-100 as an .xls file</a></p><br>
<p><a href="appinfo.jsp">Application info</a></p>
</body>
</html>