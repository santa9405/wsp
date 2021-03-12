<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
 <%@ page import="java.util.Date" %>   
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>include</title>
</head>
<body>
	<h1>현재 시간 : 
		<%@ include file="currentTime.jsp" %>
	</h1>
</body>
</html>