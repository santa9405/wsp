package com.kh.ajax.jquery.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kh.ajax.jquery.model.service.MemberService;
import com.kh.ajax.jquery.model.vo.Member;

@WebServlet("/member/selectMember.do")
public class SelectMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// 입력된 아이디 얻어오기
		String inputId = request.getParameter("inputId");
		
		try {
			// 비즈니스 로직(회원 정보 조회) 수행 후 결과 반환 받기
			Member member = new MemberService().selectMember(inputId);
			
			// 조회 성공 시
			if(member != null) {
				
				// JSONObject를 이용하여 member 데이터를 json으로 변경
				//JSONObject jsonMember = new JSONObject();
				
				//jsonMember.put("memberId", member.getMemberId());
				//jsonMember.put("memberName", member.getMemberName());
				//jsonMember.put("memberEmail", member.getMemberEmail());
				//jsonMember.put("memberInterest", member.getMemberInterest());
				//jsonMember.put("memberEnrollDate", member.getMemberEnrollDate().toString());
				// JSON에서는 char, Date, Timestamp를 사용할 수 없음
				// 문자열로 변환
				
				response.setCharacterEncoding("UTF-8");
				
				//response.getWriter().print(jsonMember.toJSONString());
				
				
				// Gson 사용---------------------------------------
				// 단순히 객체를 json으로 변경하여 응답할 때 
				//new Gson().toJson(member, response.getWriter());
						  // json으로 변환할 객체, 출력 스트림
				
				
				
				// 날짜 데이터 포맷 지정
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
				gson.toJson(member, response.getWriter());
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
