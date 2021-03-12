<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	String pizza = request.getParameter("pizza");
	int price = (int)request.getAttribute("price");
	String toppingJoin = (String)request.getAttribute("toppingJoin");
	String sideJoin = (String)request.getAttribute("sideJoin");

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>PizzaServlet 응답 페이지</title>
<style>
	span.pizza{ color : sandybrown; }
	span.topping{ color : skyblue; }
	span.side{ color : slateblue; }
</style>
</head>
<body>
	<h1>주문 내역</h1>
	<h2>피자는<span class='pizza'> <%=pizza %> </span>,
	토핑은<span class='topping'> <%=toppingJoin %> </span>,<br>
	사이드는<span class='side'> <%=sideJoin %> </span>주문하셨습니다.<br><br>
	
	<strong>총합 : <u> <span class='price'> <%=price %></span>원 </u></strong></h2>
	
	<h1> <span style="color: salmon;">피자 먹고 웃음꽃 피자 ٩(●'▿'●)۶ </span> </h1>

</body>
</html>