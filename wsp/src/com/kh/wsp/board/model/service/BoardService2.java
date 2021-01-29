package com.kh.wsp.board.model.service;

import static com.kh.wsp.common.MybatisTemplate.getSqlSession;
import static com.kh.wsp.common.JDBCTemplate.*;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.kh.wsp.board.model.dao.BoardDAO;
import com.kh.wsp.board.model.dao.BoardDAO2;
import com.kh.wsp.board.model.exception.FileInsertFailedException;
import com.kh.wsp.board.model.vo.Attachment;
import com.kh.wsp.board.model.vo.Board;
import com.kh.wsp.board.model.vo.PageInfo;

public class BoardService2 {

	private BoardDAO2 dao = new BoardDAO2();

	/**
	 * 페이징 처리를 위한 값 계산 Service
	 * 
	 * @param cp
	 * @return pInfo
	 * @throws Exception
	 */
	public PageInfo getPageInfo(String cp) throws Exception {
		
		// 마이바티스에서는 Connection대신 SqlSession을 얻어와야 함.
		SqlSession session = getSqlSession();
		
		System.out.println("sqlSession : " + session);

		// cp가 null일 경우
		int currentPage = cp == null ? 1 : Integer.parseInt(cp);

		// DB에서 전체 게시글 수를 조회하여 반환 받기
		int listCount = dao.getListCount(session);
		System.out.println("listCount : " + listCount);
		
		session.close();

		// 얻어온 현재 페이지와, DB에서 조회한 전체 게시글 수를 이용하여
		// PageInfo 객체를 생성함
		return new PageInfo(currentPage, listCount);
	}

	/**
	 * 게시글 목록 조회 Service
	 * 
	 * @param pInfo
	 * @return bList
	 * @throws Exception
	 */
	public List<Board> selectBoardList(PageInfo pInfo) throws Exception {
		SqlSession session = getSqlSession();
		
		List<Board> bList = dao.selectBoardList(session, pInfo);

		session.close();

		return bList;
	}

	/**
	 * 게시글 상세 조회 Service
	 * 
	 * @param boardNo
	 * @return board
	 * @throws Exception
	 */
	public Board selectBoard(int boardNo) throws Exception {
		SqlSession session = getSqlSession();
		
		//Connection conn = getConnection();
		Board board = dao.selectBoard(session, boardNo);

		if (board != null) { // DB에서 조회 성공 시

			// 조회수 증가
			int result = dao.increaseReadCount(session, boardNo);

			if (result > 0) {
				session.commit();

				// 반환되는 Board 데이터에는 조회수가 증가되어 있지 않기 때문에
				// 조회수를 1 증가 시켜줌
				//board.setReadCount(board.getReadCount() + 1);

			} else {
				session.rollback();
			}

		}
		session.close();
		
		return board;
	}

