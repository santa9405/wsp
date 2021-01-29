<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<hr>
<h4>다중 조건 검색</h4>

<form action="${contextPath }/search2.do" method="GET" class="text-center" id="searchForm2" style="margin-bottom:100px;">
 	<span>
 		카테고리(다중 선택 가능)<br>
      <label for="exercise">운동</label> 
      <input type="checkbox" name="ct" value="운동" id="exercise">
      &nbsp;
      <label for="movie">영화</label> 
      <input type="checkbox" name="ct" value="영화" id="movie">
      &nbsp;
      <label for="music">음악</label> 
      <input type="checkbox" name="ct" value="음악" id="music">
      &nbsp;
      <label for="cooking">요리</label> 
      <input type="checkbox" name="ct" value="요리" id="cooking">
      &nbsp;
      <label for="game">게임</label> 
      <input type="checkbox" name="ct" value="게임" id="game">
      &nbsp;
      <label for="etc">기타</label> 
      <input type="checkbox" name="ct" value="기타" id="etc">
      &nbsp;
     </span>
     <br>
     <select name="sk" class="form-control" style="width:100px; display: inline-block;">
         <option value="title">글제목</option>
         <option value="content">내용</option>
         <option value="titcont">제목+내용</option>
         <option value="writer">작성자</option>
     </select>
     <input type="text" name="sv" class="form-control" style="width:25%; display: inline-block;">
     <button class="form-control btn btn-primary" style="width:100px; display: inline-block;">검색</button>
</form>
 
 
<script>
	/* $(function(){
		var searchKey = "${param.sk}";
		var searchValue = "${param.sv}";
		
		if(searchKey != "" && searchValue != ""){
			$.each($("select[name=sk] > option"), function(index, item){
				if($(item).val() == searchKey){
					$(item).prop("selected",true);
				} 
			});
			
			$("input[name=sv]").val(searchValue);
		}
	}); */
	
	<c:forEach var="c" items="${paramValues.ct}" varStatus="vs">
		$.each($("input[name=ct]"), function(index, item){
			if($(item).val() == "${c}"){
				$(item).prop("checked",true);
			} 
		});
		
	</c:forEach>
</script>
	
	