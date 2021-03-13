package com.kh.ajax.jquery.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.ajax.jquery.model.service.MemberService;

@WebServlet("/member/idDupCheck.do")
public class IdDupCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// 전달받은 파라미터 저장
		String inputId = request.getParameter("inputId");
		
		try {
			int result = new MemberService().idDupCheck(inputId);
			
			PrintWriter out = response.getWriter();
			
			out.append(result+"");
			// 문자열만 내보낼 수 있음
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
