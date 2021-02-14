package com.kh.servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TestServlet3() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		post 방식의 데이터 전달 -> 문자 인코딩이 깨짐 - UTF-8형식으로 세팅
		request.setCharacterEncoding("UTF-8");
		
//		전달된 파라미터를 변수에 저장
		
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String age = request.getParameter("age");
		String city = request.getParameter("city");
		String height = request.getParameter("height");
		String[] foodArr = request.getParameterValues("food");
		
//		요청 데이터를 이용해서 새로운 응답 데이터를 생성
		String gift = null;
		
//		나이대에 따른 선물 추천
		switch(age) {
		case "10대 미만" : gift = "슬라임"; break;
		case "10대" : gift = "닌텐도 스위치"; break;
		case "20대" : gift = "스마트폰"; break;
		case "30대" : gift = "돈"; break;
		case "40대" : gift = "건강"; break;
		case "50대" : gift = "청춘"; break;
		}
		
//		응답 화면 출력 준비
		response.setContentType("text/html; charset=UTF-8");
		
//		작성된 HTML 문자열을 출력하기 위한 스트림 준비
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\r\n" + 
				"<html lang=\"ko\">\r\n" + 
				"<head>\r\n" + 
				"    <meta charset=\"UTF-8\">\r\n" + 
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
				"    <title>TestServlet2 응답 페이지</title>\r\n" + 
				"    <style>\r\n" + 
				"        h1{ color : gray; }\r\n" + 
				"        span.name{ color : coral; }\r\n" + 
				"        span.gender{ color : crimson; }\r\n" + 
				"        span.age{ color : slateblue; }\r\n" + 
				"        span.city{ color : skyblue; }\r\n" + 
				"        span.height{ color : salmon; }\r\n" + 
				"        span.food{ color : seagreen; }\r\n" + 
				"        span{ font-weight: bold;}\r\n" + 
				"    </style>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"    <h1>개인 정보 입력 결과(POST)</h1>");
		
		out.printf("<span class='name'>%s</span>님은\r\n" + 
				"    <span class='age'>%s</span>이며,\r\n" + 
				"    <span class='city'>%s</span>에 사는\r\n" + 
				"    키<span class='height'>%s</span>cm인\r\n" + 
				"    <span class='gender'>%s</span> 입니다.\r\n" + 
				"    <br>\r\n" + 
				"    좋아하는 음식은\r\n" + 
				"    <span class='food'>%s</span> 입니다.\r\n"  
				, name, age, city, height, gender, 
							String.join(", ", foodArr) );
		
		out.println("<h3>" + age + "에 추천할만한 선물</h3>");
		out.println("<h4>" + gift + "선물은 어떠신가요?");
		
		
		out.println("</body>\r\n" + "</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	
	
	
	
	
	
	
	
}
