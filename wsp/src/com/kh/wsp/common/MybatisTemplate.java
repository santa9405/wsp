package com.kh.wsp.common;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisTemplate {
	
	public static SqlSession getSqlSession() {
		// SqlSession : 확장된 Connection
		
		SqlSession session = null;
		
		try {
			String resource = "/mybatis-config.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			session = new SqlSessionFactoryBuilder().build(inputStream).openSession(false);
			
			// SqlSessionFactoryBuilder : SqlSession을 만들어 내는 공장을 만드는 객체
			// SqlSessionFactory : SqlSessionFactoryBilder.build()의 결과물
			//	-> SqlSessionFactory 공장에서 SqlSession을 만듦
			
			// openSession(false) : SqlSession을 만들 때 AutoCommit을 false 상태로 만들게 함
			// (AutoCommit : commit, rollback을 개발자가 service에서 제어할 수 있음)
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return session;
		
	}
	
}
