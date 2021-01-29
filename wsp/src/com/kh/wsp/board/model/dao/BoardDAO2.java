package com.kh.wsp.board.model.dao;

import static com.kh.wsp.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.kh.wsp.board.model.vo.Attachment;
import com.kh.wsp.board.model.vo.Board;
import com.kh.wsp.board.model.vo.PageInfo;

public class BoardDAO2 {
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;

	private Properties prop = null;

	public BoardDAO2() {
		String fileName = BoardDAO2.class.getResource("/com/kh/wsp/sql/board/board-query.xml").getPath();
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 전체 게시글 수 반환 DAO
	 * 
	 * @param session
	 * @return listCount
	 * @throws Exception
	 */
	public int getListCount(SqlSession session) throws Exception {
		// 기존 JDBC에서는 Statement 또는 PreparedStatement를 이용해야만
		// SQL구문을 수행하고 결과를 받아왔음. -> 번거로운 과정이 많음
		
		// 하지만 마이바티스는 SqlSession에서 제공하는 메소드를 통해
		// SQL 구문 세팅, 수행, 결과 반환을 간단히 진행할 수 있음.
		
		// selectOne() 메소드 : 조회 결과가 단일 행일 때 사용
		// 첫 번째 매개변수 : sql이 작성된 mapper의 이름.태그 아이디
		// 두 번째 매개변수 : sql 수행 시 필요한 전달 값(없으면 적지 않음)
		int listCount = session.selectOne("boardMapper.getListCount");
		return listCount;
	}

	/**
	 * 게시글 목록 조회 DAO
	 * 
	 * @param conn
	 * @param pInfo
	 * @return bList
	 * @throws Exception
	 */
	public List<Board> selectBoardList(SqlSession session, PageInfo pInfo) throws Exception {
		List<Board> bList = null;

		// Servlet / JSP에서 원하는 페이지만 조회해야 할 경우
		// ROWNUM이 포함된 복잡한 Select문을 사용함.
		
		// 하지만 마이바티스에서 이를 쉽게 조회할 수 있는
		// RowBounds 객체를 제공해줌.
		
		// offset : 시작점
		// limit : 시작점으로 부터의 범위(~개 까지)
		
		int offset = (pInfo.getCurrentPage() - 1) * pInfo.getLimit();
		
		RowBounds rowBounds = new RowBounds(offset, pInfo.getLimit());
		
		// selectList() 메소드 : 다중 행 결과를 자동적으로 List에 담아 반환
		// 첫 번째 매개변수 : mapper이름.태그아이디
		// 두 번째 매개변수 : SQL에서 사용할 전달값(없으면 null)
		// 세 번째 매개변수 : RowBounds 객체를 참조하는 변수
		bList = session.selectList("boardMapper.selectList", null, rowBounds);
		
		return bList;
	}

	/**
	 * 게시글 상세 조회 DAO
	 * 
	 * @param session
	 * @param boardNo
	 * @return board
	 * @throws Exception
	 */
	public Board selectBoard(SqlSession session, int boardNo) throws Exception {
		return session.selectOne("boardMapper.selectBoard", boardNo);
	}

	/**
	 * 조회수 증가 DAO
	 * 
	 * @param session
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int increaseReadCount(SqlSession session, int boardNo) throws Exception {
		return session.update("boardMapper.increaseReadCount", boardNo);
	}

	/**
	 * 다음 게시글 번호 조회 DAO
	 * 
	 * @param session
	 * @return boardNo
	 * @throws Exception
	 */
	public int selectNextNo(SqlSession session) throws Exception {
		return session.selectOne("boardMapper.selectNextNo");
	}

	/**
	 * 게시글 삽입 DAO
	 * 
	 * @param session
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(SqlSession session, Map<String, Object> map) throws Exception {
		return session.insert("boardMapper.insertBoard", map);
	}

	/**
	 * 파일 정보 삽입 DAO
	 * 
	 * @param conn
	 * @param at
	 * @return result
	 * @throws Exception
	 */
	public int insertAttachment(Connection conn, Attachment at) throws Exception {
		int result = 0;

		String query = prop.getProperty("insertAttachment");

		try {
			pstmt = conn.prepareStatement(query);

			pstmt.setNString(1, at.getFilePath());
			pstmt.setNString(2, at.getFileName());
			pstmt.setInt(3, at.getFileLevel());
			pstmt.setInt(4, at.getParentBoardNo());

			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);

		}
		return result;
	}

	/**
	 * 게시글에 포함된 이미지 목록 조회 DAO
	 * 
	 * @param session
	 * @param boardNo
	 * @return fList
	 * @throws Exception
	 */
	public List<Attachment> selectBoardFiles(SqlSession session, int boardNo) throws Exception {
		return session.selectList("boardMapper.selectBoardFiles", boardNo);
	}

	/**
	 * 썸네일 목록 조회 DAO
	 * 
	 * @param conn
	 * @param pInfo
	 * @return fList
	 * @throws Exception
	 */
	public List<Attachment> selectThumbnailList(Connection conn, PageInfo pInfo) throws Exception {
		List<Attachment> fList = null;

		String query = prop.getProperty("selectThumbnailList");

		try {
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);

			rset = pstmt.executeQuery();

			fList = new ArrayList<Attachment>();

			while (rset.next()) {

				Attachment at = new Attachment();
				at.setFileName(rset.getNString("FILE_NAME"));
				at.setParentBoardNo(rset.getInt("PARENT_BOARD_NO"));

				fList.add(at);
			}

		} finally {
			close(rset);
			close(pstmt);
		}

		return fList;
	}

	/**
	 * 게시글 수정 DAO
	 * 
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateBoard(Connection conn, Map<String, Object> map) throws Exception {
		int result = 0;

		String query = prop.getProperty("updateBoard");

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, (String) map.get("boardTitle"));
			pstmt.setString(2, (String) map.get("boardContent"));
			pstmt.setInt(3, (int) map.get("categoryCode"));
			pstmt.setInt(4, (int) map.get("boardNo"));

			result = pstmt.executeUpdate();

		} finally {
			close(pstmt);
		}
		return result;
	}

	/**
	 * 파일 정보 수정 DAO
	 * 
	 * @param conn
	 * @param newFile
	 * @return result
	 * @throws Exception
	 */
	public int updateAttachment(Connection conn, Attachment newFile) throws Exception {
		int result = 0;

		String query = prop.getProperty("updateAttachment");

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, newFile.getFilePath());
			pstmt.setString(2, newFile.getFileName());
			pstmt.setInt(3, newFile.getFileNo());

			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
		return result;
	}

	/**
	 * 게시글 삭제 DAO
	 * 
	 * @param conn
	 * @param no
	 * @return result
	 * @throws Exception
	 */
	public int updateBoardF1(Connection conn, int no) throws Exception {

		int result = 0;

		String query = prop.getProperty("updateBoardF1");

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			result = pstmt.executeUpdate();

		} finally {
			close(pstmt);

		}

		return result;
	}

	/** 썸네일 목록 조회 DAO(마이바티스)
	 * @param session
	 * @param boardNoStr
	 * @return fList
	 * @throws Exception
	 */
	public List<Attachment> selectThumbnailList(
			SqlSession session, String boardNoStr) throws Exception {
		return session.selectList("boardMapper.selectThumbnailList", boardNoStr);
	}

	/** 게시글 이미지 리스트 삽입 DAO
	 * @param session
	 * @param fList
	 * @return result
	 * @throws Exception
	 */
	public int insertAttachmentList(SqlSession session, List<Attachment> fList) throws Exception {
		return session.insert("boardMapper.insertAttachmentList", fList);
	}
}