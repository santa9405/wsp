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

import com.kh.wsp.board.model.vo.Attachment;
import com.kh.wsp.board.model.vo.Board;
import com.kh.wsp.board.model.vo.PageInfo;

public class BoardDAO {
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;

	private Properties prop = null;

	public BoardDAO() {
		String fileName = BoardDAO.class.getResource("/com/kh/wsp/sql/board/board-query.xml").getPath();
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
	 * @param conn
	 * @return listCount
	 * @throws Exception
	 */
	public int getListCount(Connection conn) throws Exception {
		int listCount = 0;

		String query = prop.getProperty("getListCount");

		try {
			stmt = conn.createStatement();

			rset = stmt.executeQuery(query);

			if (rset.next()) {
				listCount = rset.getInt(1);
			}

		} finally {
			close(stmt);
			close(rset);
		}
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
	public List<Board> selectBoardList(Connection conn, PageInfo pInfo) throws Exception {
		List<Board> bList = null;

		String query = prop.getProperty("selectBoardList");

		try {
			// SQL 구문 조건절에 대입할 변수 생성
			int startRow = (pInfo.getCurrentPage() - 1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() - 1;

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);

			rset = pstmt.executeQuery();

			bList = new ArrayList<Board>();

			while (rset.next()) {
				Board board = new Board(rset.getInt("BOARD_NO"), rset.getNString("BOARD_TITLE"),
						rset.getNString("MEMBER_ID"), rset.getInt("READ_COUNT"), rset.getNString("CATEGORY_NM"),
						rset.getTimestamp("BOARD_CREATE_DT"));
				bList.add(board);
			}

		} finally {
			close(rset);
			close(pstmt);
		}
		return bList;
	}

	/**
	 * 게시글 상세 조회 DAO
	 * 
	 * @param conn
	 * @param boardNo
	 * @return board
	 * @throws Exception
	 */
	public Board selectBoard(Connection conn, int boardNo) throws Exception {
		Board board = null;

		String query = prop.getProperty("selectBoard");

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, boardNo);
			rset = pstmt.executeQuery();

			if (rset.next()) {
				board = new Board();

				board.setBoardNo(rset.getInt("BOARD_NO"));
				board.setBoardTitle(rset.getString("BOARD_TITLE"));
				board.setBoardContent(rset.getString("BOARD_CONTENT"));
				board.setMemberId(rset.getString("MEMBER_ID"));
				board.setReadCount(rset.getInt("READ_COUNT"));
				board.setBoardCreateDate(rset.getTimestamp("BOARD_CREATE_DT"));
				board.setBoardModifyDate(rset.getTimestamp("BOARD_MODIFY_DT"));
				board.setCategoryName(rset.getString("CATEGORY_NM"));
			}

		} finally {
			close(rset);
			close(pstmt);
		}
		return board;
	}

	/**
	 * 조회수 증가 DAO
	 * 
	 * @param conn
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int increaseReadCount(Connection conn, int boardNo) throws Exception {
		int result = 0;

		String query = prop.getProperty("increaseReadCount");

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, boardNo);

			result = pstmt.executeUpdate();

		} finally {
			close(pstmt);
		}

		return result;
	}

	/**
	 * 다음 게시글 번호 조회 DAO
	 * 
	 * @param conn
	 * @return boardNo
	 * @throws Exception
	 */
	public int selectNextNo(Connection conn) throws Exception {
		int boardNo = 0;

		String query = prop.getProperty("selectNextNo");

		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);

			if (rset.next()) {
				boardNo = rset.getInt(1);
			}

		} finally {
			close(rset);
			close(stmt);
		}

		return boardNo;
	}

	/**
	 * 게시글 삽입 DAO
	 * 
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(Connection conn, Map<String, Object> map) throws Exception {
		int result = 0;

		String query = prop.getProperty("insertBoard");

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, (int) map.get("boardNo"));
			pstmt.setString(2, (String) map.get("boardTitle"));
			pstmt.setString(3, (String) map.get("boardContent"));
			pstmt.setInt(4, (int) map.get("boardWriter"));
			pstmt.setInt(5, (int) map.get("categoryCode"));

			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}

		return result;
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
	 * @param conn
	 * @param boardNo
	 * @return fList
	 * @throws Exception
	 */
	public List<Attachment> selectBoardFiles(Connection conn, int boardNo) throws Exception {
		List<Attachment> fList = null;

		String query = prop.getProperty("selectBoardFiles");

		try {
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, boardNo);

			rset = pstmt.executeQuery();

			fList = new ArrayList<Attachment>();

			while (rset.next()) {
				Attachment at = new Attachment(rset.getInt("FILE_NO"), rset.getNString("FILE_NAME"),
						rset.getInt("FILE_LEVEL"));

				at.setFilePath(rset.getNString("FILE_PATH"));

				fList.add(at);
			}

		} finally {
			close(rset);
			close(pstmt);
		}

		return fList;
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
}