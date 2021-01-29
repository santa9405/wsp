<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>아이디 중복 검사</title>
</head>
<style>
	body>*{
		margin-left: 75px;
	}
</style>

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<body>
	<h4 class="mt-3">아이디 중복 검사</h4>
	<br>
	
	<form action="${contextPath }/member/idDupCheck.do" id="idChekcForm" method="post">
		<input type="text" id="id" name="id">
		<input type="submit" value="중복확인">
	</form>
	<br>
	
	<!-- 사용 가능 여부 메세지 출력 -->
	<span>
	<c:if test="${!empty result }">
		<c:choose>
			<c:when test="${result == 0 }">사용 가능한 아이디입니다.</c:when>
			<c:otherwise>이미 사용중인 아이디입니다.</c:otherwise>
		</c:choose>
	</c:if>
	</span>
	<br>
	<br>
	
	<div>
		<input type="button" id="cancel" value="취소" onclick="window.close();">
		<input type="button" id="confirmId" value="확인" onclick="confirmId();">
	</div>
	
	<script type="text/javascript">
	
		var id;
		var result;
		
		// 팝업창이 오픈 완료 된 후 자동으로 실행
		$(function(){
			result = "${result}";
			
			console.log(result);
			
			// 팝업창 최초 오픈 시 if문이 동작되고 중복 체크 버튼으로 인한 팝업창 재 요청 시 else문이 실행됨. 
			if(id == null && result == ""){
				id = opener.document.signUpForm.id.value; // 부모창의 아이디 저장
									// form 태그의 name이 id인 input 태그의 value
			}else{
				// 중복 체크 후 아이디 저장
				id = "${param.id}"; 
			}
			
			console.log(id);
			// 부모창에서 입력한 아이디 또는 중복검사를 진행한 아이디를 화면에 표시
			//document.getElementById("id").value = id;
			
			$("#id").val(id);
		});
		
		
		// 확인버튼을 눌렀을 경우 부모창에 전달할 값 제어
		function confirmId(){ 
			
			// 중복체크 결과 중복되는 아이디가 없을 경우(result가 0인 경우)
			if(result == 0){
				// 부모창 문서 내에서 signUpForm 이라는 이름의 태그 내에 id라는 이름을 가진 태그의 value값을
				// 현재 태그 중 id가 "id" 요소의 값을 대입.
				opener.document.signUpForm.id.value = $("#id").val();
				
				// 부모창에 type이 hidden인 요소의 값을 true로 변경
				opener.document.signUpForm.idDup.value = true;
			}else{
				
				// 중복인 상태로 확인을 누른 경우 부모창에 type이 hidden인 요소의 값을 false로 변경
				opener.document.singUpCheck.signUpForm.idDupCheck = false;
			}
		
			if(opener != null){ // 아이디 중복창 닫기
				opener.checkForm = null;
				self.close();
			}
		}
		
		$("#idChekcForm").submit(function(){
			var regExp = /^[a-z][a-zA-z\d]{5,11}$/;
			if(!regExp.test($("#id").val())){
				alert("유효하지 않은 아이디 형식 입니다.");
				return false;
      }
		});
		
		
	</script>
</body>
</html>