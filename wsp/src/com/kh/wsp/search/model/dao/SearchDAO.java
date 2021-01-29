package com.kh.wsp.search.model.dao;

import static com.kh.wsp.common.JDBCTemplate.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kh.wsp.board.model.vo.Attachment;
import com.kh.wsp.board.model.vo.Board;
import com.kh.wsp.board.model.vo.PageInfo;

public class SearchDAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	
	/** 조건을 만족하는 게시글 수 조회 DAO
	 * @param conn
	 * @param condition
	 * @return listCount
	 * @throws Exception
	 */
	public int getListCount(Connection conn, String condition) throws Exception {
		int listCount = 0;
		
		String query = "SELECT COUNT(*) FROM V_BOARD WHERE BOARD_STATUS = 'Y' AND " + condition;
		
		try {
			stmt = conn.createStatement();
			
			rset = stmt.executeQuery(query);
			if(rset.next()) {
				listCount = rset.getInt(1);
			}
		}finally {
			close(rset);
			close(stmt);
		}
		
		return listCount;
	}


	/** 검색 게시글 목록 조회 DAO
	 * @param conn
	 * @param pInfo
	 * @param condition
	 * @return bList
	 * @throws Exception
	 */
	public List<Board> searchBoardList(Connection conn, PageInfo pInfo, String condition) throws Exception {
		List<Board> bList = null;
		
		String query = 
				"SELECT * FROM" + 
				"    (SELECT ROWNUM RNUM , V.*" + 
				"    FROM" + 
				"        (SELECT * FROM V_BOARD " + 
				"        WHERE " + condition +
				"        AND BOARD_STATUS = 'Y' ORDER BY BOARD_NO DESC) V )" + 
				"WHERE RNUM BETWEEN ? AND ?";

		try {
			// SQL 구문 조건절에 대입할 변수 생성
			int startRow = (pInfo.getCurrentPage() -1) * pInfo.getLimit() + 1;
			int endRow = startRow + pInfo.getLimit() -1;
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rset = pstmt.executeQuery();
			
			bList = new ArrayList<Board>();
			
			while(rset.next()) {
				Board board = new Board(
						rset.getInt("BOARD_NO"), 
						rset.getNString("BOARD_TITLE"), 
						rset.getNString("MEMBER_ID"), 
						rset.getInt("READ_COUNT"), 
						rset.getNString("CATEGORY_NM"), 
						rset.getTimestamp("BOARD_CREATE_DT"));
				bList.add(board);
			}
			
		}finally {
			close(rset);
			close(pstmt);
		}
				
		return bList;
	}


	/** 검색이 적용된 썸네일 목록 조회 DAO
	    * @param conn
	    * @param pInfo
	    * @param condition
	    * @return fList
	    * @throws Exception
	    */
	   public List<Attachment> searchThumbnailList(Connection conn, PageInfo pInfo, String condition) throws Exception {
	      List<Attachment> fList = null;
	      
	      String query = 
	            "SELECT FILE_NAME, PARENT_BOARD_NO FROM ATTACHMENT " + 
	            "WHERE PARENT_BOARD_NO IN (" + 
	            "    SELECT BOARD_NO FROM " + 
	            "    (SELECT ROWNUM RNUM, V.* FROM " + 
	            "            (SELECT BOARD_NO  FROM V_BOARD " + 
	            "            WHERE BOARD_STATUS='Y' " + 
	            "            AND " + condition + 
	            "            ORDER BY BOARD_NO DESC ) V) " + 
	            "    WHERE RNUM BETWEEN ? AND ?" + 
	            ") " + 
	            "AND FILE_LEVEL = 0";
	      
	         try {
	            // 위치 홀더에 들어갈 시작 행, 끝 행번호 계산
	            int startRow = (pInfo.getCurrentPage() -1) * pInfo.getLimit() + 1;
	            int endRow = startRow + pInfo.getLimit()-1;
	            
	            pstmt = conn.prepareStatement(query);
	            pstmt.setInt(1, startRow);
	            pstmt.setInt(2, endRow);
	            
	            rset = pstmt.executeQuery();
	            
	            fList = new ArrayList<Attachment>();
	            while(rset.next()){
	               Attachment at = new Attachment();
	               at.setFileName(rset.getString("FILE_NAME"));
	               at.setParentBoardNo(rset.getInt("PARENT_BOARD_NO"));
	               
	               fList.add(at);
	            }
	         }finally {
	            close(rset);
	            close(pstmt);
	         }

	      return fList;
	   
	   }

}
