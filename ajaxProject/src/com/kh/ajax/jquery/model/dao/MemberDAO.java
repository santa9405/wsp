package com.kh.ajax.jquery.model.dao;

import static com.kh.ajax.common.JDBCTemplate.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.kh.ajax.jquery.model.vo.Member;

public class MemberDAO {

	
	/** 아이디 중복검사 DAO
	 * @param conn
	 * @param inputId
	 * @return result
	 * @throws Exception
	 */
	public int idDupCheck(Connection conn, String inputId) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int result = 0;
		
		String query = "SELECT COUNT(*) FROM MEMBER WHERE MEMBER_ID=? AND MEMBER_STATUS='Y'";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, inputId);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				result = rset.getInt(1);
			}
		}finally {
			close(rset);
			close(pstmt);
			
		}
		
		return result;
	}

	/** 회원 정보 조회 DAO
	 * @param conn
	 * @param inputId
	 * @return member
	 * @throws Exception
	 */
	public Member selectMember(Connection conn, String inputId) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member member = null;
		
		String query = "SELECT MEMBER_ID, MEMBER_NM, MEMBER_EMAIL, " + 
	            "MEMBER_INTEREST, MEMBER_ENROLL_DATE " + 
	            "FROM MEMBER " + 
	            "WHERE MEMBER_ID = ? " + 
	            "AND MEMBER_STATUS = 'Y'";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setNString(1, inputId);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				member = new Member();
				
				member.setMemberId(rset.getString("MEMBER_ID"));
				member.setMemberName(rset.getString("MEMBER_NM"));
				member.setMemberEmail(rset.getString("MEMBER_EMAIL"));
				member.setMemberInterest(rset.getString("MEMBER_INTEREST"));
				member.setMemberEnrollDate(rset.getTimestamp("MEMBER_ENROLL_DATE"));
				
				// java.sql.Date 사용 시 년월일만 얻어와짐
				// ex) 2020-12-31 00:00:00 00
				
				// -> java.sqp.Timestamp를 활용하면 년월일 시분초 모두 얻어와짐
						
			}
			
		}finally {
			close(rset);
			close(pstmt);
			
		}
		
		return member;
	}
}
