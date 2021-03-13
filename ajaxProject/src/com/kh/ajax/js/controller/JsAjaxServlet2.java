package com.kh.ajax.js.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jsAjax2.do")
public class JsAjaxServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// POST 방식으로 데이터 전달 -> 문자 인코딩 깨짐 -> 해결 필요
		request.setCharacterEncoding("UTF-8");
		
		// 전달된 파라미터 변수에 저장
		String pname = request.getParameter("pname");
		String price = request.getParameter("price");
		
		System.out.println(pname + " / " + price);
		
		// 응답 데이터 문자 인코딩 처리
		response.setCharacterEncoding("UTF-8");
		
		// 응답 데이터 전송 스트림 연결
		PrintWriter out = response.getWriter();
		
		out.append("서버에서 비동기로 전송한 값<br>");
		out.append(pname + "의 가격은 " + price + "원 입니다.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
