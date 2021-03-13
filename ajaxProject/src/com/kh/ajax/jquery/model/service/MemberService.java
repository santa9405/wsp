package com.kh.ajax.jquery.model.service;

import static com.kh.ajax.common.JDBCTemplate.*;

import java.sql.Connection;

import com.kh.ajax.jquery.model.dao.MemberDAO;
import com.kh.ajax.jquery.model.vo.Member;

public class MemberService {
	
	private MemberDAO dao = new MemberDAO();

	/** 아이디 중복검사 Service
	 * @param inputId
	 * @return result
	 * @throws Exception
	 */
	public int idDupCheck(String inputId) throws Exception {
		Connection conn = getConnection();
		
		int result = dao.idDupCheck(conn, inputId);
		
		close(conn);
		
		return result;
	}

	/** 회원 정보 조회 Service
	 * @param inputId
	 * @return member
	 * @throws Exception
	 */
	public Member selectMember(String inputId) throws Exception {
		Connection conn = getConnection();
		Member member = dao.selectMember(conn, inputId);
		
		close(conn);
		
		return member;
	}

}
