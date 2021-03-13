package com.kh.ajax.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JDBCTemplate {
   
   private static Connection conn = null;
   
   private JDBCTemplate() { }
   
   
   /* 요청시마다 Connection을 새로 만들고 반환 -> 요청시간 지연 -> 비효율적
    * 
    * WAS(web Application Server, Tomcat(==servlet container))가
    * 미리 DB에 접속할 수 있는 객체(Connection)를 일정 개수를 만들어 두고
    * 요청이 올 때마다 만들어둔 객체를 전달하고 사용 완료 후 반환받는
    * Connection Pool을 사용함
    * 
    * */
   public static Connection getConnection() throws NamingException, SQLException {
         // JNDI(Java Naming and Directory Interface API)
         /*디렉터리 서비스에 접근하는데 사용하는 API
		      어플리케이션은 JNDI를 사용하여 서버의 resource를 찾는다.
		      특히 JDBC resource를 data source라고 부른다.
         
            Resource를 서버에 등록할 때 고유한 JNDI 이름을 붙이는데, JNDI 이름은 디렉터리 경로 형태를 가진다.
        	예를 들어 data source의 JNDI 이름은 'jdbc/mydb' 형식으로 짓는다.
         
          	서버에서 'jdbc/oracle'라는 DataSource를 찾으려면 
            'java:comp/env/jdbc/oracle'라는 JNDI 이름으로 찾아야 한다. 
         	즉 lookup() 메소드에 'java:comp/env/jdbc/oracle'를 인자값으로 넘긴다.
         
         */
         
         // Servers에 존재하는 context.xml 파일을 찾는 작업
         Context initContext = new InitialContext();
         Context envContext  = (Context)initContext.lookup("java:/comp/env");  // java:comp/env   응용 프로그램 환경 항목
         
         // context.xml 파일에서 name이 "jdbc/oracle"인 DataSource를 얻어옴
         // DataSource : DriverManager를 대체하는 객체로 
         // Connection 생성, Connectoin pool을 구현하는 객체
         DataSource ds = (DataSource)envContext.lookup("jdbc/oracle");

         conn = ds.getConnection(); // DataSource에 의해 미리 만들어진 Connection 중 하나를 얻어옴.
         
         conn.setAutoCommit(false);
      
      return conn;
   }
   
   
   
   public static void commit(Connection conn) {
      try {
         if(conn != null && !conn.isClosed()) {
            
            conn.commit();
         }
      }catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   public static void rollback(Connection conn) {
      try {
         if(conn != null && !conn.isClosed()) {
            
            conn.rollback();
         }
      }catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   
   public static void close(Connection conn) {
      try {
         if(conn != null && !conn.isClosed()) {
            
            conn.close();
         }
      }catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   public static void close(ResultSet rset) {
      try {
         if(rset != null && !rset.isClosed()) {
            rset.close();
         }
      }catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   public static void close(Statement stmt) {
      try {
         if(stmt != null && !stmt.isClosed()) {
            stmt.close();
         }
      }catch (Exception e) {
         e.printStackTrace();
      }
   }
   
}