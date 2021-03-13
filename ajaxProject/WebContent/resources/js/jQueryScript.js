// 1. 버튼 클릭 시 GET 방식으로 서버에 입력값 전달 및 응답
$("#jqBtn1").on("click", function(){

    $.ajax({
        // url : 요청을 보낼 주소(필수!!)
        url : "jqTest1.do",
        type : "GET", // type : 데이터 전송 방식
        data : { "input" : $("#input1").val() }, // data : 전달할 데이터
        success : function(result){
            // success : ajax 통신 성공 시 동작(try)
            // 매개변수 result : 서버 응답한 데이터
            
            $("#result-area").html(result);
        },

        error : function(){
            // error : ajax 통신 실패 시 동작(catch)
            console.log("ajax 통신 실패");
        },

        complete : function(){
            // complete : 통신 성공 여부와 관계 없이 수행(finally)
            console.log("통신 성공 여부 관계 없이 수행됨.");
        }

    });
});

// 2. 버튼 클릭시 POST 방식으로 서버에 데이터 전송 및 응답
$("#jqBtn2").on("click", function(){

    $.ajax({

        url : "jqTest2.do", // 요청 주소 (**필수**)
        data : {"input" : $("#input2").val()} , // 요청 시 전달할 값 (#input2 값을 input에 보내겠다.)
        type : "post", // 데이터 전달 방식 (GET/POST)
        dataType : "text", // dataType : 응답 데이터의 형식을 지정해주는 속성. (Type 꼭 대문자로 해야 인식됨)
                                // -> 미작성시 어느정도 자동으로 판별하여 지정됨.
        
        success : function(result){ // 통신 성공 시 (result : 응답데이터. 이름은 마음대로)
            $("#result-area").html(result);
        },

        error : function(){
            console.log("ajax 통신 실패");
        }
    });

});

// 실시간 아이디 중복 검사
$("#inputId").on("input", function(){

    $.ajax({
        url : "member/idDupCheck.do",
        data : { "inputId" : $("#inputId").val() },
        type : "GET",
        success : function(result){
            if(result == 0){ // 동등 비교
                $("#idDupResult").text("사용 가능한 아이디 입니다.");
            }else{
                $("#idDupResult").text("이미 사용 중인 아이디 입니다.");
            }
        },

        error : function(request, status, error){
            console.log("ajax 통신 오류");
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }

    });

});



// 3. 서버로 기본형 데이터 전송 후, 응답을 객체(Object)로 받기
$("#jqBtn3").on("click", function(){

    $.ajax({
        url : "jqTest3.do",
        data : { "input" : $("#input3").val() },
        type : "get",
        dataType : "json", // 응답 데이터가 JSON 형태임을 인식 시키는 방법3(jQuery로 ajax를 사용할 때만 가능)
        success : function(user){
            // 데이터가 넘어올 때는 text로 넘어옴 
            // dataType : "json" 을 통해 json 언어로 인식

            // 응답 데이터가 JSON 형태임을 인식 시키는 방법2
            // user = JSON.parse(user);
            console.log(user);

            var result = "";

            if(user != null){
                result = "번호 : " + user.no + "<br>"
                        + "이름 : " + user.name + "<br>"
                        + "나이 : " + user.age + "<br>"
                        + "성별 : " + user.gender;
            }else{
                result = "일치하는 사용자가 없습니다.";
            }

            $("#result-area").html(result);
        },

        error : function(){
            console.log("ajax 통신 실패");
        }

    });

});



// 실시간 회원 정보 조회
$("#selectMemberBtn").on("click", function(){

    $.ajax({
        url : "member/selectMember.do",
        data : {"inputId" : $("#inputId2").val() },
        type : "get", // 검색은 대부분 get 방식
        dataType : "json",
        success : function(member){
            console.log(member);

            if(member != null){
                var result = "";

                result += "<h4>" + member.memberId + "</h4>";
                result += "이름 : " + member.memberName + "<br>";
                result += "이메일 : " + member.memberEmail + "<br>";
                result += "관심 분야 : " + member.memberInterest + "<br>";
                result += "가입일 : " + member.memberEnrollDate + "<br>";

                $("#result-area").html(result);
            }
        },

        error : function(){
            console.log("회원 정보 조회 중 문제 발생");
        }
    });
});