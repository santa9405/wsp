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

@WebServlet("/member/updateMember.do")
public class UpdateMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// 전송된 파라미터를 변수에 저장
		String memberEmail = request.getParameter("email");
		String phone1 = request.getParameter("phone1");
		String phone2 = request.getParameter("phone2");
		String phone3 = request.getParameter("phone3");
		
		// 전화번호를 '-' 구분자로 하여 하나로 합치기
		String memberPhone = phone1 + "-" + phone2 + "-" + phone3;	
		
		String post = request.getParameter("post"); // 우편번호
		String address1 = request.getParameter("address1"); // 도로명 주소
		String address2 = request.getParameter("address2"); // 상세 주소
		
		String memberAddress = post + "," + address1 + "," + address2;
		
		String[] interest = request.getParameterValues("memberInterest");
		
		String memberInterest = null;
		
		// 배열이 null이 아닐 때만 한 줄로 합쳐서 memberInterest에 저장
		if(interest != null) memberInterest = String.join(",", interest);
		
		// Session에 있는 로그인 정보를 얻어옴 -> 회원 번호 사용
		HttpSession session = request.getSession();
		Member loginMember = (Member)session.getAttribute("loginMember");
		
		// 얻어온 수정 정보와 회원 번호를 하나의 Member객체에 저장
		Member member = new Member();
		member.setMemberNo(loginMember.getMemberNo());
		member.setMemberEmail(memberEmail);
		member.setMemberPhone(memberPhone);
		member.setMemberAddress(memberAddress);
		member.setMemberInterest(memberInterest);
		
		try {
			// 비즈니스 로직 수행 후 결과 반환
			int result = new MemberService2().updateMember(member);
			
			String swalIcon = null;
			String swalTitle = null;
			
			if(result > 0) { // 성공
				swalIcon = "success";
				swalTitle = "수정 완료!";
				
				// DB 데이터가 갱신된 경우 Session에 있는 회원 정보도 갱신되어야 한다.
				
				// 기존 로그인 정보에서 id를 얻어와 갱신에 사용된 member 객체에 저장
				member.setMemberId( loginMember.getMemberId() );
				member.setMemberName( loginMember.getMemberName() );
				member.setMemberGrade( loginMember.getMemberGrade() );
				// -> member 객체가 갱신된 회원 정보를 모두 갖게됨
				
				// Session에 있는 loginMember 정보를 member로 갱신
				session.setAttribute("loginMember", member);
				
			}else { // 실패
				swalIcon = "error";
				swalTitle = "수정 실패";
			}
			
			// 로그인 실패시와 같은 코드
			session.setAttribute("swalIcon", swalIcon);
			session.setAttribute("swalTitle", swalTitle);
			
			// 수정 완료 후 다시 내정보 페이지로 재요청
			response.sendRedirect("myPage.do");
			//response.sendRedirect(request.getHeader("referer"));
			
		}catch(Exception e) {
			e.printStackTrace();
			
			request.setAttribute("errorMsg", "수정 과정에서 오류가 발생했습니다.");
			
			String path = "/WEB-INF/views/common/errorPage.jsp";
			
			RequestDispatcher view = request.getRequestDispatcher(path);
			view.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	
	
	
	
	
	
	
}
