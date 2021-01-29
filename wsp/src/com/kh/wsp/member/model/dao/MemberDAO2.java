package com.kh.wsp.member.model.dao;

import static com.kh.wsp.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.session.SqlSession;

import com.kh.wsp.member.model.vo.Member;

public class MemberDAO2 {
	
	// DAO에서 자주 사용되는 JDBC 참조 변수 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rset = null;
	
	private Properties prop = null;
	
	public MemberDAO2() {
		// 외부에 저장된 XML 파일로 부터 SQL을 얻어옴 --> 유지보수성 향상
		try {
			String filePath = MemberDAO2.class.getResource("/com/kh/wsp/sql/member/member-query.xml").getPath();
					
			prop = new Properties();
			
			prop.loadFromXML(new FileInputStream(filePath));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 로그인 DAO
	 * @param session
	 * @param member
	 * @return loginMember
	 * @throws Exception
	 */
	public Member loginMember(SqlSession session, Member member) throws Exception {
		return session.selectOne("memberMapper.loginMember", member);
	}

	/** 회원가입 DAO
	 * @param session
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int signUp(SqlSession session, Member member) throws Exception {
		return session.insert("memberMapper.signUp", member);
	}

	/** 아이디 중복검사 DAO
	 * @param session
	 * @param id
	 * @return result
	 * @throws Exception
	 */
	public int idDupCheck(SqlSession session, String id) throws Exception {
		return session.selectOne("memberMapper.idDupCheck", id);
	}

	/** 회원정보 수정 DAO
	 * @param session
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int updateMember(SqlSession session, Member member) throws Exception  {
		return session.update("memberMapper.updateMember", member);
	}

	/** 현재 비밀번호 검사 DAO
	 * @param session
	 * @param loginMember
	 * @return result
	 * @throws Exception
	 */
	public int checkCurrentPwd(SqlSession session, Member loginMember) throws Exception {
		return session.selectOne("memberMapper.checkCurrentPwd", loginMember);
	}

	/** 비밀번호 변경 DAO
	 * @param session
	 * @param loginMember
	 * @return result
	 * @throws Exception
	 */
	public int updatePwd(SqlSession session, Member loginMember) throws Exception {
		return session.update("memberMapper.updatePwd", loginMember);
	}

	/** 회원탈퇴 DAO
	 * @param loginMember 
	 * @param session 
	 * @return result
	 * @throws Exception
	 */
	public int updateStatus(SqlSession session, Member loginMember) throws Exception {
		return session.update("memberMapper.updateStatus", loginMember);
	}

}
