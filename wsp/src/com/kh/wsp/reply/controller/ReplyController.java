package com.kh.wsp.reply.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kh.wsp.reply.model.service.ReplyService;
import com.kh.wsp.reply.model.service.ReplyService2;
import com.kh.wsp.reply.model.vo.Reply;

@WebServlet("/reply/*")
public class ReplyController extends HttpServlet {
   private static final long serialVersionUID = 1L;
       
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String uri = request.getRequestURI();
      String contextPath = request.getContextPath();
      String command = uri.substring((contextPath+"/reply").length());
      
      try {
         ReplyService2 service = new ReplyService2();
         
         // 댓글 목록 조회 Controller *************************************
         if(command.equals("/selectList.do")) {
            int parentBoardNo = Integer.parseInt(request.getParameter("parentBoardNo"));
            
            List<Reply> rList = service.selectList(parentBoardNo);
            
            Gson gson = new GsonBuilder().setDateFormat("yyyy년 MM월 dd일 HH:mm").create();
            gson.toJson(rList, response.getWriter()); // rList가 자동으로 gson 형태로 변환된다.
            
         }
         
         else if(command.equals("/insertReply.do")) {
        	 
        	// 오라클에서 숫자로 이루어진 문자열은 자동으로 숫자로 변환되는 특징을 사용할 예정
        	String replyWriter = request.getParameter("replyWriter");
			int parentBoardNo = Integer.parseInt(request.getParameter("parentBoardNo"));
			String replyContent = request.getParameter("replyContent");
			
//			Reply reply = new Reply();
//			reply.setMemberId(replyWriter); // 회원번호 저장됨
//			reply.setReplyContent(replyContent);
//			reply.setParentBoardNo(parentBoardNo);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("replyWriter", replyWriter);
			map.put("replyContent", replyContent);
			map.put("parentBoardNo", parentBoardNo);
			
			int result = service.insertReply(map);
			
			// 결과 성공 실패 여부와 관계 없이 바로 응답페이지로 돌려보냄
			response.getWriter().print(result);
         }
         
         
         // 댓글 수정 Controller--------------------------------------------------------------
         else if(command.equals("/updateReply.do")) {
        	 
        	 // 파라미터(댓글 번호, 댓글 내용) 얻어오기
        	 int replyNo = Integer.parseInt(request.getParameter("replyNo"));
        	 String replyContent = request.getParameter("replyContent");
        	 
//        	 Reply reply = new Reply();
//        	 reply.setReplyNo(replyNo);
//        	 reply.setReplyContent(replyContent);
        	 
        	 Map<String, Object> map = new HashMap<String, Object>();
        	 map.put("replyNo", replyNo);
        	 map.put("replyContent", replyContent);
        	 
        	 int result = service.updateReply(map);
        	 
        	 response.getWriter().print(result);
         }
         
         
         // 댓글 상태 변경 Controller-------------------------------------------------
         else if(command.equals("/deleteReply.do")) {
        	 
        	 int replyNo = Integer.parseInt(request.getParameter("replyNo"));
        	 
        	 int result = service.updateReplyStatus(replyNo);
        	 
        	 response.getWriter().print(result);
        	 
         }
         
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}