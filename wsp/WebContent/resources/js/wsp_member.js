// 입력 값들이 유효성 검사가 진행되었는지 확인하기 위한 갹체 생성
var validateCheck = {
    "id" : false,
    "pwd1" : false,
    "pwd2" : false,
    "name" : false,
    "phone2" : false,
    "email" : false
}

// 아이디 유효성 검사
// 첫 글자는 영어 소문자, 나머지 글자는 영어 대,소문자 + 숫자, 총 6~12글자
$("#id").on("input",function(){
    var regExp = /^[a-z][a-zA-Z\d]{5,11}$/;

    var value = $("#id").val();
    if(!regExp.test(value) ){
        $("#checkId").text("유효하지 않은 아이디 형식입니다.").css("color","red");
        validateCheck.id = false;
    }else{
        // $("#checkId").text("유효한 아이디 형식입니다.").css("color","skyblue");
        
        // ajax를 이용한 실시간 아이디 중복 검사
        $.ajax({
            url :  "idDupCheck.do", // 상대 경로
            data : {"id" : value },
            type : "post",
            success : function(result){
                if(result == 0){ // 중복되지 않은 경우
                    $("#checkId").text("사용 가능한 아이디입니다.").css("color","skyblue");
                    validateCheck.id = true;
                }else{
                    $("#checkId").text("이미 사용중인 아이디입니다.").css("color","red");
                    validateCheck.id = false;
                }
            },
            error : function(){
                console.log("아이디 중복검사 실패");
            }
        });
    }

    // 팝업창으로 중복검사 시 사용
    // 아이디가 입력되는 경우 hidden 태그의 값을 false로 변경
    // $("#idDup").val("false"); 
});

// 이름 유효성 검사
// /^[가-힣]{2,}$/; // 한글 두 글자 이상
$("#name").on("input", function(){
    var regExp = /^[가-힣]{2,}$/;

    var value = $("#name").val();
    if(!regExp.test(value)){
        $("#checkName").text("이름 형식이 유효하지 않습니다.").css("color","red");
        validateCheck.name = false;
    }else{
        $("#checkName").text("유효한 이름 형식입니다.").css("color","skyblue");
        validateCheck.name = true;
    }
});

// 이메일 유효성 검사
// /^[\w]{4,}@[\w]+(\.[\w]+){1,3}$/; // 4글자 아무단어 @ 아무단어 . * 3
$("#email").on("input", function(){
    var regExp = /^[\w]{4,}@[\w]+(\.[\w]+){1,3}$/;

    var value = $("#email").val();
    if(!regExp.test(value)){
        $("#checkEmail").text("이메일 형식이 유효하지 않습니다.").css("color","red");
        validateCheck.email = false;
    }else{
        $("#checkEmail").text("유효한 이메일 형식입니다.").css("color","skyblue");
        validateCheck.email = true;
    }
});

//비밀번호 유효성 검사
//영어 대소문자 + 숫자, 총 6~12글자
//+ 비밀번호, 비밀번호 확인의 일치 여부
//+ 비밀번호를 입력하지 않거나 유효하지 않은 상태로
// 비밀번호 확인을 작성하는 경우

$("#pwd1, #pwd2").on("input", function(){
    // 비밀번호 유효성 검사
    var regExp = /^[a-zA-Z\d]{6,12}$/;

    var v1 = $("#pwd1").val();
    var v2 = $("#pwd2").val();

    if(!regExp.test(v1)){
        $("#checkPwd1").text("비밀번호 형식이 유효하지 않습니다.")
        .css("color", "red");
        validateCheck.pwd1 = false;
    } else{
        $("#checkPwd1").text("유효한 비밀번호 형식입니다.")
        .css("color", "skyblue");
        validateCheck.pwd1 = true;
    }


    // 비밀번호가 유효하지 않은 상태에서 비밀번호 확인 작성 시
    if(!validateCheck.pwd1 && v2.length > 0){
        swal("유효한 비밀번호를 먼저 작성해주세요.");
        $("#pwd2").val(""); // 비밀번호 확인에 입력한 값 삭제
        $("#pwd1").focus();
    }else {
        // 비밀번호, 비밀번호 확인의 일치 여부
       if(v1.length == 0 || v2.length == 0){
         $("#checkPwd2").html("&nbsp;");
      }else if(v1 == v2){
            $("#checkPwd2").text("비밀번호 일치")
            .css("color", "skyblue");
            validateCheck.pwd2 = true;
        }else{
            $("#checkPwd2").text("비밀번호 불일치")
            .css("color", "red");
            validateCheck.pwd2 = false;
        }
    }
});

