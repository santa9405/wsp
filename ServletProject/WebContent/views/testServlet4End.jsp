<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	// 위임받은 request에 있는 파라미터 값 얻어오기
	String name = request.getParameter("name");
	String gender = request.getParameter("gender");
	String age = request.getParameter("age");
	String city = request.getParameter("city");
	String height = request.getParameter("height");
	
	// 위임받은 request에 새롭게 세팅된 가공 데이터 얻어오기
	// setArrtibute()로 세팅된 값의 자료형은 Object가 된다.
	// -> 알맞은 형변환 필요
	String gift = (String)request.getAttribute("gift");
	String foodJoin = (String)request.getAttribute("foodJoin");
%>
    
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TestServlet1 응답 페이지</title>
    <style>
        h1{ color : red; }
        span.name{ color : orange;}
        span.gender{ color : yellow;}
        span.age{ color : green;}
        span.city{ color : blue;}
        span.height{ color : navy;}
        span.food{ color : purple;}
        span{font-weight: bold;}
    </style>
</head>
<body>
    <h1>개인 정보 입력 결과(Servlet + JSP)</h1>
    <span class='name'> <%=name %> </span>님은
    <span class='age'> <%=age %> </span>이며,
    <span class='city'> <%=city %> </span>에 사는 키
    <span class='height'> <%=height %> </span>cm 인 
    <span class='gender'> <%= gender %> </span> 입니다.
    <br>좋아하는 음식은
    <span class='food'> <%=foodJoin %></span> 입니다.
    
    <h3> <%=age %>에 추천할만한 선물 </h3>
    <h4> <%=gift %>는 어떠신가요?</h4>
</body>
</html>
    