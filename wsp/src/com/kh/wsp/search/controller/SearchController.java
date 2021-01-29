package com.kh.wsp.search.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.wsp.board.model.vo.Attachment;
import com.kh.wsp.board.model.vo.Board;
import com.kh.wsp.board.model.vo.PageInfo;
import com.kh.wsp.search.model.service.SearchService;
import com.kh.wsp.search.model.service.SearchService2;

@WebServlet("/search.do")
public class SearchController extends HttpServlet {
   private static final long serialVersionUID = 1L;
   
   
   
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String searchKey = request.getParameter("sk");
      String searchValue = request.getParameter("sv");
      String cp = request.getParameter("cp");
      
      try {
         //SearchService service = new SearchService();
         SearchService2 service = new SearchService2();
         
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("searchKey", searchKey);
         map.put("searchValue", searchValue);
         map.put("currentPage", cp);
         
         // 페이징 처리를 위한 데이터를 계산하고 저장하는 객체 PageInfo 얻어오기
         PageInfo pInfo = service.getPageInfo(map);
         
         // 검색 게시글 목록 조회
         List<Board> bList = service.searchBoardList(map, pInfo);
         
         // 결과 확인
         /*System.out.println(pInfo);
         for(Board b : bList) {
            System.out.println(b);
         }*/
         
         // 검색 게시글 목록 조회 성공 시 썸네일 목록 조회 수행
         if(bList != null && !bList.isEmpty()) {
        	 List<Attachment> fList = service.searchThumbnailList(bList);
        	 
        	 if(!fList.isEmpty()) { // 조회된 썸네일 목록이 있다면
        		 request.setAttribute("fList", fList);
        	 }
         }
         
         // 조회된 내용과 PageInfo 객체를 request객체에 담아서 요청 위임
         String path = "/WEB-INF/views/board/boardList.jsp";
         
         request.setAttribute("bList", bList);
         request.setAttribute("pInfo", pInfo);
         
         RequestDispatcher view = request.getRequestDispatcher(path);
         view.forward(request, response);
         
         
      }catch (Exception e) {
         e.printStackTrace();
         String path = "/WEB-INF/views/common/errorPage.jsp";
         request.setAttribute("errorMsg", "검색 과정에서 오류 발생");
         RequestDispatcher view = request.getRequestDispatcher(path);
         view.forward(request, response);
      }
      
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}