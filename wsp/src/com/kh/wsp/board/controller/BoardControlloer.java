package com.kh.wsp.board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.wsp.board.model.service.BoardService;
import com.kh.wsp.board.model.service.BoardService2;
import com.kh.wsp.board.model.vo.Attachment;
import com.kh.wsp.board.model.vo.Board;
import com.kh.wsp.board.model.vo.PageInfo;
import com.kh.wsp.common.MyFileRenamePolicy;
import com.kh.wsp.member.model.vo.Member;
import com.oreilly.servlet.MultipartRequest;

@WebServlet("/board/*")
public class BoardControlloer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri = request.getRequestURI();          // /wsp/board/list.do 요청이 들어오는 주소
		String contextPath = request.getContextPath(); // /wsp 프로젝트 주소
		String command = uri.substring( (contextPath + "/board").length() ); // /wsp/board를 잘라낸 주소 == /list.do
		
		//System.out.println(command);
		
		String path = null;
		RequestDispatcher view = null;
		
		String swalIcon = null;
		String swalTitle = null;
		String swalText = null;
		
		String errorMsg = null;
		
		try {
//			BoardService service = new BoardService();
			
			// 마이바티스가 적용된 Service
			BoardService2 service = new BoardService2();
			
			// 현재 페이지를 얻어옴
			String cp = request.getParameter("cp"); // 현재 null인 상태
			
			// currentPage 사용 이유
			// 1. 페이징 처리 시 계산에 필요한 값이기 때문
			// 2. 상세조회, 북마크 등의 UI/UX 향상을 위해 사용
			
			// 게시글 목록 조회 Controller------------------------------------
			if(command.equals("/list.do")) {
				errorMsg = "게시판 목록 조회 과정에서 오류 발생";
				
				// 1. 페이징 처리를 위한 값 계산 Service 호출
				PageInfo pInfo = service.getPageInfo(cp);
				//System.out.println(pInfo);
				
				// 2. 게시글 목록 조회 비즈니스 로직 수행
				List<Board> bList = service.selectBoardList(pInfo);
				// pInfo에 있는 currentPage, limit를 사용해야지만
				// 현재 페이지에 맞는 게시글 목록만 조회할 수 있음.
				
//				for(Board b : bList) {
//					System.out.println(b);
//				}
				
				// 3. 게시글 목록이 조회 되었을 때 해당 게시글 목록 요소에 작성된 썸네일 이미지 목록 얻어오기
				if(bList != null) {
					// 게시글 목록 중 글번호만 별도 리스트에 저장
					// -> String.join()을 이용해 한 줄로 만들기 위함
					// ex) 1,2,3,4,5
					// 단, String.join() 사용 시 제네릭이 적용된 List는 사용 못함
					List boardNoList = new ArrayList();
					
					for(Board b : bList) { 
						boardNoList.add(b.getBoardNo()+"");
					}
					String boardNoStr = String.join(", ", boardNoList);
					
					// 썸네일 이미지 목록 조회 서비스 호출
					//List<Attachment> fList = service.selectThumbnailList(pInfo);
					List<Attachment> fList = service.selectThumbnailList(pInfo);
					
					// 썸네일 이미지 목록이 비어있지 않은 경우
					if(!fList.isEmpty()) {
						request.setAttribute("fList", fList);
					}
				}
				
				path = "/WEB-INF/views/board/boardList.jsp";
				
				request.setAttribute("bList", bList);
				request.setAttribute("pInfo", pInfo);
				
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			
			// 게시글 상세 조회 Controller-------------------------------------------------
			else if(command.equals("/view.do")) {
				errorMsg = "게시글 상세 조회 과정에서 오류 발생";
				int boardNo = Integer.parseInt(request.getParameter("no"));
				
				// 상세조회 비즈니스 로직 수행 후 결과 반환 받기
				Board board = service.selectBoard(boardNo);
				
				if(board != null) { // 상세 조회 성공
					
					// 해당 게시글에 포함된 이미지 파일 목록 조회 서비스 호출
					List<Attachment> fList = service.selectBoardFiles(boardNo);
					
					if(!fList.isEmpty()) { // 해당 게시글 이미지 정보가 DB에 있을 경우
						request.setAttribute("fList", fList);
					}
					
					path = "/WEB-INF/views/board/boardView.jsp";
					request.setAttribute("board", board);
					view = request.getRequestDispatcher(path);
					view.forward(request, response);
					
				}else { // 상세 조회 실패
					
					request.getSession().setAttribute("swalIcon", "error");
					request.getSession().setAttribute("swalTitle", "게시글 상세 조회 실패");
					response.sendRedirect("list.do?cp=1");
				}
			}
			
			// 게시글 작성 화면 전환 Controller----------------------------------------------
			else if(command.equals("/insertForm.do")) {
				
				path = "/WEB-INF/views/board/boardInsert.jsp";
				view = request.getRequestDispatcher(path);
				view.forward(request, response);
			}
			
			// 게시글 등록 Controller (+ 파일 업로드)----------------------------------------
			else if(command.equals("/insert.do")) {
				
				errorMsg = "게시글 등록 과정에서 오류 발생";
				
				// 제출(요청)되는 form 태그의 encType이 multipart/form-data 형식이면
				// 기존에 사용하던 request 객체로 파라미터를 얻어올 수 없다.
				// --> cos.jar에서 제공하는 MultipartRequest 객체를 사용하면
				//     파라미터를 얻어올 수 있다.
				
				// 1. MultipartRequest 객체 생성하기
				// 1-1. 전송 파일 용량 지정(byte 단위)
				int maxSize = 20 * 1024 * 1024; // 20MB == 20 * 1024KB == 20 * 1024 * 1024Byte
				
				// 1-2. 서버에 업로드된 파일을 저장할 경로 지정
				String root = request.getSession().getServletContext().getRealPath("/"); // 실제주소 WebContent가 반환됨
				String filePath = root + "resources/uploadImages/";
				
				//System.out.println("filePath : " + filePath);
				
				// 1-3. 파일명 변환을 위한 클래스 작성하기
				// cos.jar에서 중복되는 파일이 업로드 되었을 때
				// 파일명을 바꿔주는 DefaultFileRenamePolicy 클래스를 제공해 주지만
				// ex) a.jpg, a(1).jpg, a(2).jpg
				// 파일명에 업로드된 시간을 표기할 수 있도록 변경하는 별도의 클래스를 작성할 예정.
				
				// 1-4. MultipartRequest 객체 생성
				// -> 객체 생성과 동시에 파라미터로 넘어온 내용 중 파일이 서버에 바로 저장됨.
				MultipartRequest multiRequest 
					= new MultipartRequest(request, filePath, maxSize, "UTF-8", new MyFileRenamePolicy());
															// 파일을 제외한건 UTF-8로 인코딩 하겠다
				
				// 2. 생성한 MultipartRequest 객체에서 파일 정보만을 얻어와
				//    별도의 List에 모두 저장하기
				
				// 2-1. 파일 정보를 모두 저장할 List 객체 생성
				List<Attachment> fList = new ArrayList<Attachment>();
				
				// 2-2. MultipartRequest에서 업로드된 파일의 name 속성값 반환 받기
				Enumeration<String> files = multiRequest.getFileNames();
				//System.out.println(files);
				// Iterator : 컬렉션 요소 반복 접근자
				// Enumeration : Iterator의 과거 버전
				
				// 2-3. 얻어온 Enumeration 객체에 요소를 하나씩 반복 접근하여
				//		업로드된 파일 정보를 Attachment 객체에 저장한 후
				//		fList에 추가하기
				while(files.hasMoreElements()) { // 다음 요소가 있다면
					
					// 현재 접근한  요소 값 반환
					String name = files.nextElement(); // img0
					System.out.println("name : " + name);
//					System.out.println("원본 파일명 : " 
//										+ multiRequest.getOriginalFileName(name));
//					System.out.println("변경된 파일명 : " + multiRequest.getFilesystemName(name));
					
					// 제출받은 file태그 요소 중 업로드 된 파일이 있을 경우
					if( multiRequest.getFilesystemName(name) != null) {
						
						// Attachment 객체에 파일 정보 저장
						Attachment temp = new Attachment();
						
						temp.setFileName(multiRequest.getFilesystemName(name));
						temp.setFilePath(filePath);
						
						// name 속성에 따라 fileLevel 지정
						int fileLevel = 0;
						switch(name) {
						case "img0" : fileLevel = 0; break;
						case "img1" : fileLevel = 1; break;
						case "img2" : fileLevel = 2; break;
						case "img3" : fileLevel = 3; break;
						}
						
						temp.setFileLevel(fileLevel);
						
						// fList에 추가
						fList.add(temp);
						System.out.println(fList);
					}
				} // end wile
				
				// 3. 파일정보를 제외한 게시글 정보를 얻어와 저장하기
				String boardTitle = multiRequest.getParameter("boardTitle");
				String boardContent = multiRequest.getParameter("boardContent");
				int categoryCode = Integer.parseInt(multiRequest.getParameter("categoryCode"));
				
				System.out.println(boardTitle);
				
				// 세션에서 로그인한 회원의 번호를 얻어오기
				Member loginMember = (Member)request.getSession().getAttribute("loginMember");
				int boardWriter = loginMember.getMemberNo();
				
				// fList, boardTitle, boardContent, categoryCode, boardWriter
				
				// Map 객체를 생성하여 얻어온 정보들을 모두 저장
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fList", fList);
				map.put("boardTitle", boardTitle);
				map.put("boardContent", boardContent);
				map.put("categoryCode", categoryCode);
				map.put("boardWriter", boardWriter);
				
				// 4. 게시글 등록 비즈니스 로직 수행 후 결과 반환 받기
				int result = service.insertBoard(map);
				
				if(result > 0) { // DB 삽입 성공 시 result에는 삽입한 글 번호가 저장되어있다!
					swalIcon = "success";
					swalTitle = "게시글 등록 성공";
					path = "view.do?cp=1&no=" + result;
				}else {
					swalIcon = "error";
					swalTitle = "게시글 등록 실패";
					path = "list.do"; // 게시글 목록
				}
				
				request.getSession().setAttribute("swalIcon", swalIcon);
				request.getSession().setAttribute("swalTitle", swalTitle);
				response.sendRedirect(path);
			}
			
			// 게시글 수정 화면 전환 Controller------------------------------------
			else if(command.equals("/updateForm.do")) {
				
				errorMsg = "게시글 수정 화면 전환 과정에서 오류 발생";
				
				// 수정 화면이 미리 이전 내용이 작성돼있을 수 있도록
				// 글 번호를 이용하여 이전 내용을 조회해옴
				
				int boardNo = Integer.parseInt(request.getParameter("no"));
				
				Board board = service.updateView(boardNo);
				
				// 업데이트 화면 출력용 게시글 조회가 성공한 경우
				if(board != null) {
					// 해당 게시글에 작성된 이미지(파일) 목록 정보 조회
					List<Attachment> fList = service.selectBoardFiles(boardNo);
					
					if(!fList.isEmpty()) {
						request.setAttribute("fList", fList);
					}
					
					request.setAttribute("board", board);
					path = "/WEB-INF/views/board/boardUpdate.jsp";
					view = request.getRequestDispatcher(path);
					view.forward(request, response);
					
				}else {
					request.getSession().setAttribute("swalIcon", "error");
					request.getSession().setAttribute("swalTitle", "게시글 수정 화면 전환 실패");
					response.sendRedirect(request.getHeader("referer"));
					// 상세 조회 -> 수정 화면 -> 상세 조회
				}
			}
			
	         // 게시글 수정 Controller ********************************
            else if(command.equals("/update.do")) {
               
               errorMsg = "게시글 수정 과정에서 오류 발생";
               
               // 1. MultipartRequest 객체 생성에 필요한 값 설정
               int maxSize = 20 * 1024 * 1024; // 최대 크기 20MB
               String root = request.getServletContext().getRealPath("/");
               String filePath = root + "resources/uploadImages/";
               
               // 2. MultipartRequest 객체 생성
               // -> 생성과 동시에 전달받은 파일이 서버에 저장됨
               MultipartRequest mRequest = new MultipartRequest(request, filePath, maxSize, "UTF-8", new MyFileRenamePolicy());
               
               // 3. 파일 정보를 제외한 파라미터 얻어오기
               String boardTitle = mRequest.getParameter("boardTitle");
               String boardContent = mRequest.getParameter("boardContent");
               int categoryCode = Integer.parseInt(mRequest.getParameter("categoryCode"));
               int boardNo = Integer.parseInt(mRequest.getParameter("no"));
               
               // 4. 전달받은 파일 정보를 List에 저장
               List<Attachment> fList = new ArrayList<Attachment>();
               Enumeration<String> files = mRequest.getFileNames();
               // input type="file"인 모든 요소의 name 속성 값을 반환받아 files에 저장
               
               while(files.hasMoreElements()) {
                  // 현재 접근중인 name속성값을 변수에 저장
                  String name = files.nextElement();
                 
                  // 현재 name 속성이 일치하는 요소로 업로드된 파일이 있다면
                  if(mRequest.getFilesystemName(name) != null) {
                     
                     Attachment temp = new Attachment();
                     
                     // 변경된 파일 이름 temp에 저장
                     temp.setFileName(mRequest.getFilesystemName(name));
                     
                     // 지정한 파일 경로 tmep에 저장
                     temp.setFilePath(filePath);
                     
                     // 해당 게시글 번호 temp에 저장
                     temp.setParentBoardNo(boardNo);
                     
                     // 파일 레벨 temp에 저장
                     switch(name) {
                     case "img0" : temp.setFileLevel(0); break;
                     case "img1" : temp.setFileLevel(1); break;
                     case "img2" : temp.setFileLevel(2); break;
                     case "img3" : temp.setFileLevel(3); break;
                     }
                     
                     // temp를 fList에 추가
                     fList.add(temp);
                    // 이미지를 변경한 부분들만 fList에 추가가 된다.
                  }
               }
               
               // 5. Session에서 로그인한 회원의 번호를 얻어와 저장
               int boardWriter = ((Member)request.getSession().getAttribute("loginMember")).getMemberNo();
               
               // 6. 준비된 값들을 하나의 Map에 저장
               Map<String, Object> map = new HashMap<String, Object>();
               map.put("boardTitle", boardTitle);
               map.put("boardContent", boardContent);
               map.put("categoryCode",categoryCode);
               map.put("boardNo",boardNo);
               map.put("fList",fList);
               map.put("boardWriter",boardWriter);
               
               // 7. 준비된 값을 매개변수로 하여 게시글 수정 Service 호출
               int result = service.updateBoard(map);
               
               // 8. result 값에 따라 View 연결 처리
               path = "view.do?cp=" + cp + "&no=" + boardNo;
               String sk = mRequest.getParameter("sk");
               String sv = mRequest.getParameter("sv");
               
               // 전달된 sk,sv가 존재 할 때 (검색을 통한 접근일 때)
               if(sk != null && sv != null ) {
                  path += "&sk="+sk + "&sv=" + sv;
               }
               
               if (result>0) {
                  swalIcon = "success";
                  swalTitle = "게시글 수정 성공";
                                 
               }else {
                  swalIcon = "error";
                  swalTitle = "게시글 수정 실패";
               }
               request.getSession().setAttribute("swalIcon", swalIcon);
               request.getSession().setAttribute("swalTitle", swalTitle);
               
               response.sendRedirect(path);
            }
			
			// 게시글 삭제 Controller-----------------------------------------
            else if(command.equals("/delete.do")) {
            	
            	// 게시글 번호 얻어오기
            	int no = Integer.parseInt(request.getParameter("no"));
            	
            	// 게시글 삭제(게시글 상태 -> N) 비즈니스 로직 수행 후 결과 반환
            	int result = service.updateBoardF1(no);
            	
            	if(result > 0) {
            		// 비즈니스 로직 결과에 따라
            		// "게시글 삭제 성공" / "게시글 삭제 실패" 메세지를 전달
            		// 삭제 성공 시 : 게시글 목록으로 redirect
            		request.getSession().setAttribute("swalIcon", "success");
            		request.getSession().setAttribute("swalTitle", "게시글 삭제 성공");
            		response.sendRedirect("list.do?cp=1");
            	}else {
            		// 삭제 실패 시 : 상세 시도한 게시글 상세조회 페이지로 redirect
            		request.getSession().setAttribute("swalIcon", "error");
            		request.getSession().setAttribute("swalTitle", "게시글 삭제 실패");
            		response.sendRedirect( request.getHeader("referer") );
            	}
            	
            	
            }
			
			
			
			
			
			
			
			
			
			
	         
	      } catch (Exception e) {
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
