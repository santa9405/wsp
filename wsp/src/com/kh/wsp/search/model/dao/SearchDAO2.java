package com.kh.wsp.search.model.dao;

import static com.kh.wsp.common.JDBCTemplate.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.kh.wsp.board.model.vo.Attachment;
import com.kh.wsp.board.model.vo.Board;
import com.kh.wsp.board.model.vo.PageInfo;

public class SearchDAO2 {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	
	/** 조건을 만족하는 게시글 수 조회 DAO
	 * @param session
	 * @param map
	 * @return listCount
	 * @throws Exception
	 */
	public int getListCount(SqlSession session, Map<String, Object> map) throws Exception {
		return session.selectOne("searchMapper.getListCount", map);
	}


	/** 검색 게시글 목록 조회 DAO
	 * @param session
	 * @param pInfo
	 * @param map
	 * @return bList
	 * @throws Exception
	 */
	public List<Board> searchBoardList(SqlSession session, PageInfo pInfo, Map<String, Object> map) throws Exception {
		
		int offset = (pInfo.getCurrentPage() - 1) * pInfo.getLimit();
		
		RowBounds rowBounds = new RowBounds(offset, pInfo.getLimit());
		
		return session.selectList("searchMapper.searchBoardList", map, rowBounds);
	}


	/** 검색이 적용된 썸네일 목록 조회 DAO
	    * @param session
	    * @param pInfo
	    * @param bList
	    * @return fList
	    * @throws Exception
	    */
	   public List<Attachment> searchThumbnailList(SqlSession session, List<Board> bList) throws Exception {
	      return session.selectList("searchMapper.searchThumbnailList", bList);
	   
	   }


	/** 다중 조건이 포함된 전체 게시글 수 조회 DAO
	 * @param session
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getListCount2(SqlSession session, Map<String, Object> map) throws Exception {
		return session.selectOne("searchMapper.getListCount2", map);
	}
	
	/** 다중 조건이 포함된 게시글 목록 조회 DAO
	 * @param session
	 * @param pInfo
	 * @param map
	 * @return bList
	 * @throws Exception
	 */
	public List<Board> searchBoardList2(SqlSession session, PageInfo pInfo, Map<String, Object> map) throws Exception {
		
		int offset = (pInfo.getCurrentPage() - 1) * pInfo.getLimit();
		
		RowBounds rowBounds = new RowBounds(offset, pInfo.getLimit());
		
		return session.selectList("searchMapper.searchBoardList2", map, rowBounds);
	}

}
