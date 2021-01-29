package com.kh.wsp.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.kh.wsp.wrapper.EncryptWrapper;

@WebFilter({"/member/Login.do", "/member/signUp.do", "/member/updatePwd.do", "/member/updateStatus.do"})
public class EncryptFilter implements Filter {

    public EncryptFilter() {}
    public void init(FilterConfig fConfig) throws ServletException {}
   public void destroy() {}

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      
      // 비밀번호 암호화
      // - 입력된 비밀번호는 HttpServletRequest에 Parameter로 전달됨
      //    -> Parameter의 가공이 필요한 경우 Wrapper 클래스가 필요함.
      
      HttpServletRequest req = (HttpServletRequest) request;
      
      // 암호화 wrapper 클래스 생성
      EncryptWrapper encWrapper = new EncryptWrapper(req);
      
      
      chain.doFilter(encWrapper, response);
      
   }

}