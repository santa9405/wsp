package com.kh.wsp.reply.model.dao;

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

import org.apache.ibatis.session.SqlSession;

import com.kh.wsp.board.model.dao.BoardDAO;
import com.kh.wsp.reply.model.vo.Reply;


public class ReplyDAO2 {
   private Statement stmt = null;
   private PreparedStatement pstmt = null;
   private ResultSet rset = null;
   
   private Properties prop;
   public ReplyDAO2(){
      String fileName = ReplyDAO2.class.getResource("/com/kh/wsp/sql/reply/reply-query.xml").getPath();
      try {
         prop = new Properties();
         prop.loadFromXML(new FileInputStream(fileName)); 
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   
   
   /** 댓글 목록 조회 DAO
    * @param session
    * @param parentBoardNo
    * @return rList
    * @throws Exception
    */
   public List<Reply> selectList(SqlSession session, int parentBoardNo) throws Exception{
      return session.selectList("replyMapper.selectList", parentBoardNo);
   }



   /** 댓글 삽입 DAO
    * @param session
    * @param map
    * @return result
    * @throws Exception
    */
   public int insertReply(SqlSession session, Map<String, Object> map) throws Exception {
      return session.insert("replyMapper.insertReply", map);
   }



	/** 댓글 수정 DAO
	 * @param session
	 * @param map
	 * @return result
	 * @throws Exception
	 */
	public int updateReply(SqlSession session, Map<String, Object> map) throws Exception {
		return session.update("replyMapper.updateReply", map);
	}

	/** 댓글 상태 변경 DAO
	 * @param session
	 * @param replyNo
	 * @return result
	 * @throws Exception
	 */
	public int updateReplyStatus(SqlSession session, int replyNo) throws Exception {
		return session.update("replyMapper.updateReplyStatus", replyNo);
	}
	
}