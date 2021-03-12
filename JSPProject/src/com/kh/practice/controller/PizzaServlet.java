package com.kh.practice.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PizzaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PizzaServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		// 선택된 피자와 토핑, 사이드메뉴의 총 가격을 계산하여
		// 요청 위임 객체에 추가 후
		// 응답화면용 JSP를 만들어 출력하기
		String pizza = request.getParameter("pizza");
		String[] toppingArr = request.getParameterValues("topping");
		String[] sideArr = request.getParameterValues("side");
		
		int price = 0;
		switch(pizza) {
		case "치즈피자" : price += 5000; break;
		case "콤비네이션피자" : price += 6000; break;
		case "포테이토피자" : price += 7000; break;
		case "고구마피자" : price += 7000; break;
		case "불고기피자" : price += 8000; break;
		}
		
		for(int i=0; i<toppingArr.length; i++) {
			switch(toppingArr[i]) {
			case "고구마무스" : price += 1000; break;
			case "콘크림무스" : price += 1500; break;
			case "파인애플토핑" : price += 2000; break;
			case "치즈토핑" : price += 2000; break;
			case "치즈크러스트" : price += 2000; break;
			case "치즈바이트" : price += 3000; break;
			}
		}
		
		for(int i=0; i<sideArr.length; i++) {
			switch(sideArr[i]) {
			case "오븐구이통닭" : price += 9000; break;
			case "치킨스틱&윙" : price += 4900; break;
			case "치즈오븐스파게티" : price += 4000; break;
			case "새우링&웨지감자" : price += 3500; break;
			case "갈릭포테이토" : price += 3000; break;
			case "콜라" : price += 1500; break;
			case "사이다" : price += 1500; break;
			case "갈릭소스" : price += 500; break;
			case "피클" : price += 300; break;
			case "핫소스" : price += 100; break;
			case "파마산 치즈가루" : price += 100; break;
			}
		}
		
		
		RequestDispatcher view 
		= request.getRequestDispatcher("views/result.jsp");
		
		request.setAttribute("pizza", pizza);
		request.setAttribute("price", price);
		request.setAttribute("toppingJoin", String.join(", ", toppingArr));
		request.setAttribute("sideJoin", String.join(", ", sideArr));
		
		view.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
