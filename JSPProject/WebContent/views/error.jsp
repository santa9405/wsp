<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
    
    <%-- isErrorPage : 현재 페이지가 에러 처리 페이지인지 확인하는 지시자 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error 처리 페이지</title>
</head>
<body>
	<h1>에러가 발생했습니다.</h1>
	
	<p>
		isErrorPage에서만 사용 가능한 객체 : exception<br>
		
		<%= exception %>
	</p>
</body>
</html>