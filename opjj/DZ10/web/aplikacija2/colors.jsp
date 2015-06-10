<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<% 
	String color = (String) request.getSession().getAttribute("pickedBgCol");
	out.print(hr.fer.zemris.java.dz10.servleti.TagGenerator.generateBodyBGColor(color));
%>
<h1>Pick a background color:</h1>
<br>
<a href="setcolor?picked=white">WHITE</a>
<a href="setcolor?picked=red">RED</a>
<a href="setcolor?picked=green">GREEN</a>
<a href="setcolor?picked=cyan">CYAN</a>
</body></html>