	/**
	 * 게시글 등록 Service (게시글 + 파일)
	 * 
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(Map<String, Object> map) throws Exception {
		SqlSession session = getSqlSession();
		
		//Connection conn = getConnection();

		int result = 0;

		// 1. 게시글 번호 얻어오기
		int boardNo = dao.selectNextNo(session);

		if (boardNo > 0) {
			// 얻어온 게시글 번호를 map에 추가(게시글, 파일정보 삽입 DAO에서 사용하기 위해)
			map.put("boardNo", boardNo);

			// 2. 글 제목/내용 크로스 사이트 스크립팅 방지 처리
			String boardTitle = (String) map.get("boardTitle");
			String boardContent = (String) map.get("boardContent");

			boardTitle = replaceParameter(boardTitle);
			boardContent = replaceParameter(boardContent);

			// 3. 글 내용 개행문자 \r\n -> <br> 변경 처리
			boardContent = boardContent.replaceAll("\r\n", "<br>");

			// 처리된 내용을 다시 map에 추가
			map.put("boardTitle", boardTitle);
			map.put("boardContent", boardContent);

			try {
				// 4. 게시글 부분(제목, 내용, 카테고리)만 BOARD 테이블에 삽입하는 DAO 호출
				result = dao.insertBoard(session, map);

				// 5. 파일 정보 부분만 ATTACHMENT 테이블에 삽입하는 DAO 호출
				List<Attachment> fList = (List<Attachment>) map.get("fList");

				// 게시글 부분 삽입 성공 && 파일 정보가 있을 경우
				if (result > 0 && !fList.isEmpty()) {
					result = 0; // result 재활용을 위해 0으로 초기화
					
					// 게시글 번호를 Attachment에 세팅
					for(Attachment at : fList) {
						at.setParentBoardNo(boardNo);
					}
					
					result = dao.insertAttachmentList(session, fList);

					if (result == 0) { // 파일 정보 삽입 실패
						// 강제로 예외 발생
						throw new FileInsertFailedException("파일 정보 삽입 실패");
					}
				}

			} catch (Exception e) {
				// 4,5번에 대한 추가 작업
				// 게시글 또는 파일 정보 삽입 중 에러 발생 시
				// 서버에 저장된 파일을 삭제하는 작업이 필요함.

				List<Attachment> fList = (List<Attachment>) map.get("fList");

				if (!fList.isEmpty()) {

					for (Attachment at : fList) {

						String filePath = at.getFilePath();
						String fileName = at.getFileName();

						File deleteFile = new File(filePath + fileName);

						if (deleteFile.exists()) {
							// 해당 경로에 해당 파일이 존재하면
							deleteFile.delete(); // 해당 파일 삭제
						}
					}
				}

				// 에러페이지가 보여질 수 있도록 catch한 Exception을 Controller로 던져줌
				throw e;

			} // end catch

			// 6. 트랜잭션 처리
			if (result > 0) {
				session.commit();

				// 삽입 성공 시 상세 조회 화면으로 이동해야 하기 때문에
				// 글 번호를 반환할 수 있도록 result에 boardNo를 대입
				result = boardNo;

			} else {
				session.rollback();
			}

		}

		// 7. 커넥션 반환
		session.close();

		// 8. 결과 반환
		return result;
	}

	// 크로스 사이트 스크립팅 방지 메소드
	private String replaceParameter(String param) {
		String result = param;
		if (param != null) {
			result = result.replaceAll("&", "&amp;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("\"", "&quot;");
		}

		return result;
	}

	/**
	 * 게시글에 포함된 이미지 목록 조회 Service
	 * 
	 * @param boardNo
	 * @return fList
	 * @throws Exception
	 */
	public List<Attachment> selectBoardFiles(int boardNo) throws Exception {
		SqlSession session = getSqlSession();
		
		//Connection conn = getConnection();

		List<Attachment> fList = dao.selectBoardFiles(session, boardNo);

		session.close();

		return fList;
	}

	/**
	 * 썸네일 목록 조회 Service
	 * 
	 * @param pInfo
	 * @return fList
	 * @throws Exception
	 */
	public List<Attachment> selectThumbnailList(PageInfo pInfo) throws Exception {
		Connection conn = getConnection();

		List<Attachment> fList = dao.selectThumbnailList(conn, pInfo);

		close(conn);

		return fList;
	}

	/**
	 * 게시글 수정 화면 출력용 Service
	 * 
	 * @param boardNo
	 * @return board
	 * @throws Exception
	 */
	public Board updateView(int boardNo) throws Exception {

		Connection conn = getConnection();

		// 이전에 만들어 놓은 상세조회 DAO 호출
		Board board = null; //dao.selectBoard(conn, boardNo);

		// textarea 출력을 위한 개행문자 변경
		board.setBoardContent(board.getBoardContent().replaceAll("<br>", "\r\n"));

		close(conn);

		return board;
	}

