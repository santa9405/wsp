<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp" %>
    
    <%-- errorPage : 현재 페이지에서 오류가 발생할 경우 처리해줄 페이지를 연결하는 지시자 --%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>page 지시자(import, errorPage)</title>
</head>
<body>
   <% 
      List<String> list = new ArrayList<>();
      
      list.add("HTML");
      list.add("Servlet");
      list.add("JSP");
      
      // 강제 에러 발생
      list.add(10, "test");
      // list의 10번 인덱스에 "test"를 추가
      
   %>
   
   <h3>단순 출력</h3>
   <%= list.get(0) %> <br>
   <%= list.get(1) %> <br>
   <%= list.get(2) %> <br>
   
   <h3>scriptlet + expression + html</h3>
   <% for(String str : list) { %>
      <span>출력 : <%= str %> </span> <br>
   <% } %>
   
   <h3>expression + javascript</h3>
   <button type="button" onclick="test();">실행</button>
   
   <script>
      function test(){
         
         alert("<%= list.get(0) %>");
      
      }
   </script>
</body>
</html>