package com.kh.uitest.ex1;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Example1 {
   
    public static void main(String[] args) {
      
       // UI 테스트 종류
       // 정적 테스트 (수동) : 좁은 범위에 대해 테스트를 진행. 인력과 시간이 많이 소모됨.
       // 동적 테스트 (자동) : 넓은 범위에 대해 테스트를 진행. 인력과 시간이 적게 소모됨.
       
       // Selenium : UI 자동화 테스트 도구
       // -> 테스트, 매크로, 크롤링(화면 전체를 긁어오는, 자동으로 정보를 수집)
       
       // 크롬 드라이버 위치 지정 - 필수 코드
       System.setProperty("webdriver.chrome.driver", "C:\\Users\\tnejd\\Desktop\\dev\\selenium/chromedriver.exe");
       
       // 웹 드라이버 (크롬드라이버) 객체 생성
       // 객체가 생성됨과 동시에 브라우저가 실행됨.
       WebDriver driver = new ChromeDriver();
       
       // 브라우저에 주소 작성
       // - get("주소") : 해당 주소로 이동
       driver.get("http://www.naver.com");
       
       // 테스트 종료
       // - close() : 제어하던 브라우저를 종료
       //driver.close();
       // ** 테스트를 종료하는 close() 메소드는 원래 필수로 작성해야 하지만
       // 정상 동작 하는지 확인하기 위하여 실습 중에는 주석처리 하도록 한다.
   }
}