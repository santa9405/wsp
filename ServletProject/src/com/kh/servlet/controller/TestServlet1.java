package com.kh.servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TestServlet1() {
        super();
    }

	public void init(ServletConfig config) throws ServletException {
		System.out.println("init() 메소드 호출(servlet 객체 생성됨)");
		// init() 메소드 다음에는 service()가 호출되고
		// init() 메소드가 없을 경우 자동으로 service() 메소드가 호출됨		
	}

	public void destroy() {
		System.out.println("destroy() 호출됨.");
	}

//	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//	}
//	service() 메소드가 없을 경우
//	자동으로 요청 방식이 get/post임을 확인하여
//	get일 경우 doGet(), post일 경우 doPost()를 호출함.

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		MVC 역할
		
//		Model : 비즈니스 로직 처리 역할(Service, DAO, VO, ...)
		
//		View : 입력(요청)을 받아서  (Model수행) 결과(응답)을 보여주는 역할
//			   (HTML, Servlet, jsp)
		
//		Controller : 요청에 따라 알맞은 Model을 연결하고
//					 Model 처리 결과를 보여줄 View를 선택하는 역할
		
//		HttpServletRequest 
//		: 웹 브라우저에서 사용자가 요청한 내용과 관련 정보를 받아주는 객체
		
//		HttpServletResponse
//		: 요청 처리 결과를 요청한 클라이언트에게 전달하는 역할을 하는 객체
		
//		form 태그에서 제출된 값(Parameter) 얻어오기
//		tip. 파라미터의 자료형은 모두 String이다.
		
//		request.getParameter("name속성");
//		-> 해당 name속성으로 전달된 input태그의 value를 얻어옴
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String age = request.getParameter("age");
		String city = request.getParameter("city");
		String height = request.getParameter("height");
		
//		체크박스 또는 name속성이 같은 여러 input 태그 값을 얻어올 경우
//		String[] 형태로 얻어와야 함.
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
		
//		응답(response) 화면 준비
		response.setContentType("text/html; charset=UTF-8");
		
//		서버에서 작성된 문자열을 출력할 스트림을
//		HttpServletResponse 객체를 이용해서 얻어와
//		클라이언트 응답 화면과 연결
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\r\n" + 
				"<html lang=\"ko\">\r\n" + 
				"<head>\r\n" + 
				"    <meta charset=\"UTF-8\">\r\n" + 
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
				"    <title>TestServlet1 응답 페이지</title>\r\n" + 
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
				"    <h1>개인 정보 입력 결과(GET)</h1>");
		
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
		
//		post로 요청이 와도 doGet() 메소드로 처리하겠다.
//		== get방식이든 post방식이든 같은 방법으로 처리하겠다.
	}

}
