package com.kh.servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TestServlet2() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
//		POST 방식으로 데이터 전송 시 문자 인코딩이 정해져 있지 않아
//		아스키 코드 범위(영어, 숫자, 특수문자 몇개)를 제외한 문자는
//		모두 깨진다.
		
//		POST 방식으로 데이터 전송 시 문자 인코딩이 지정되지 않아
//		전달되는 데이터의 문자 인코딩이 브라우저의 기본값(ISO-8859-1)을 따름.
//		UTF-8(이클립스 세팅)과 일치 하지 않으므로
//		파라미터를 얻어오기 전에 request의 문자 인코딩을 UTF-8로 변경.
		
//		요청 데이터(파라미터)를 모두 얻어와 각 변수에 저장
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String age = request.getParameter("age");
		String city = request.getParameter("city");
		String height = request.getParameter("height");
		String[] foodArr = request.getParameterValues("food");
		
//		파라미터 전달 확인
		System.out.println("name : " + name);
		System.out.println("gender : " + gender);
		System.out.println("age : " + age);
		System.out.println("city : " + city);
		System.out.println("height : " + height);
		
//		for(String food : foodArr) {
//			System.out.println("food : " + food);
//		}
		
		for(int i=0; i<foodArr.length; i++) {
			System.out.println("foodArr [" + i + "] " + foodArr[i]);
		}
		
//		응답 화면 준비
		response.setContentType("text/html; charset=UTF-8");
		
//		응답 화면을 내보낼 스트림 연결
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
				"    <span class='food'>%s</span> 입니다.\r\n" + 
				"</body>\r\n" + 
				"</html>", name, age, city, height, gender, 
							String.join(", ", foodArr) );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
