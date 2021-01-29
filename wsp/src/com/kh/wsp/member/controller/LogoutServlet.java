package com.kh.wsp.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/logout.do")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// 로그인 상태 -> DB에서 조회한 회원 정보가 Session에 존재하는 것
		// 로그아웃 상태 -> Session에 회원 정보가 없는 것
		
		// 세션 만료(세션 무효화)
		request.getSession().invalidate();
		
		// 로그아웃 후 메인 or 로그아웃을 수행한 페이지로 리다이렉트
		response.sendRedirect(request.getContextPath()); // 로그아웃 후 메인
		//response.sendRedirect(request.getHeader("referer")); // 로그아웃을 수행한 페이지로 리다이렉트
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
