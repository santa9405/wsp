package com.kh.wsp.notice.controller;

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
import javax.servlet.http.HttpSession;

import com.kh.wsp.member.model.vo.Member;
import com.kh.wsp.notice.model.service.NoticeService;
import com.kh.wsp.notice.model.vo.Notice;

// 모든 notice 관련 요청을 받아들임
@WebServlet("/notice/*")
public class NoticeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Front Controller 패턴
		// - 클라이언트의 요청을 한 곳으로 집중시켜 개발하는 패턴 
		// - 요청마다 Servlet을 생성하는 것이 아닌 하나의 Servlet에 작성하여
		//   관리가 용이해짐.
		
		// Controller 역할 : 요청에 맞는 Service 호출, 응답을 위한 View 선택
		
		String uri = request.getRequestURI(); // 전체 요청 주소
		// ex) /wsp/notice/list.do
		
		String contextPath = request.getContextPath();
		// ex) /wsp
		
		String command = uri.substring((contextPath+"/notice").length());
		// ex) /wsp/notice를 제거 == /list.do
		
//		System.out.println(uri);
//		System.out.println(contextPath);
//		System.out.println(command);
		
		// 컨트롤러 내에서 공용으로 사용할 변수 미리 선언
		String path = null; // forward 또는 redirect 경로를 저장할 변수
		RequestDispatcher view = null; // 요청 위임 객체
		
		String swalIcon = null; // sweet alert로 메세지 전달하는 용도
		String swalTitle = null;
		String swalText = null;
		
		String errorMsg = null; // 에러 메세지 전달용 변수
		
		try {
			NoticeService service = new NoticeService();
			
			// 공지사항 목록 조회 Controller--------------------------
			if(command.equals("/list.do")) {
				errorMsg = "공지사항 목록 조회 중 오류 발생.";
				
				// 비즈니스 로직 처리 후 결과 반환 받기
				List<Notice> list = service.selectList();
				
				// 요청을 위임할 jsp 경로 지정
				path = "/WEB-INF/views/notice/noticeList.jsp";
				
				// 요쳥 위임 시 전달할 값 세팅
				request.setAttribute("list", list);
				
				// 요청 위임 객체 생성 후 위임 진행
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			// 공지사항 상세 조회 Controller -------------------------
			else if(command.equals("/view.do")){
				errorMsg = "공지사항 상세 조회 과정에서 오류 발생";
				
				// 쿼리스트링으로 전달된 공지사항 번호를 int형으로 파싱하여 저장
				int noticeNo = Integer.parseInt(request.getParameter("no"));
				
				// 상세조회 비즈니스 로직 수행 결과 반환
				Notice notice = service.selectNotice(noticeNo);
				
				// 조회 결과에 따른 view 연결 처리
				if(notice != null) { // 조회 성공
					path = "/WEB-INF/views/notice/noticeView.jsp";
					request.setAttribute("notice", notice);
					view = request.getRequestDispatcher(path);
					view.forward(request, response);
					
				}else { // 조회 실패
					HttpSession session = request.getSession();
					session.setAttribute("swalIcon", "error");
					session.setAttribute("swalTitle", "공지사항 조회 실패");
					response.sendRedirect( request.getHeader("referer") );
				}
				
			}
				// 공지사항 작성 화면 전환 Controller----------------------------------------
				else if(command.equals("/insertForm.do")) {
					path = "/WEB-INF/views/notice/noticeInsert.jsp";
					view = request.getRequestDispatcher(path);
					view.forward(request, response);
			}
			
			// 공지사항 등록 Controller----------------------------------------
				else if(command.equals("/insert.do")) {
					errorMsg = "공지사항 등록 과정에서 오류 발생";
					
					// 파라미터로 전달된 값 저장
					String noticeTitle = request.getParameter("noticeTitle");
					String noticeContent = request.getParameter("noticeContent");
					
					// 세션에서 회원 번호 얻어오기
					HttpSession session = request.getSession();
					int noticeWriter = ((Member)session.getAttribute("loginMember")).getMemberNo();
					
					// Map을 이용하여 데이터 전달(VO 대신 사용)
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("noticeTitle", noticeTitle);
					map.put("noticeContent", noticeContent);
					map.put("noticeWriter", noticeWriter);
					
					// 공지사항 등록 비즈니스 로직 수행 후 결과 반환
					int result = service.insertNotice(map);
					
					if(result > 0) { // 삽입 성공 시
						// result에는 삽입된 공지사항 번호가 저장되어 있음.
						path = "view.do?no=" + result; // 쿼리스트링을 이용하여 공지사항 상세 조회 요청
						swalIcon = "success";
						swalTitle = "공지사항이 등록되었습니다.";
					}else {
						path = "list.do";
						swalIcon = "error";
						swalTitle = "공지사항 등록 실패";
					}
					
					session.setAttribute("swalIcon", swalIcon);
					session.setAttribute("swalTitle", swalTitle);
					
					response.sendRedirect(path);
					// illegalStateException 리다이렉트와 포워드를 동시에 할수 없다.
				}
			
				// 공지사항 수정 화면 출력용 Controller
				else if(command.equals("/updateForm.do")) {
					
					errorMsg = "공지사항 수정 화면 전환 과정에서 오류 발생";
					
					int noticeNo = Integer.parseInt( request.getParameter("no") );
					
					Notice notice = service.updateView(noticeNo);
					
					if(notice != null) {
						path = "/WEB-INF/views/notice/noticeUpdate.jsp";
						request.setAttribute("notice", notice);
						view = request.getRequestDispatcher(path);
						view.forward(request, response);
					}else {
						request.getSession().setAttribute("swalIcon", "error");
						request.getSession().setAttribute("swalText", "공지사항 수정을 위한 조회 실패");
						
						response.sendRedirect("view.do?no=" + noticeNo);
					}
				}
			
				// 공지사항 수정 Controller
				else if(command.equals("/update.do")) {
					
					errorMsg = "공지사항 수정 과정에서 오류 발생";
					
					// 파라미터 얻어오기
					int noticeNo = Integer.parseInt( request.getParameter("no"));
					String noticeTitle = request.getParameter("noticeTitle");
					String noticeContent = request.getParameter("noticeContent");
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("noticeNo", noticeNo);
					map.put("noticeTitle", noticeTitle);
					map.put("noticeContent", noticeContent);
					
					// 비즈니스 로직 수행
					int result = service.updateNotice(map);
					
					if(result > 0) {
						// 로직 수행 성공 시 "공지사항이 수정되었습니다." swal 출력
						request.getSession().setAttribute("swalIcon", "success");
						request.getSession().setAttribute("swalTitle", "공지사항이 수정되었습니다.");
					}else {
						// 로직 수행 실패 시 "공지사항 수정 실패" swal 출력
						request.getSession().setAttribute("swalIcon", "error");
						request.getSession().setAttribute("swalTitle", "공지사항 수정 실패");
					}
					
					// 수정된 공지사항 상세조회로 redirect
					response.sendRedirect("view.do?no=" + noticeNo);
				}
			
				// 공지사항 삭제 Controller-------------------------------------------------------
				else if(command.equals("/delete.do")) {
					
					errorMsg = "공지사항 삭제 과정에서 오류 발생";
					
					int noticeNo = Integer.parseInt(request.getParameter("no"));
					
					// 비즈니스 로직 수행 후 결과 반환
					int result = service.updateNoticeFl(noticeNo);
					
					if(result > 0) {
						swalIcon = "success";
						swalTitle = "공지사항이 삭제되었습니다.";
						path = "list.do";
					}else {
						swalIcon = "error";
						swalTitle = "공지사항 삭제 실패";
						path = request.getHeader("referer");
					}
					
					request.getSession().setAttribute("swalIcon", swalIcon);
					request.getSession().setAttribute("swalTitle", swalTitle);
					
					response.sendRedirect(path);
					
				}
				
			
		}catch (Exception e) {
			e.printStackTrace();
			path = "/WEB-INF/views/common/errorPage.jsp";
			request.setAttribute("errorMsg", errorMsg);
			view = request.getRequestDispatcher(path);
			view.forward(request, response);
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
