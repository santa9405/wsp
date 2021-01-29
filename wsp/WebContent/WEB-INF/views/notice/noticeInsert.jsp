<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<style>
	#notice-area{ height: 700px;}
</style>
</head>
<body>
  <jsp:include page="../common/header.jsp"></jsp:include>
	<div class="container my-5">


			<h3>공지사항 등록</h3>
		      <hr>
		      <div class="bg-white rounded shadow-sm container py-3" id="notice-area">
		        <form method="POST" action="insert.do" role="form" onsubmit="return noticeValidate();">
		          <div class="form-inline mb-2">
		            <div class="input-group">
		              <label class="input-group-addon mr-3">제목</label>
		              <input type="text" class="form-control" id="noticeTitle" name="noticeTitle" size="70">
		            </div>
		          </div>
		
		          <div class="form-inline mb-2">
		            <div class="input-group">
		              <label class="input-group-addon mr-3">작성자</label>
		              <h5 class="my-0">${loginMember.memberId}</h5>
		            </div>
		          </div>
		
		
		          <div class="form-inline mb-2">
		            <div class="input-group">
		              <label class="input-group-addon mr-3">작성일</label>
		              <h5 class="my-0" id="today"></h5>
		            </div>
		          </div>
		
		          <hr>
		
		          <div class="form-group">
		            <div><label for="content">내용</label> </div>
		            <textarea class="form-control" id="noticeContent" name="noticeContent" rows="15" style="resize: none;"></textarea>
		          </div>
		
		
		        <hr class="mb-4">
		
		        <div class="text-center">
					<button type="submit" class="btn btn-primary">등록</button>
					<a href="list" class="btn btn-primary">목록으로</a>
				</div>
		        
		        </form>
		      </div>

	</div>
	<jsp:include page="../common/footer.jsp"></jsp:include>
	
	<script>
		(function printToday(){
			// 오늘 날짜 출력 
			
			var now = new Date(); // 현재 시간
			var year = now.getFullYear(); // 2021
			var month = now.getMonth() + 1; // getMonth() : 0 ~ 11 중 하나가 반환, 0
			var date = now.getDate(); // 4
			
			var str = year + "-" 
						+ (month < 10 ? "0" + month : month) + "-" 
						+ (date < 10 ? "0" + date : date);
			
			$("#today").text(str);
	
		
		})();
		
		
		// 유효성 검사
		function noticeValidate(){
			if( $("#noticeTitle").val().trim().length == 0){
				alert("제목을 입력해 주세요.");
				$("#noticeTitle").focus();
				return false;
			}
			
			if( $("#noticeContent").val().trim().length == 0){
				alert("내용을 입력해 주세요.");
				$("#content").focus();
				return false;
			}
		}
		
	</script>
</body>
</html>
