<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글</title>
<style>
   #board-area{ min-height: 700px; margin-bottom : 100px; }
   #board-content{ padding-bottom:150px;}

   .boardImgArea{
      height: 300px;
   }

   .boardImg{
      width : 100%;
      height: 100%;
      
      max-width : 300px;
      max-height: 300px;
      
      margin : auto;
   }
   
   /* 이미지 화살표 색 조정
   -> fill='%23000' 부분의 000을
      RGB 16진수 값을 작성하여 변경 가능 
    */
   .carousel-control-prev-icon {
       background-image: url("data:image/svg+xml;charset=utf8,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='%23000' viewBox='0 0 8 8'%3E%3Cpath d='M5.25 0l-4 4 4 4 1.5-1.5-2.5-2.5 2.5-2.5-1.5-1.5z'/%3E%3C/svg%3E") !important;
   }
   
   .carousel-control-next-icon {
        background-image: url("data:image/svg+xml;charset=utf8,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='%23000' viewBox='0 0 8 8'%3E%3Cpath d='M2.75 0l-1.5 1.5 2.5 2.5-2.5 2.5 1.5 1.5 4-4-4-4z'/%3E%3C/svg%3E") !important;
   }
   
   .replyWrite > table{
      width: 90%;
      align: center;
   }
   
   #replyContentArea{ width: 90%; }
   
   #replyContentArea > textarea{
       resize: none;
       width: 100%;
   }
   
   #replyBtnArea{
       width: 100px;
       text-align: center;
   }
   
   .rWriter{ margin-right: 30px;}
   .rDate{
      font-size: 0.7em;
      color : gray;
   }
   
   #replyListArea{
      list-style-type: none;
   }
   
   .board-dateArea{
      font-size: 14px;
   }
   
   /* 이미지 선택 색 변경*/
   .carousel-indicators > li{
      background-color: #ccc !important;
   }
   
</style>
</head>
<body>
	<jsp:include page="../common/header.jsp"></jsp:include>
	<div class="container  my-5">

		<div>

			<div id="board-area">

				<!-- Category -->
				<h6 class="mt-4">카테고리 : [${board.categoryName}]</h6>
				
				<!-- Title -->
				<h3 class="mt-4">${board.boardTitle}</h3>

				<!-- Writer -->
				<p class="lead">
					작성자 : ${board.memberId}
				</p>

				<hr>

				<!-- Date -->
				<p>
					<span class="board-dateArea">
						작성일 : <fmt:formatDate value="${board.boardCreateDate}" pattern="yyyy년 MM월 dd일 HH:mm:ss" />
						<br>
						마지막 수정일 : <fmt:formatDate value="${board.boardModifyDate}" pattern="yyyy년 MM월 dd일 HH:mm:ss" />
					</span>
			 		<span class="float-right">조회수 ${board.readCount}</span>
				</p>

				<hr>
				<!-- 이미지 출력 -->
                <div class="carousel slide boardImgArea" id="board-image">
                <c:if test="${!empty fList}">
	               <!-- 이미지 선택 버튼 -->
	               <ol class="carousel-indicators">
	                  <c:forEach var="file" items="${fList}" varStatus="vs">
	                     
	                     <li data-slide-to="${vs.index }" data-target="#board-image"  
	                           <c:if test="${vs.first}"> class="active" </c:if> 
	                     >                      
	                     </li>
	                  
	                  </c:forEach>
	               </ol>
               
               
               <!-- 출력되는 이미지 -->
               <div class="carousel-inner">
                  <c:forEach var="file" items="${fList}" varStatus="vs">
                  
                     <div class="carousel-item <c:if test="${vs.first}">active</c:if>">
                     
                        <img class="d-block w-100 boardImg" id="${file.fileNo}" 
                           src="${contextPath}/resources/uploadImages/${file.fileName}">
                     </div>
                     
                  </c:forEach>
               
               </div> 
               
               <!-- 좌우 화살표 -->
               <a class="carousel-control-prev" href="#board-image" data-slide="prev"><span class="carousel-control-prev-icon"></span> <span class="sr-only">Previous</span></a> 
               <a class="carousel-control-next" href="#board-image" data-slide="next"><span class="carousel-control-next-icon"></span> <span class="sr-only">Next</span></a>
            </div>
            </c:if>
				
				
				
				
				
				
				
				
				
				
				
				
				
				

				<!-- Content -->
				<div id="board-content">${board.boardContent}</div>
				

				<hr>
				 
				
				<div>
				
					<%-- 로그인된 회원과 해당 글 작성자가 같은 경우--%>
					<c:if test="${!empty loginMember && (board.memberId) == loginMember.memberId }">
						<button id="deleteBtn" class="btn btn-primary float-right">삭제</button> 
						
						
						<%-- 
						
						검색 하지 않은 경우 : 상세조회 -> 수정 버튼 클릭 -> 수정 화면 -> 수정 성공 -> 상세 조회 ?
																	cp=1&no=505
																	
						검색한 경우 : 검색목록 -> 상세 조회 -> 수정 버튼 클릭 -> 수정 화면 -> 수정 성공 -> 상세 조회
												cp=1&no=505&sk=title&sv=파일 --%>
						
						
						<%-- 게시글 수정 후 상세조회 페이지로 돌아오기 위한 url 조합 --%>	
						
						<c:if test="${!empty param.sv && !empty param.sk}">
						 <%-- 검색을 통해 들어온 상세 조회 페이지인 경우 --%>
						
							<c:set var="searchStr" value="&sk=${param.sk}&sv=${param.sv}" />
						
						</c:if>										
						
						<a href="updateForm.do?cp=${param.cp}&no=${param.no}${searchStr}" class="btn btn-primary float-right ml-1 mr-1">수정</a>
					</c:if>
					
					<%--
						상대경로 작성법
						 - ../ : 현재 위치에서 한단계 상위(주소 제일 마지막 /보다 왼쪽으로 한칸 앞 /)
						 
						 http://localhost:8080/wsp/board/view.do 에서
						 http://localhost:8080/wsp/search.do 으로
						 
						 검색 -> 게시글 상세조회 -> 목록 -> 상세조회 전 페이지
					 --%>
					
					<c:choose>
						<c:when test="${!empty param.sk && !empty param.sv}">
							<c:url var="goToList" value="../search.do">
								<c:param name="cp">${param.cp}</c:param>
								<c:param name="sk">${param.sk}</c:param>
								<c:param name="sv">${param.sv}</c:param>
							</c:url>
						</c:when>
						
						<c:otherwise>
							<c:url var="goToList" value="/board/list.do">
								<c:param name="cp">${param.cp}</c:param>
							</c:url>
						</c:otherwise>
					</c:choose>
					
					
					<a href="${goToList}" class="btn btn-primary float-right">목록으로</a>
				</div>
				
				<jsp:include page="reply.jsp"></jsp:include>
				
			</div>



		</div>
	</div>
	<jsp:include page="../common/footer.jsp"></jsp:include>
	
	
	<script>
		// 삭제 버튼 이벤트
		$("#deleteBtn").on("click", function(){
			if(confirm("정말 삭제 하시겠습니까?")){
				location.href = "delete.do?no=${board.boardNo}";
			}
		});
		
		
	</script>
</body>
</html>













