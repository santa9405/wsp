<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<style>
.pagination {
   justify-content: center;
}

#searchForm {
   position: relative;
}

#searchForm>* {
   top: 0;
}

.boardTitle>img {
   width: 50px;
   height: 50px;
}

#list-table th {
   text-align: center;
}

#list-table td:not(:nth-of-type(3)) {
   text-align: center;
}

.list-wrapper{
   min-height: 540px;
}

#list-table td:hover{
   cursor : pointer;
}

/* 세로 가운데 정렬*/
#list-table td{
  vertical-align: middle;
  /* vertical-align : inline, inline-block 요소에만 적용 가능(td는 inline-block)*/
	
}

</style>

</head>
<body>
   <jsp:include page="../common/header.jsp"></jsp:include>
   <div class="container my-5">
      
      <h1>게시판</h1>
      
      	<jsp:include page="searchForm.jsp"></jsp:include>
      
         <div class="list-wrapper">
            <table class="table table-hover table-striped my-5" id="list-table">
               <thead>
                  <tr>
                     <th>글번호</th>
                     <th>카테고리</th>
                     <th>제목</th>
                     <th>작성자</th>
                     <th>조회수</th>
                     <th>작성일</th>
                  </tr>
               </thead>
               
               <%-- 게시글 목록 출력 --%>
               <tbody>
                  <c:choose>
                     <c:when test="${empty bList}">
                        <tr>
                           <td colspan="6">존재하는 게시글이 없습니다.</td>
                        </tr>
                     </c:when>
                     
                     <c:otherwise> <%-- 조회된 게시글 목록이 있을 때 --%>
                        <c:forEach var="board" items="${bList}">
                           <tr>
                              <td>${board.boardNo}</td>
                              <td>${board.categoryName}</td>
                              
                              <td class="boardTitle">
	                              <%-- 썸네일 출력 --%>
	                              <c:forEach var="thumbnail" items="${fList}">
	                              	
	                              	<%-- 현재 출력하려는 게시글 번호와 썸네일 목록 중 
	                              		  부모게시글 번호가 일치하는 썸네일 정보가 있다면 --%>
	                              	<c:if test="${board.boardNo == thumbnail.parentBoardNo}">
	                              		
	                              		<img src="${contextPath}/resources/uploadImages/${thumbnail.fileName}">
	                              		
	                              	</c:if>
	                              	
	                              </c:forEach>
	                              
	                              
	                              ${board.boardTitle}
                              </td>
                              
                              <td>${board.memberId}</td>
                              <td>${board.readCount}</td>
                              <td>
                                 <%-- 날짜 출력 모양 지정 --%>
                                 <fmt:formatDate var="createDate"
                                     value="${board.boardCreateDate}"
                                      pattern="yyyy-MM-dd"/>  
                                      
                                                                            
                                 <fmt:formatDate var="today"
                                     value="<%= new java.util.Date() %>"
                                      pattern="yyyy-MM-dd"/> 
                                         
                                 
                                      
                                 <c:choose>
                                    <%-- 글 작성일이 오늘이 아닐 경우 --%>
                                    <c:when test="${createDate != today }">
                                       ${createDate}
                                    </c:when>
                                    
                                    <%-- 글 작성일이 오늘일 경우 --%>
                                    <c:otherwise>
                                       <fmt:formatDate value="${board.boardCreateDate}"
                                      pattern="HH:mm"/>    
                                    </c:otherwise>
                                 
                                 </c:choose>                                    
                              </td>
                           </tr>
                        </c:forEach>
                     </c:otherwise>
                  </c:choose>
               </tbody>
            </table>
         </div>


         <%-- 로그인이 되어있는 경우 --%>
         <c:if test="${!empty loginMember}">
         	<button type="button" class="btn btn-primary float-right" id="insertBtn" 
         	 onclick="location.href = '${contextPath}/board/insertForm.do'">글쓰기</button>
         </c:if>
         
         <%---------------------- Pagination ----------------------%>
         <%-- 검색 내용이 파라미터에 존재할 때   ==  검색을 통해 만들어진 페이지인가?  --%>
         <c:choose>
		 <c:when test="${!empty param.ct}">
			<c:url var="pageUrl" value="/search2.do"/>
			
			<%-- 쿼리스트링으로 사용할 내용을 변수에 저장 --%>
			<c:set var="category" value="&ct=${fn:join(paramValues.ct,'&ct=')}"/>
			
			<c:set var="searchStr" value="${category}&sk=${param.sk}&sv=${param.sv}" />
			
		 </c:when>
         
         
         	<%-- 페이징 처리 주소를 쉽게 사용할 수 있도록 미리 변수에 저장 --%>
            <%-- 검색 내용이 파라미터에 존재할 때 == 검색을 통해 만들어진 페이지인가? --%>
            <c:when test="${!empty param.sk && !empty param.sv}">
               <c:url var="pageUrl" value="/search.do"/>
               
               <!-- 쿼리스트링으로 사용할 내용을 변수에 저장 -->
               <c:set var="searchStr" value="&sk=${param.sk}&sv=${param.sv}"/>
            </c:when>
            
            <c:otherwise>
              <c:url var="pageUrl" value="/board/list.do"/>
            </c:otherwise>
            
         </c:choose>
         
         <!-- 화살표에 들어갈 주소를 변수로 생성 -->
         <%-- 
		            검색을 안했을 때 : /board/list.do?cp=1
		            검색을 했을 때 :  /search.do?cp=1&sk=title&sv=49
          --%>
         
         <c:set var="firstPage" value="${pageUrl}?cp=1${searchStr}"/>
         <c:set var="lastPage" value="${pageUrl}?cp=${pInfo.maxPage}${searchStr}"/>
         
         <%-- EL을 이용한 숫자 연산의 단점 : 연산이 자료형에 영향을 받지 않는다. --%>
         <%--
          <fmt : parseNumber> : 숫자 형태를 지정하여 변수 선언 
          integerOnly="true" : 정수로만 숫자 표현(소수점 버림)
         --%>
         
         <fmt:parseNumber var="c1" value="${(pInfo.currentPage - 1) / 10}" integerOnly="true"/>
         <fmt:parseNumber var="prev" value="${c1 * 10}" integerOnly="true"/>
         <c:set var="prevPage" value="${pageUrl}?cp=${prev}${searchStr}" />
         
         <fmt:parseNumber var="c2" value="${(pInfo.currentPage + 9) / 10 }" integerOnly="true" />
         <fmt:parseNumber var="next" value="${ c2 * 10 + 1}" integerOnly="true" />
         <c:set var="nextPage" value="${pageUrl}?cp=${next}${searchStr}" />
         
         
         
         <div class="my-5">
            <ul class="pagination">
               <%-- 현재 페이지가 10페이지 초과인 경우 --%>
               <c:if test="${pInfo.currentPage > 10}">
                  <li> <%-- 첫 페이지로 이동(<<) --%>
                     <a class="page-link" href="${firstPage}">&lt;&lt;</a>
                  </li>
                  <li> <%-- 이전 페이지로 이동(<) --%>
                     <a class="page-link" href="${prevPage}">&lt;</a>
                  </li>
               </c:if>
               
               <!--  페이지 목록   -->
               <c:forEach var="page" begin="${pInfo.startPage}" end="${pInfo.endPage}">
                  <c:choose>
                     <c:when test="${pInfo.currentPage == page}">
                        <li>
                           <a class="page-link">${page}</a>
                        </li>
                     </c:when>
                     
                     <c:otherwise>
                        <li>
                           <a class="page-link" href="${pageUrl}?cp=${page}${searchStr}">${page}</a>
                        </li>
                     </c:otherwise>
                  </c:choose>
               </c:forEach>
               

            <%-- 다음 페이지가 마지막 페이지 이하인 경우 --%>
            <c:if test="${ next <= pInfo.maxPage}">
               <li>
                  <%-- 다음 페이지로 이동(>) --%> 
                  <a class="page-link" href="${nextPage}">&gt;</a>
               </li>
               <li>
                  <%-- 마지막 페이지로 이동(>>) --%> 
                  <a class="page-link" href="${lastPage}">&gt;&gt;</a>
               </li>
            </c:if>

         </ul>
         </div>
      
      
         <!-- 검색창 -->
         <div class="my-5">
            <form action="${contextPath}/search.do" method="GET" class="text-center" id="searchForm">
               <select name="sk" class="form-control" style="width: 100px; display: inline-block;">
                  <option value="title">글제목</option>
                  <option value="content">내용</option>
                  <option value="titcont">제목+내용</option>
                  <option value="writer">작성자</option>
               </select>
               <input type="text" name="sv" class="form-control" style="width: 25%; display: inline-block;">
               <button class="form-control btn btn-primary" style="width: 100px; display: inline-block;">검색</button>
            </form>


         </div>
   </div>
   <jsp:include page="../common/footer.jsp"></jsp:include>


   <script>
      // 게시글 상세보기 기능 (jquery를 통해 작업)
      $("#list-table td").on("click",function(){
         
         // 게시글 번호 얻어오기
         var boardNo = $(this).parent().children().eq(0).text();
         //console.log(boardNo);
         // 클릭이 되는지 테스트
         
         var url = "${contextPath}/board/view.do?cp=${pInfo.currentPage}&no=" + boardNo + "${searchStr}";
         
         location.href = url;
         
      });
      
      
      // 검색 내용이 있을 경우 검색창에 해당 내용을 작성해두는 기능
      (function(){
    	  var searchKey = "${param.sk}";
    	  // 파라미터 중 sk가 있을 경우 ex)"49"
    	  // 파라미터 중 sk가 없을 경우 ex) ""
    	  var searchValue = "${param.sv}";
    	  
    	  // 검색창 select의 option을 반복 접근
    	  $("select[name=sk] > option").each(function(index, item){
    		  // index : 현재 접근 중인 요소의 인덱스
    		  // item : 현재 접근 중인 요소
    		  
    		  		// title          title
    		  if( $(item).val() == searchKey ){ // 제목으로 검색한 경우
    			 $(item).prop("selected", true);
    		  }
    	  });
    	  
    	  // 검색어 입력창에 searchValue 값 출력
    	  $("input[name=sv]").val(searchValue);
    	  
      })();
   </script>
</body>
</html>