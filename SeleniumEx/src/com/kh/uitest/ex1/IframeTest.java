package com.kh.uitest.ex1;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class IframeTest {
	
	public static void main(String[] args) {
		
		// 크롬 드라이버 위치 지정
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\tnejd\\Desktop\\dev\\selenium/chromedriver.exe");
		
		// 웹 드라이버 객체 생성
		WebDriver driver = new ChromeDriver();
		
		// 링크 이동
		driver.get("http://aclass.xyz:8081/Selenium/exam1/iframeTest.html");
		
		// * iframe 같은 페이지 내 다른 페이지를 접근하는 방법
		// WebDriver.switchTo().frame("이동할 요소의 name속성값");
		driver.switchTo().frame("subIframe");
		
		// 아이디, 비밀번호 input 태그를 얻어와
		// "test" 라는 값을 작성
		WebElement inputId = driver.findElement(By.id("id"));
		WebElement inputPw = driver.findElement(By.id("password"));
		inputId.sendKeys("test");
		inputPw.sendKeys("test");
		
		// 태그를 이용한 로그인 버튼 선택, 클릭
		//List<WebElement> btns = driver.findElements(By.tagName("button"));
		
		//btns.get(0).click();
		
		// *** 자바스크립트 함수를 호출하여 로그인 수행
		JavascriptExecutor exe = (JavascriptExecutor)driver;
		exe.executeScript("fnLogin()");
		
		// iframe으로 맞춰진 포커스를 다시 원래대로 변환
		driver.switchTo().parentFrame();
		
		// 화면에 있는 p태그 모두 선택
		List<WebElement> pList = driver.findElements(By.tagName("p"));
		
		for(WebElement p : pList) {
			System.out.println(p.getText()); // 크롤링 기초
		}
		
		// 테스트 종료
		//driver.close();
	}
}
