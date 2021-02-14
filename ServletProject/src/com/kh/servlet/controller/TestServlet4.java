package com.kh.servlet.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet4 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public TestServlet4() {
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
		
//		응답 화면을 만들기 위한 servlet 코드를 jsp로 작성할 수 있도록
//		요청 위임을 진행
		
//		여기서 요청 위임이란?
//		현재 Servlet의 역할을 JSP로 넘기는 것
		
//		요청 위임 객체 > RequestDispatcher
		RequestDispatcher view 
			= request.getRequestDispatcher("views/testServlet4End.jsp");
		
		/* Dispatcher : 필요 정보를 제공하는 역할, 발송자
		 * RequestDispatcher : 현재 HttpServletRequest 객체에 담긴 정보를 저장하고 있다가
		 * 					      지정된 다음 페이지에서 해당 정보를 볼 수 있게 위임하는 기능.
		 * */
		
//		요청 위임 객체에 가공된 데이터를 전달할 수 있도록 세팅
//		* jsp로 새로운 데이터를 전달하는 방법
//		request.setAttribute(key, value);
		
		request.setAttribute("gift", gift);
		request.setAttribute("foodJoin", String.join(", ", foodArr));
		
//		요청 위임 진행
		view.forward(request, response);
		/* forward 방식 : 요청에 대한 응답 방법 중 하나로 
		 * 기존 파라미터 등의 요청 정보를 유지하며 페이지를 전환하는 방법.
		 * -> forward 방식으로 페이지 전환 시 이전 페이지 주소가 유지됨.
		 * 
		 * A.html --(요청)--> b.do(Servlet) --(요청위임)--> C.jsp
		 * 요청위임 시 forward 방식을 사용할 경우
		 * 요청 위임 객체 RequestDispatcher가 B.do에서 C.jsp로 넘어가지만,
		 * 주소는 B.do로 유지됨.
		 *  -> (B.do의 응답 화면 코드가 C.jsp로 대체됐을 뿐이다)
		 * 
		 * testServlet4.do에서 testServlet4End.jsp로 주소를 위임 하였으나
		 * testServlet4.do 주소가 출력됨
		 * */
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
