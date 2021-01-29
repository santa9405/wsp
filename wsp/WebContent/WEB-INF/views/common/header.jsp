<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>WebServer Project</title>

<!-- Bootstrap core CSS -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">

<!-- Bootstrap core JS -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>

<!-- sweetalert : alert창을 꾸밀 수 있게 해주는 라이브러리 https://sweetalert.js.org/ -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<style>
body {
	padding-top: 56px;
}
</style>

</head>

<body>
	<%--
		프로젝트의 시작주소(context root)를 얻어와 간단하게 사용할 수 있도록
		별도의 변수를 생성 
	--%>
	<c:set var="contextPath" scope="application" value="${pageContext.servletContext.contextPath}" />
	
	<%-- 
		로그인 실패 등의 서버로 부터 전달 받은 메세지를 경고창으로 출력하기
		
		1) 서버로 부터 전달받은 메세지가 있는지 검사
		
						↓ 세션에 텍스트가 있다면
	 --%>
	 <c:if test="${!empty sessionScope.swalTitle}">
	 	<script>
	 		swal({ icon : "${swalIcon}", title : "${swalTitle}", text : "${swalText}"});
	 	</script>
	 	
	 	<%-- 2) 한 번 출력한 메세지를 Session에서 삭제 --%>
	 	<c:remove var="swalIcon"/>
	 	<c:remove var="swalTitle"/>
	 	<c:remove var="swalText"/>
	 </c:if>

	<!-- Navigation으로 된 header -->
	<div class="header navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
		<div class="container">
			<a class="navbar-brand" href="${contextPath}">WebServer Project</a>
			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarResponsive">
				<ul class="navbar-nav ml-auto">
					<li class="nav-item"><a class="nav-link" href="${contextPath}/notice/list.do">Notice</a></li>
					<li class="nav-item"><a class="nav-link" href="${contextPath}/board/list.do">Board</a></li>

					<c:choose>
						<%-- 로그인이 되어있지 않을 때 == session에 loginMember라는 값이 없을 때 --%>
						<c:when test="${empty sessionScope.loginMember}">
						
							<!-- 헤더에 있는 login 버튼 클릭 시 #modal-container-1 이라는 아이디를 가진 요소를 보여지게 함. -->
							<li class="nav-item active">
								<a class="nav-link" data-toggle="modal" href="#modal-container-1">
									Login
								</a>
							</li>
						</c:when>
						
						<%-- 로그인이 되어 있을 때 --%>
						<c:otherwise>
							<li class="nav-item active">
								<%-- 로그인 회원의 이름을 가져와 출력 --%>
								<a class="nav-link" href="${contextPath}/member/myPage.do">${loginMember.memberName}</a>
							</li>
							<li class="nav-item active">
								<a class="nav-link" href="${contextPath}/member/logout.do">Logout</a>
							</li>
						</c:otherwise>
						
					</c:choose>
					

				</ul>
			</div>
		</div>
	</div>


	<%-- Modal창에 해당하는 html 코드는 페이지 마지막에 작성하는게 좋다 --%>
	<div class="modal fade" id="modal-container-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">

			<div class="modal-content">

				<div class="modal-header">
					<h5 class="modal-title" id="myModalLabel">로그인 모달창</h5>
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
				</div>

				<div class="modal-body">
					<form class="form-signin" method="POST" action="${contextPath}/member/Login.do">
												<!-- input 태그의 memberId, memberPwd, save의 파라미터가 위의 주소로 전달됨 -->

						<input type="text" class="form-control" id="memberId" name="memberId" 
						placeholder="아이디" value="${cookie.saveId.value}">
						<br>
						<input type="password" class="form-control" id="memberPwd" name="memberPwd" placeholder="비밀번호">
						<br>

						<div class="checkbox mb-3">
							<label> 
								<%-- cookie에 저장된 아이디가 있을 경우 checked 속성 추가 --%>
								<input type="checkbox" name="save" id="save" 
									<c:if test="${!empty cookie.saveId.value}">
										checked
									</c:if>
								> 아이디 저장
							</label>
						</div>

						<button class="btn btn-lg btn-primary btn-block" type="submit">로그인</button>
						<a class="btn btn-lg btn-secondary btn-block" href="${contextPath}/member/signUpForm.do">회원가입</a>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>


</body>

</html>
