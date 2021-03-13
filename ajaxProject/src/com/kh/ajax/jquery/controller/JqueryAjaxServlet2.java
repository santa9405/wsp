package com.kh.ajax.jquery.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jqTest2.do")
public class JqueryAjaxServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// POST 방식 전송 시 문자 인코딩 변경 필수
        request.setCharacterEncoding("UTF-8");
      
        // 파라미터 얻어오기
        String input = request.getParameter("input");
        System.out.println("입력 값 : " + input);
		
		// 응답 데이터 문자 인코딩 지정
		response.setCharacterEncoding("UTF-8");
		
		// 응답을 위한 스트림 연결
	    PrintWriter out = response.getWriter();
	      
	    for(int i=0; i<input.length(); i++) {
	         out.append(input.charAt(i) + "<br>"); // 한 줄씩 출력
		      }
		   }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
