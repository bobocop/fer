<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<% 
	String color = (String) request.getSession().getAttribute("pickedBgCol");
	out.print(hr.fer.zemris.java.dz10.servleti.TagGenerator.generateBodyBGColor(color));
%>
<h1>Invalid arguments provided</h1>
<p>The provided values must fulfil the following constraints:</p>
<br>
<p><b>a</b>, integer in [-100, 100]</p>
<p><b>b</b>, integer in [-100, 100], <b>b</b> >= <b>a</b></p>
<p><b>n</b>, integer in [1, 5]</p>
</body>
</html>