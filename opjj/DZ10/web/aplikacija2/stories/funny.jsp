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
<%
	java.util.Random rand = new java.util.Random();
	String[] textColors = {"black", "green", "blue", "red", "yellow"};
	out.print("<font color=\"" + textColors[rand.nextInt(textColors.length)] 
				+ "\">");
%>
<p>
Have you ever sent a text message only to later find out your phone's 
software AutoCorrected the **** out of it, making it unreadable or, 
even worse, completely changing the meaning? For example, in trying to fix 
what it perceives as a spelling error, the phone might change "basket" into 
"casket," or "gasoline" into "Vaseline," or "hi grandma" into "I smoke meth 
and worship strange gods."
</p>
<p>
Well, telegraph users in the 19th and early 20th centuries had to put up 
with a surprisingly similar annoyance: Just because you sent the right 
signals through the wire didn't necessarily mean the same words would reach 
the other side. The Victorian version of AutoCorrect was called "hog-Morse" 
after the tendency for the word "home" to come through as "hog," resulting, 
for example, in a message that said "home sweet home" becoming "hog swat hog." 
Other examples include turning "cow" into "coat," "wife" into "wig" 
and "U.S. Navy" into "us nasty," which had to be the name of at least 
one '80s R&B group. This led to awkward situations, like 
the time a commission firm in Richmond, Virginia, received a 
telegram inquiring about the price of "undressed staves" ... 
where "staves" (wooden posts) had accidentally been replaced with "slaves." 
An employee at the firm replied: "No trade in naked chattel since 
Emancipation Proclamation."</p>
<p>Read more: 5 Internet Annoyances That Are Way Older Than the Internet | @
<a href="http://www.cracked.com/article_19847_5-internet-annoyances-that-are-way-older-than-internet.html">Cracked.com</a>
</p>
<br>
<a href="/aplikacija2/index.jsp">Back to index</a>
</body>
</html>