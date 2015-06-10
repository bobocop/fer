<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<% 
	String color = (String) request.getSession().getAttribute("pickedBgCol");
	out.print(hr.fer.zemris.java.dz10.servleti.TagGenerator.generateBodyBGColor(color));
%>
<h1>Info</h1>
<p>The application has been running for </p>
<%
	Object attr = request.getServletContext().getAttribute("uptime");
	if(attr != null) {
		Long uptime = (System.currentTimeMillis() 
				- ((Long) request.getServletContext().getAttribute("uptime")));
		out.println(
				String.format("%d days, %d hours, %d minutes, %d seconds, %d miliseconds", 
				uptime/1000/(24*3600), uptime/1000/3600, 
				((uptime/1000)%3600)/60, (uptime/1000)%60, 
				uptime%1000
				));
	} else {
		out.println("No data");
	}
%>
</body></html>