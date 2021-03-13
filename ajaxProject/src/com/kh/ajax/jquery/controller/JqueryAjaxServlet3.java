package com.kh.ajax.jquery.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.kh.ajax.jquery.model.vo.User;

@WebServlet("/jqTest3.do")
public class JqueryAjaxServlet3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// 샘플데이터 생성
		List<User> list = new ArrayList<User>();
		list.add(new User(1,"박철수", 30, '남'));
		list.add(new User(2,"김영희", 26, '여'));
		list.add(new User(3,"오영심", 32, '여'));
		list.add(new User(4,"이민기", 28, '남'));
		list.add(new User(5,"홍길동", 33, '남'));

		// 파라미터 전달 받기
		int input = Integer.parseInt(request.getParameter("input"));
		
		// JSON 라이브러리를 이용한 JSON 객체 생성하기
		// https://code.google.com/archive/p/json-simple/downloads
		
		// JSONObject : 데이터를 K:V 형태로 저장하고 출력시  JSON으로 내보내 주는 객체
		JSONObject jsonUser = null;
		
		// input과 회원 번호가 일치하는 회원 찾기
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).getNo() == input) {
				// list.get(i) --> JSON 형태로 변경
				
				jsonUser = new JSONObject(); // JSONObject 객체 생성
				
				jsonUser.put("no", list.get(i).getNo());
				jsonUser.put("name", list.get(i).getName());
				jsonUser.put("age", list.get(i).getAge());
				jsonUser.put("gender", list.get(i).getGender()+"");
				
				// Javascript에는 char 자료형이 없음 -> String으로 변환
				
				break; // 검색 중지
			}
		}

		// JSONObject에 저장된 내용 확인
		System.out.println(jsonUser.toJSONString());
		
		response.setCharacterEncoding("UTF-8"); // 문자 인코딩
		
		// 응답 데이터가 JSON 형태임을 인식 시키는 방법1
		// MIME TYPE 지정
		//response.setContentType("application/json; charset=UTF-8");
		
		// 응답용 스트림 연결 및 출력
		response.getWriter().print(jsonUser.toJSONString());
	
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
