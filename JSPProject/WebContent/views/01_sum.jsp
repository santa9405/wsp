<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP로 1부터 100까지 합 구하기</title>
</head>
<body>

	<!-- HTML 주석은 클라이언트에게 전달됨 -->
	<%--JSP 주석은 클라이언트에게 전달되지 않음. --%>
	
	<% // 스크립틀릿(scriptlet) : JSP에서 자바 코드를 작성하는 부분 
	
		int sum = 0;
		for(int i=1; i<=100; i++){
			sum += i;
		}
	
		System.out.println("합계 : " + sum);
		// print 구문을 이용한 출력은
		// 이클립스 콘솔창에 출력되는 구문이다.
	%>
		<%-- 표현식 ( <%= %>) --%>
		<h1>1부터 100까지의 합 : <%=sum %> </h1>
</body>
</html>