// 전화번호 유효성 검사
$(".phone").on("input", function(){
    // 전화번호 input 태그에 4글자 초과 입력하지 못하게 하는 이벤트
    if ($(this).val().length > 4) {
        $(this).val( $(this).val().slice(0, 4));
    }

    var regExp1 = /^\d{3,4}$/;
    var regExp2 = /^\d{4}$/;

    var v1 = $("#phone2").val();
    var v2 = $("#phone3").val();

    if(!regExp1.test(v1) || !regExp2.test(v2)){
        $("#checkPhone").text("전화번호가 유효하지 않습니다.")
        .css("color", "red");
        validateCheck.phone2 = false;
    }else{
        $("#checkPhone").text("유효한 형식의 전화번호 입니다.")
        .css("color", "skyblue");
        validateCheck.phone2 = true;
    }

});

function validate(){
    // 아이디 중복 검사 여부 확인(팝업창)
    // if( $("#idDup").val() != "true" ){
    //     swql("아이디 중복 검사를 진행해 주세요.");
    //     $("#idDupCheck").focus();
    //     return false;
    // }

    // 유효성 검사 여부 확인
    for(var key in validateCheck){

        // key 값이 false가 나왔을 때
        if(!validateCheck[key]){

            var msg;
            switch(key){
                case "id" : msg="아이디가"; break;
                case "pwd1" : 
                case "pwd2" : msg="비밀번호가"; break;
                case "name" : msg="이름이"; break;
                case "phone2" : msg="전화번호가"; break;
                case "email" : msg="이메일이"; break;
            }

            swal(msg + " 유효하지 않습니다.");

            $("#"+key).focus();

            return false;
        }
    }
};

$(function(){
    $("#postcodify_search_button").postcodifyPopUp();
});

// 아이디 중복 체크창 오픈
$("#idDupCheck").on("click",function () {
	window.open("idDupForm.do", "idDupForm","width=450, height=250");
	 //			   팝업 주소	 팝업창 name	       설정
});


// 회원 정보 수정 ----------------------------------------------------------------------------------
// 회원 정보 수정 유효성 검사
function memberUpdateValidate(){
  
    // 유효성 검사를 저장할 객체 생성
    var updateCheck = {"phone2":false,
                        "email":false}

    var regExp1 = /^\d{3,4}$/; // 숫자 3~4 글자
    var regExp2 = /^\d{4,4}$/; // 숫자 4 글자
    var regExp3 = /^[\w]{4,}@[\w]+(\.[\w]+){1,3}$/;


    // 전화번호 유효성 검사
    var p2 = $("#phone2").val();
    var p3 = $("#phone3").val();
    if(!regExp1.test(p2) || !regExp2.test(p3)){
        updateCheck.phone2 = false;
    }else{
        updateCheck.phone2 = true;
    }

    // 이메일 유효성 검사
    if(!regExp3.test( $("#email").val() )){
        updateCheck.email = false;
    }else{
        updateCheck.email = true;
    }


    // updateCheck 내부에 저장된 값 검사
    for(var key in updateCheck){

        // 업데이트에서 가져온 값이 false 라면
        if(!updateCheck[key]){
            swal("일부 값이 유효하지 않습니다.");
            return false;
        }
    }
}


// 비밀번호 수정 ------------------------------------------------------------------------------------
function pwdValidate(){

    var regExp = /^[a-zA-Z\d]{6,12}$/; // 영어 대,소문자 + 숫자, 총 6~12글자

    if(!regExp.test( $("#newPwd1").val() ) ){
        swal("비밀번호 형식이 유효하지 않습니다.");
        $("#newPwd1").focus();

        return false; 
    }

    // 새로운 비밀번호와 확인이 일치하지 않을 때
    if( $("#newPwd1").val() != $("#newPwd2").val() ){
        swal("새로운 비밀번호가 일치하지 않습니다.");
        
        $("#newPwd1").focus(); // 포커스 이동
        $("#newPwd1").val(""); // newPwd1에 값 지우기 
        $("#newPwd2").val(""); // newPwd2에 값 지우기 

        return false;
    }
};


// 회원 탈퇴 약관 동의 체크 확인 --------------------------------------
function secessionValidate(){
    if(!$("#agree").prop("checked")){
        // #agree 체크박스가 체크되어 있지 않다면
        swal("약관에 동의해 주세요.");
        return false;
    }else{
        return confirm("정말로 탈퇴 하시겠습니까?");
        /* confirm : alert 창이 뜨면서 확인/취소 버튼이 있다.
          확인 누르면 true 반환, 취소 누르면 false 반환 */
    }
};