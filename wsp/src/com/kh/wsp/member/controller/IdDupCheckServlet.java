package com.kh.wsp.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.wsp.member.model.service.MemberService;
import com.kh.wsp.member.model.service.MemberService2;

@WebServlet("/member/idDupCheck.do")
public class IdDupCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// post 형식으로 데이터를 전달받지만 ID에는 한글이 포함되지 않아 인코딩을 변경하지 않아도 글자가 깨지지 않음.
		
		String id = request.getParameter("id");
		
		try {
			// 1) 비즈니스 로직을 호출하여 결과 반환 받기
			int result = new MemberService2().idDupCheck(id);
			
			// 팝업창으로 중복 검사-------------------------------------------------------
			// 2) 반환 결과를 request에 세팅하여 요청 위임 진행
			//request.setAttribute("result", result);
			// 요청 위임 객체 만들기
			//RequestDispatcher view = request.getRequestDispatcher("idDupForm.do");
																	// 상대 경로
			//view.forward(request, response);
			//-----------------------------------------------------------------------
			
			
			// Ajax로 중복 검사----------------------------------------------------------
			response.getWriter().print(result);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			String path = "/WEB-INF/views/common/errorPage.jsp";
			request.setAttribute("errorMsg", "아이디 중복 검사 과정에서 오류 발생!");
			RequestDispatcher view = request.getRequestDispatcher(path);
			view.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
