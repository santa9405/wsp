<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<style>
   #notice-area{ margin-bottom:200px;}
   #notice-content{ padding-bottom:150px;}
</style>
</head>
<body>
   <jsp:include page="../common/header.jsp"></jsp:include>

   <div class="container my-5">

      <h3>공지사항 수정</h3>
         <hr>
         <div class="bg-white rounded shadow-sm container py-3">
           <form method="POST" action="update.do?no=${param.no}" role="form" onsubmit="return noticeValidate();">
           
           
             <div class="form-inline mb-2">
               <div class="input-group">
                 <label class="input-group-addon mr-3">제목</label>
                 <input type="text" class="form-control" id="noticeTitle" name="noticeTitle" size="70" value="${notice.noticeTitle }">
               </div>
             </div>
   
             <div class="form-inline mb-2">
               <div class="input-group">
                 <label class="input-group-addon mr-3">작성자</label>
                 <h5 class="my-0" id="writer">${notice.memberId}</h5>
               </div>
             </div>
   
   
             <div class="form-inline mb-2">
               <div class="input-group">
                 <label class="input-group-addon mr-3">작성일</label>
                 <h5 class="my-0" id="today">${notice.noticeCreateDate}</h5>
               </div>
             </div>
   
             <hr>
   
             <div class="form-group">
               <div><label for="content">내용</label> </div>
               <textarea class="form-control" id="noticeContent" name="noticeContent" rows="10" 
               style="resize: none;">${notice.noticeContent}</textarea>
             </div>
   
   
           <hr class="mb-4">
   
          <div class="text-center">
               <button type="submit" class="btn btn-primary">수정</button>
               <a href="view.do?no=${param.no }" class="btn btn-primary">취소</a>
            </div>
           
      </form>
     </div>

   </div>
   <jsp:include page="../common/footer.jsp"></jsp:include>
   
   
   <script>
   // 유효성 검사
   function noticeValidate(){
      if( $("#noticeTitle").val().trim().length == 0){
         alert("제목을 입력해 주세요.");
         $("#title").focus();
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