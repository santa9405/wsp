 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내정보</title>
<style>
   input[type="number"]::-webkit-outer-spin-button, 
   input[type="number"]::-webkit-inner-spin-button
      {
      -webkit-appearance: none;
      margin: 0;
   }
</style>
</head>
<body>
   <div class="container">
      <jsp:include page="../common/header.jsp"></jsp:include>
      
      <%-- 전화번호, 주소를 구분자를 이용하여 분리된 배열 형태로 저장 --%>
      <c:set var="phone" value="${fn:split(loginMember.memberPhone, '-') }"/>
      											         <%-- 문자열, 구분자 --%>
      <c:set var="address" value="${fn:split(loginMember.memberAddress, ',') }"/>
      
      <div class="row my-5">
         <jsp:include page="sideMenu.jsp"></jsp:include>      
            
         <div class="col-sm-8">
            <h3>My Page</h3>
            <hr>
            <div class="bg-white rounded shadow-sm container p-3">
               <form method="POST" action="updateMember.do" onsubmit="return memberUpdateValidate();" class="form-horizontal" role="form">
                  <!-- 아이디 -->
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <h6>아이디</h6>
                     </div>
                     <div class="col-md-6">
                        <h5 id="id">${loginMember.memberId}</h5>
                     </div>
                  </div>
   
                  <!-- 이름 -->
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <h6>이름</h6>
                     </div>
                     <div class="col-md-6">
                        <h5 id="name">${loginMember.memberName}</h5>
                     </div>
                  </div>
   
                  <!-- 전화번호 -->
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <label for="phone1">전화번호</label>
                     </div>
                     <!-- 전화번호1 -->
                     <div class="col-md-3">
                        <select class="custom-select" id="phone1" name="phone1">
                           <option>010</option>
                           <option>011</option>
                           <option>016</option>
                           <option>017</option>
                           <option>019</option>
                        </select>
                     </div>
                     
   
                     <!-- 전화번호2 -->
                     <div class="col-md-3">
                        <input type="number" class="form-control phone" id="phone2" name="phone2" value="${phone[1]}">
                     </div>
   
                     <!-- 전화번호3 -->
                     <div class="col-md-3">
                        <input type="number" class="form-control phone" id="phone3" name="phone3" value="${phone[2]}">
                     </div>
                  </div>
   
                  <!-- 이메일 -->
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <label for="memberEmail">Email</label>
                     </div>
                     <div class="col-md-6">
                        <input type="email" class="form-control" id="email" name="email" value="${loginMember.memberEmail}">
                     </div>
                  </div>
                  <br>
   
                  <!-- 주소 -->
                  <!-- 오픈소스 도로명 주소 API -->
                  <!-- https://www.poesis.org/postcodify/ -->
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <label for="postcodify_search_button">우편번호</label>
                     </div>
                     <div class="col-md-3">
                        <input type="text" name="post" class="form-control postcodify_postcode5" value="${address[0]}">
                     </div>
                     <div class="col-md-3">
                        <button type="button" class="btn btn-primary" id="postcodify_search_button">검색</button>
                     </div>
                  </div>
   
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <label for="address1">도로명 주소</label>
                     </div>
                     <div class="col-md-9">
                        <input type="text" class="form-control postcodify_address" name="address1" id="address1"  value="${address[1]}">
                     </div>
                  </div>
   
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <label for="address2">상세주소</label>
                     </div>
                     <div class="col-md-9">
                        <input type="text" class="form-control postcodify_details" name="address2" id="address2"  value="${address[2]}">
                     </div>
                  </div>
   
   
                  <!-- 관심분야 -->
                  <hr class="mb-4">
                  <div class="row">
                     <div class="col-md-3">
                        <label>관심 분야</label>
                     </div>
                     
                     <div class="col-md-9 custom-control custom-checkbox">
                     
                        <div class="form-check form-check-inline">
                           <input type="checkbox" class="form-check-input custom-control-input"
                              name="memberInterest" id="sports" value="운동">
                           <label class="form-check-label custom-control-label" for="sports">운동</label>
                        </div>
                        
                        <div class="form-check form-check-inline">
                           <input type="checkbox" class="form-check-input custom-control-input"
                              name="memberInterest" id="movie" value="영화">
                           <label class="form-check-label custom-control-label" for="movie">영화</label>
                        </div>
                        
                        <div class="form-check form-check-inline">
                           <input type="checkbox" class="form-check-input custom-control-input" 
                              name="memberInterest" id="music" value="음악">
                           <label class="form-check-label custom-control-label" for="music">음악</label>
                        </div>
                        <br>
                        
                        <div class="form-check form-check-inline">
                           <input type="checkbox" name="memberInterest" id="cooking"
                              value="요리" class="form-check-input custom-control-input">
                           <label class="form-check-label custom-control-label"
                              for="cooking">요리</label>
                        </div>
                        
                        <div class="form-check form-check-inline">
                           <input type="checkbox"  class="form-check-input custom-control-input" 
                              name="memberInterest" id="game" value="게임">
                           <label class="form-check-label custom-control-label" for="game">게임</label>
                        </div>
                        
                        <div class="form-check form-check-inline">
                           <input type="checkbox" class="form-check-input custom-control-input" 
                              name="memberInterest" id="etc" value="기타"> 
                           <label class="form-check-label custom-control-label" for="etc">기타</label>
                        </div>
                        
                     </div>
                  </div>
   
                  <hr class="mb-4">
                  <button class="btn btn-primary btn-lg btn-block" type="submit">수정</button>
               </form>
            </div>
         </div>
      </div>
   </div>
   <br><br>
   
   <jsp:include page="../common/footer.jsp"></jsp:include>
      
      
      
   <!-- postcodify를 로딩 -->
   <script src="//d1p7wdleee1q2z.cloudfront.net/post/search.min.js"></script>
   
   <!-- 회원 관련 Javascript 코드를 모아둘 wsp_member.js 파일을 작성 -->
   <script src="${contextPath}/resources/js/wsp_member.js"></script>
   
       
   <script>
      /* 스크립트 코드에 EL/JSTL 이 포함된 경우 별도 js파일에 작성할 수 없다
      - js파일은 요청 시 클라이언트 측으로 전달되어져 브라우저가 해석하지만
        JSP(EL/JSTL)는 서버측에서 JAVA로 변환되고 해석되어야함.
         -> js파일에 작성하는 경우 EL/JSTL 코드가 해석되지 않은 상태로 클라이언트로 전달되어 
            의도한 형태로 해석되지 않는 문제가 발생함.
      */
   
      // 전화번호 첫 번째 자리를 회원 전화 번호 첫 번째 자리와 일치하는 값으로 선택하기
      // (function(){})(); -> 즉시 처리 함수
      // 함수가 정의 되자마자 수행되는 함수. 지역변수 이므로 변수명 충돌 현상 방지 + 속도적 측면에서 우위가 있음.
      
      (function(){
         
         // #phone1의 자식 중 option 태그들을 반복 접근
         $("#phone1 > option").each(function(index, item){
            // index : 현재 접근중인 인덱스
            // item : 현재 접근중인 요소
            
            // 현재 접근한 요소의 내용이 phone[0]과 같을 경우
            if($(item).text() == "${phone[0]}"){
               //현재 접근한 요소에 selected 속성 추가
               $(item).prop("selected", true);
            }
         });
         
      })();

      
   // 관심분야 중 회원 정보와 일치하는 부분 체크하기
      (function(){
         
         // 회원 정보에서 관심 분야 문자열을 얻어와 ','를 구분자로하여 분리하기
         var interest = "${loginMember.memberInterest}".split(",");
         
         // 체크 박스 요소를 모두 선택하여 반복 접근
         $("input[name='memberInterest']").each(function(index, item){
            
            // interest 배열 내에
            // 현재 접근중인 체크 박스의 value와 일치하는 요소가 있는지 확인
            if(interest.indexOf( $(item).val()) != -1 ){
               $(item).prop("checked", true);
            } 
         });
         
      })();
      
      
      
   </script>











   
   
</body>
</html>