	 /** 게시글 수정 Service
     * @param map
     * @return result
     * @throws Exception
     */
    public int updateBoard(Map<String, Object> map) throws Exception {
       Connection conn = getConnection();

       int result = 0; // Service 수정 결과 저장 변수

       List<Attachment> deleteFiles = null; // 삭제할 파일 정보 저장 변수 선언

       // 1. 크로스 사이트 스크립팅 방지 처리

       String boardTitle = (String) map.get("boardTitle");
       String boardContent = (String) map.get("boardContent");

       boardTitle = replaceParameter(boardTitle);
       boardContent = replaceParameter(boardContent);

       // 2. 글 내용 개행문자 \r\n -> <br>
       boardContent.replaceAll("\r\n", "<br>");

       // 처리된 내용을 다시 map에 추가
       map.put("boardTitle", boardTitle);
       map.put("boardContent", boardContent);
       
       try {
          // 3. 게시글 부분 수정 DAO 호출
          result = dao.updateBoard(conn, map);
          
          // 4. 게시글 수정이 성공하고 fList가 비어있지 않으면
          //      파일 정보 수정 DAO를 호출함
          
          // 수정 화면에서 새로운 이미지가 업로드된 파일 정보만을 담고 있는 List
          List<Attachment> newFileList = (List<Attachment>)map.get("fList");
          
          
          if(result > 0 && !newFileList.isEmpty()) {
             
             // DB에서 해당 게시글의 수정전 파일 목록 조회
             List<Attachment> oldFileList = null; // dao.selectBoardFiles(conn, (int)map.get("boardNo"));
             
             // newFileList -> 수정된 썸네일(lv.0)
             
             // oldFileList -> 썸네일(lv.0), 이미지 (lv.1), 이미지2(lv.2)
             
             // 기존 썸네일(lv.0) -> 수정된 썸네일(lv.0)로 변경됨
             // -> DB에서 기존 썸네일의 데이터를 수정된 썸네일로 변경
             //   --> DB에서 기존 썸네일 정보가 사라짐
             
             result = 0; // result 재활용
             deleteFiles = new ArrayList<Attachment>(); // 삭제될 파일 정보 저장 List
             
             // 새로운 이미지 정보 반복 접근
             for(Attachment newFile : newFileList) {
                
                // flag가 false인 경우 : 새 이미지와 기존 이미지의 파일 레벨이 중복되는 경우 -> update
                // flag가 true인 경우 : 새 이미지와 기존 이미지의 파일 레벨이 중복되지 않는 경우 -> insert
                boolean flag = true;
                
                // 기존 이미지 정보 반복 접근
                for(Attachment oldFile : oldFileList) {
                   
                   // 새로운 이미지와 기존 이미지의 파일 레벨이 동일한 파일이 있다면
                   if(newFile.getFileLevel() == oldFile.getFileLevel()) {
                      
                      // 기존 파일을 삭제 List에 추가
                      deleteFiles.add(oldFile);
                      
                      // 새 이미지 정보에 이전 파일 번호를 추가 -> 파일 번호를 이용한 수정 진행
                      newFile.setFileNo(oldFile.getFileNo());
                      
                      flag = false;
                      
                      break;
                   }
                }
                
                // flag 값에 따라 파일 정보 insert 또는 update수행
                if(flag) {
                   result = dao.insertAttachment(conn, newFile);
                }else {
                   result = dao.updateAttachment(conn, newFile);
                }
                
                // 파일 정보 삽입 또는 수정 중 실패 시
                if(result == 0) {
                   // 강제로 사용자 정의 예외 발생
                   throw new FileInsertFailedException("파일 정보 삽입 또는 수정 실패");
                }
             }
          }
          
          
       } catch (Exception e) {
          // 게시글 수정 중 실패 또는 오류 발생 시
          // 서버에 미리 저장되어 있던 이미지 파일 삭제
          List<Attachment> fList = (List<Attachment>)map.get("fList");
          
          if(!fList.isEmpty()) {
             for(Attachment at : fList) {
                String filePath = at.getFilePath();
                String fileName = at.getFileName();
                
                File deleteFile = new File(filePath + fileName);
                
                if(deleteFile.exists()) {
                   // 해당 경로에 해당 파일이 존재하면
                   deleteFile.delete(); // 해당 파일 삭제
                }
             }
          }
          
          // 에러페이지가 보여질 수 있도록 catch한 Exception을 Controller로 던져줌
          throw e; 
       }
       
       // 5. 트랜잭션 처리 및 삭제 목록에 있는 파일 삭제
       if(result > 0) {
          commit(conn);
          
          // DB 정보와 맞지 않는 파일(deleteFiles) 삭제 진행
          if(deleteFiles != null) {
             
             for(Attachment at : deleteFiles) {
                String filePath = at.getFilePath();
                String fileName = at.getFileName();
                
                File deleteFile = new File(filePath + fileName);
                
                if(deleteFile.exists()) {
                   deleteFile.delete();
                }
             }
          }
       }else {
          rollback(conn);
       }
       return result;
    }

	/** 게시글 삭제 Service
	 * @param no
	 * @return result
	 * @throws Exception
	 */
	public int updateBoardF1(int no) throws Exception {
		Connection conn = getConnection();
		
		int result = dao.updateBoardF1(conn, no);
		
		if(result > 0)		commit(conn);
		else				rollback(conn);
		
		close(conn);
		
		return result;
	}

	
	/** 게시글 썸네일 목록 조회 Service(마이바티스)
	 * @param boardNoStr
	 * @return fList
	 * @throws Exception
	 */
	public List<Attachment> selectThumbnailList(String boardNoStr) throws Exception {
		SqlSession session = getSqlSession();
		
		List<Attachment> fList = dao.selectThumbnailList(session,  boardNoStr);
		
		session.close();
		
		return fList;
	}

}
