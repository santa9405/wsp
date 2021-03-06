package com.kh.wsp.member.model.service;

import static com.kh.wsp.common.MybatisTemplate.getSqlSession;
import static com.kh.wsp.common.JDBCTemplate.*;

import java.sql.Connection;

import org.apache.ibatis.session.SqlSession;

import com.kh.wsp.member.model.dao.MemberDAO;
import com.kh.wsp.member.model.dao.MemberDAO2;
import com.kh.wsp.member.model.vo.Member;

public class MemberService2 {
	
	private MemberDAO2 dao = new MemberDAO2();

	/** 로그인 Service
	 * @param member
	 * @return loginMember
	 * @throws Exception
	 */
	public Member loginMember(Member member) throws Exception {
		SqlSession session = getSqlSession();
		
		// 1) Connection 얻어오기
		//Connection conn = getConnection();
		
		// 2) DAO 메소드를 수행하여 결과 반환받기
		Member loginMember = dao.loginMember(session, member);
		
		// 3) Connection 반환하기
		session.close();
		
		// 4) DAO 수행 결과를 Controller로 반환하기
		return loginMember;
	}

	/** 회원가입 Service
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int signUp(Member member) throws Exception {
		SqlSession session = getSqlSession();
		
		// 1) Connection 얻어오기
		//Connection conn = getConnection();
		
		// 2) DAO 메소드를 수행하여 결과 반환받기
		int result = dao.signUp(session, member);
		
		// 3) 트랜잭션 처리
		if(result > 0) session.commit();
		else		   session.rollback();
		
		// 4) Connection 반환
		session.close();
		
		// 5) 결과값 반환
		return result;
	}

	/** 아이디 중복 검사 Service
	 * @param id
	 * @return result
	 * @throws Exception
	 */
	public int idDupCheck(String id) throws Exception {
		SqlSession session = getSqlSession();
		
		// 1) 커넥션 얻어오기
		//Connection conn = getConnection();
		
		// 2) DAO 호출 후 결과 반환
		int result = dao.idDupCheck(session, id);
		
		// 3) Connection 반환
		session.close();
		
		// 4) 결과값 반환
		return result;
	}

	/** 회원정보 수정 Service
	 * @param member
	 * @return result
	 */
	public int updateMember(Member member) throws Exception {
		SqlSession session = getSqlSession();
		
		// 1) Connection 얻어오기
		//Connection conn = getConnection();
		
		// 2) DAO 메소드를 수행하여 결과 반환받기
		int result = dao.updateMember(session, member);
		
		// 3) 트랜잭션 처리
		if(result > 0) session.commit();
		else		   session.rollback();
		
		// 4) Connection 반환
		session.close();
		
		// 5) 결과값 반환
		return result;
		
	}

	/** 비밀번호 변경 Service
	 * @param loginMember
	 * @param newPwd
	 * @return result
	 * @throws Exception
	 */
	public int updatePwd(Member loginMember, String newPwd) throws Exception {
		SqlSession session = getSqlSession();
		
		//Connection conn = getConnection();
		
		// 1) 현재 비밀번호가 일치 하는지 검사
		int result = dao.checkCurrentPwd(session, loginMember);
		
		// 2) 현재 비밀번호 일치 시 새 비밀번호로 수정
		if(result > 0) { // 현재 비밀번호 일치
			
			// loginMember의 비밀번호 필드에 newPwd를 세팅하여 재활용
			// memberNo / currentPwd -> memberNo / newPwd
			loginMember.setMemberPwd(newPwd);
			
			result = dao.updatePwd(session, loginMember);
			
			// 트랜잭션 처리
			if(result > 0) session.commit();
			else		   session.rollback();
			
		}else { // 현재 비밀번호 불일치
			result = -1;
			
		}
		
		session.close();
		
		return result; 
		// 출력 경우의 수 : -1(비밀번호 불일치), 0(실패), 1(성공)
	}

	/** 회원탈퇴 Service
	 * @param loginMember 
	 * @return result
	 * @throws Exception
	 */
	public int updateStatus(Member loginMember) throws Exception {
		SqlSession session = getSqlSession();
		
		//Connection conn = getConnection();
		
		// 1) 현재 비밀번호가 일치하는지 검사
		int result = dao.checkCurrentPwd(session, loginMember);
		
		// 2) 현재 비밀번호 일치시 회원탈퇴 진행
		if(result > 0) {
		
			result = dao.updateStatus(session, loginMember);
			//result = dao.updateStatus(conn, loginMember.getMemberNo());
			// 탈퇴하려는 회원의 번호만 있으면 탈퇴 가능
			
			if(result > 0) session.commit();
			else		   session.rollback();
		}else {
			result = -1;
		}
		
		return result;
		// 출력 경우의 수 : -1(비밀번호 불일치), 0(실패), 1(성공)
	}

	
	
	
	
	
	
	
	
}
