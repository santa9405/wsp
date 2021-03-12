<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	Date now = new Date();

	SimpleDateFormat smft 
		= new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
	
	String currentTime = smft.format(now);
%>

<span style="color:blue"> <%=currentTime %> </span>