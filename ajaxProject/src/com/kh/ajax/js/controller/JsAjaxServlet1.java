package com.kh.ajax.js.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/jsAjax1.do")
public class JsAjaxServlet1 extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //전달된 파라미터 변수에 저장
      String name = request.getParameter("name");
      String age = request.getParameter("age");
      
      // 파라미터 확인
      System.out.println(name + " / " + age);
      
      // 응답 문자 인코딩 지정
      response.setCharacterEncoding("UTF-8");
      
      // 응답 데이터 전송 스트림 연결
      PrintWriter out = response.getWriter();
      
      out.append("서버에서 비동기로 전송한 값 <br>");
      out.append("이름은 " + name + "이고, 나이는 " + age + "세 입니다.");
   }


   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}