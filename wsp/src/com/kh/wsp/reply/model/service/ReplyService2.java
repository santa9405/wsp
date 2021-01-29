package com.kh.wsp.reply.model.service;

import static com.kh.wsp.common.MybatisTemplate.getSqlSession;
import static com.kh.wsp.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.kh.wsp.reply.model.dao.ReplyDAO;
import com.kh.wsp.reply.model.dao.ReplyDAO2;
import com.kh.wsp.reply.model.vo.Reply;

public class ReplyService2 {
	private ReplyDAO2 dao = new ReplyDAO2();

	/**
	 * 댓글 목록 조회 Service
	 * 
	 * @param parentBoardNo
	 * @return rList
	 * @throws Exception
	 */
	public List<Reply> selectList(int parentBoardNo) throws Exception {
		SqlSession session = getSqlSession();
		
		List<Reply> rList = dao.selectList(session, parentBoardNo);
		
		session.close();
		
		return rList;
	}

	/**
	 * 댓글 삽입 Service
	 * 
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int insertReply(Map<String, Object> map) throws Exception {
		SqlSession session = getSqlSession();
		
		String replyContent = (String) map.get("replyContent");

		// 크로스 사이트 스크립팅 방지 처리
		replyContent = replaceParameter(replyContent);

		// 개행문자 변환 처리
		// ajax 통신 시 textarea의 개행문자가 \n 하나만 넘어옴.
		// \n -> <br>
		replyContent = replyContent.replace("\n", "<br>");

		// 변경된 replyContent를 다시 reply에 세팅
		map.put("replyContent", replyContent);

		int result = dao.insertReply(session, map);

		// 트랜잭션 처리
		if (result > 0)
			session.commit();
		else
			session.rollback();

		return result;
	}

	// 크로스 사이트 스크립트 방지 메소드
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
	 * 댓글 수정 Service
	 * 
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateReply(Map<String, Object> map) throws Exception {
		SqlSession session = getSqlSession();

		String replyContent = (String) map.get("replyContent");

		// 크로스 사이트 스크립팅 방지 처리
		replyContent = replaceParameter(replyContent);

		// 개행문자 변환 처리
		// ajax 통신 시 textarea의 개행문자가 \n 하나만 넘어옴.
		// \n -> <br>
		replyContent = replyContent.replace("\n", "<br>");

		// 변경된 replyContent를 다시 reply에 세팅
		map.put("replyContent", replyContent);

		int result = dao.updateReply(session, map);

		if (result > 0)
			session.commit();
		else
			session.rollback();

		session.close();

		return result;
	}

	/**
	 * 댓글 상태 변경 Service
	 * 
	 * @param replyNo
	 * @return result
	 * @throws Exception
	 */
	public int updateReplyStatus(int replyNo) throws Exception {
		SqlSession session = getSqlSession();
		
		int result = dao.updateReplyStatus(session, replyNo);
		
		if(result > 0) session.commit();
		else		   session.rollback();
		
		session.close();

		return result;
	}

}
