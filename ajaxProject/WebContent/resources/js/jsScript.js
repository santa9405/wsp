// 자바스크립트 방식의 Ajax

// XMLHttpRequest 객체 생성하기

// XMLHttpRequest 객체를 저장할 변수 선언
var xhr;

// 크로스 브라우저 대처 작업 진행
function getXMLHttpRequest(){
    // IE7 이상 이거나 또는 그 외 브라우저(크롬, 엣지, 웨일 등등)
    if(window.XMLHttpRequest){
        return new XMLHttpRequest();
    }

    // IE6 이하 버전
    else if(window.ActiveXObject){
        try{
            return new ActiveXObject("Microsoft.XMLHTTP");
        }catch(e){
            return null;
        }
    }

    // ajax를 지원하지 않는 브라우저
    else{
        return null;
    }
};


// contextPath를 구하는 함수
function getContextPath() {
    var hostIndex = location.href.indexOf( location.host ) + location.host.length;
    return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
  };

// 버튼 요소를 얻어와 저장
var jsBtn1 = document.getElementById("jsBtn1");
var jsBtn2 = document.getElementById("jsBtn2");

// 1. GET 방식으로 서버에 데이터 전송 및 응답 받기
jsBtn1.addEventListener("click", function(){

    // 1) XMLHttpRequest 객체 생성
    xhr = getXMLHttpRequest();

    // 2) onreadystatechange : Ajax 통신이 성공한 경우에 대한 동작
    xhr.onreadystatechange = function(){

        // Ajax 통신이 성공했는지 검사
        // 2-1) readyState : 서버 응답 상태 확인(Ajax 통신 상황 확인)
        if(xhr.readyState == 4){ // 요청 처리가 완료됐고 응답 준비가 되어있는가

            // 2-2) status : HTTP 응답 상태 코드(응답이 정상적으로 이루어 졌는가)
            if(xhr.status == 200){
                console.log("ajax 통신 성공!");

                // responseText : 응답 데이터 문자열 반환

                document.getElementById("result-area").innerHTML
                    = xhr.responseText;
            }
        }

    };

    // 3) open() : 서버와 데이터 교환 시 필요한 정보를 입력
    xhr.open("GET", "jsAjax1.do?name=홍길동&age=20", true);

    // 4) send() : 서버로 데이터 교환 요청
    xhr.send();
});

// 2. POST 방식으로 서버에 데이터 전송 및 응답
jsBtn2.addEventListener("click", function(){

    xhr = getXMLHttpRequest();

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){

                document.getElementById("result-area").innerHTML
                    = xhr.responseText;
            }
        }
    }

    // 3. open() : 서버와 데이터 교환 시 필요한 정보 입력
    xhr.open("POST", "jsAjax2.do", true);

    // * POST 방식 데이터 전송 시 send() 호출전 Mime type을 설정해줘야함
	xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded;');

    // 4. send() : 서버로 데이터 교환 요청
    // POST 방식 전송 시 send의 매개변수로 전달할 데이터를 쿼리스트링 형태로 작성
    xhr.send("pname=노트북&price=2000000");
});