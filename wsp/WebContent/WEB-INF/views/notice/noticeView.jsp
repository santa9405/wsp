<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<style>
	#notice-area{ height: 700px;}
	#notice-content{ padding-bottom:150px;}
	
</style>
</head>
<body>
	<jsp:include page="../common/header.jsp"></jsp:include>
	<div class="container my-5">

		<div id="notice-area">

			<!-- Title -->
			<h1 class="mt-4">${notice.noticeTitle}</h1>

			<!-- Writer -->
			<p class="lead">
				작성자 : <a class="idSelect">${notice.memberId}</a>
			</p>

			<hr>

			<!-- Date -->
			<p>
				 ${notice.noticeCreateDate}
				 <span class="float-right">조회수 ${notice.readCount}</span>
			</p>

			<hr>


			<!-- Content -->
			<div id="notice-content">${notice.noticeContent}</div>
			
			<hr>
			
			<div>
	
				<%-- 로그인된 회원이 해당 글의 작성자인 경우 --%>
			<c:if test="${ !empty loginMember && (loginMember.memberId == notice.memberId) }">
				<button class="btn btn-primary float-right" id="deleteBtn">삭제</button>
				<a href="updateForm.do?no=${param.no}" class="btn btn-primary float-right ml-1 mr-1">수정</a>
				<%-- request 파라미터 중 no가 존재하고 있음 --%>
			</c:if>	
				<a href="list.do" class="btn btn-primary float-right">목록으로</a>
			</div>
			
		</div>

	</div>
	<jsp:include page="../common/footer.jsp"></jsp:include>
	
	
	
	<script>
	$("#deleteBtn").on("click", function(){
		
		if( confirm("정말 삭제 하시겠습니까?") ){
			
			location.href = "delete.do?no=${param.no}";
			
		}
		
	});
		
			
	</script>
	
</body>
</html>
