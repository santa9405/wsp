package com.kh.wsp.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.wsp.member.model.service.MemberService;
import com.kh.wsp.member.model.service.MemberService2;
import com.kh.wsp.member.model.vo.Member;

@WebServlet("/member/updateStatus.do")
public class UpdateStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// 현재 비밀번호
		String currentPwd = request.getParameter("currentPwd");
		
		// 현재 로그인한 회원의 정보를 얻어옴
		HttpSession session = request.getSession();
		// loginMember 객체에는 비밀번호가 담겨있지 않음
		Member loginMember = (Member)session.getAttribute("loginMember");
		
		try {
			loginMember.setMemberPwd(currentPwd);
			// 비즈니스 로직 처리 후 결과 반환 받기
			int result = new MemberService2().updateStatus(loginMember);

			String swalIcon = null;
			String swalTitle = null;
			
			// 마지막에 경로로 사용할 변수
			String url = null;
			
			if(result > 0) {
				swalIcon = "success";
				swalTitle = "탈퇴 성공.";
				
				// 탈퇴 성공 시 -> 로그아웃 + 메인페이지로 전환
				
				// 로그아웃 == 세션 무효화
				session.invalidate();
				// 세션 무효화 시 현재 얻어온 세션을 사용할 수 없는 문제점이 있다!
				// -> 새로운 세션 얻어오기
				session = request.getSession();
				
				// 시작주소로
				url = request.getContextPath();
				
			}else if(result == 0) {
				swalIcon = "error";
				swalTitle = "탈퇴 실패.";
				
				// 탈퇴 페이지 유지
				url = "session.do";
			}else {
				swalIcon = "warning";
				swalTitle = "비밀번호 불일치";
				
				// 탈퇴 페이지 유지
				url = "session.do";
			}
			
			// 알림창 화면에 전달
			session.setAttribute("swalIcon", swalIcon);
			session.setAttribute("swalTitle", swalTitle);
			
			// loginMember 세션에서 삭제
			//session.removeAttribute( "loginMember" );
			
			// 탈퇴 후 메인으로 리다이렉트
			response.sendRedirect(url);
			
		}catch(Exception e) {
			e.printStackTrace();
			String path = "/WEB-INF/views/common/errorPage.jsp";
			
			request.setAttribute("errorMsg", "회원탈퇴 중 오류 발생");
			RequestDispatcher view = request.getRequestDispatcher(path);
			view.forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
