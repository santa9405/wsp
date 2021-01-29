package com.kh.wsp.notice.model.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kh.wsp.notice.model.vo.Notice;

import static com.kh.wsp.common.JDBCTemplate.*;

public class NoticeDAO {
	
	// 자주 사용하는 JDBC 참조 변수 미리 선언.
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	// 외부 XML 파일에 작성된 sql을 읽어올 변수 선언
	Properties prop = null;
	
	// 기본 생성자로 NoticeDAO 객체 생성 시 SQL이 작성된 xml 파일 얻어오기
	public NoticeDAO() {
		String fileName = NoticeDAO.class.getResource("/com/kh/wsp/sql/notice/notice-query.xml").getPath();
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 공지사항 목록 조회 DAO
	 * @param conn
	 * @return list
	 * @throws Exception
	 */
	public List<Notice> selectList(Connection conn) throws Exception {
		List<Notice> list = null;
		
		String query = prop.getProperty("selectList");
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(query);
			
			// SQL 수행 후 DB 관련 문제가 발생하지 않으면
			// 조회 내용을 저장할 수 있는 ArrayList 생성
			
			list = new ArrayList<Notice>();
			
			while(rset.next()) {
				
				Notice notice = new Notice();
				notice.setNoticeNo(  	rset.getInt("NOTICE_NO")  );
				notice.setNoticeTitle(  rset.getString("NOTICE_TITLE")  );
				notice.setMemberId(  	rset.getString("MEMBER_ID")  );
				notice.setReadCount(  	rset.getInt("READ_COUNT")  );
				notice.setNoticeCreateDate(  rset.getDate("NOTICE_CREATE_DT")  );
				
				list.add(notice);
			}
			
		}finally {
			close(rset);
			close(stmt);
		}
		
		return list;
	}

	/** 공지사항 상세 조회 DAO
	 * @param conn
	 * @param noticeNo
	 * @return notice
	 * @throws Exception
	 */
	public Notice selectNotice(Connection conn, int noticeNo) throws Exception {
		
		// 1) 결과를 저장할 변수 선언
		Notice notice = null;
		
		// 2) SQL 구문 얻어오기
		String query = prop.getProperty("selectNotice");
		
		try {
			// 3) SQL 수행 수 결과를 notice에 저장
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, noticeNo);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				notice = new Notice();
				notice.setNoticeTitle( rset.getNString("NOTICE_TITLE"));
				notice.setNoticeContent( rset.getNString("NOTICE_CONTENT"));
				notice.setMemberId( rset.getString("MEMBER_ID"));
				notice.setReadCount( rset.getInt("READ_COUNT"));
				notice.setNoticeCreateDate( rset.getDate("NOTICE_CREATE_DT"));
				
			}
			
		}finally {
			// 4) 사용한 JDBC 객체 반환
			close(rset);
			close(pstmt);
		}

		// 5) 결과 반환
		return notice;
	}

	/** 조회수 증가 DAO
	 * @param conn
	 * @param noticeNo
	 * @return result
	 * @throws Exception
	 */
	public int increaseReadCount(Connection conn, int noticeNo) throws Exception {
		int result = 0;
		
		String query = prop.getProperty("increaseReadCount");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, noticeNo);
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
			
		}
		return result;
	}

	/** 공지사항 등록시 사용될 번호 반환 DAO
	 * @param conn
	 * @return noticeNo
	 * @throws Exception
	 */
	public int selectNextNo(Connection conn) throws Exception {
		int noticeNo = 0;
		
		String query = prop.getProperty("selectNextNo");
		
		try {
			stmt = conn.createStatement();
			
			rset = stmt.executeQuery(query);
			
			if(rset.next()) {
				noticeNo = rset.getInt(1);
			}
		}finally {
			close(rset);
			close(stmt);
		}
		return noticeNo;
	}

	/** 공지사항 등록 DAO
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertNotice(Connection conn, Map<String, Object> map) throws Exception {
		int result = 0;
		
		String query = prop.getProperty("insertNotice");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, (int)map.get("noticeNo"));
			pstmt.setString(2, (String)map.get("noticeTitle"));
			pstmt.setString(3, (String)map.get("noticeContent"));
			pstmt.setInt(4, (int)map.get("noticeWriter"));
			
			result = pstmt.executeUpdate();	
		}finally {
			close(pstmt);
		}
		return result;
	}

	/** 게시글 수정 DAO
	 * @param conn
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateNotice(Connection conn, Map<String, Object> map) throws Exception {
		int result = 0;
		
		String query = prop.getProperty("updateNotice");
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setNString(1, (String)map.get("noticeTitle"));
			pstmt.setNString(2, (String)map.get("noticeContent"));
			pstmt.setInt(3, (int)map.get("noticeNo"));
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	/** 공지사항 삭제 DAO
	 * @param conn
	 * @param noticeNo
	 * @return result
	 * @throws Exception
	 */
	public int updateNoticeFl(Connection conn, int noticeNo) throws Exception {
		int result = 0;
		
		String query = prop.getProperty("updateNoticeFl");
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, noticeNo);
			result = pstmt.executeUpdate();
		}finally {
			close(pstmt);
			
		}
		return result;
	}

}
