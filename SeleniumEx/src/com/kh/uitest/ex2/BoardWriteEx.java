package com.kh.uitest.ex2;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BoardWriteEx {
	public static void main(String[] args) {
		// 크롬 드라이버 위치 지정(필수)
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\tnejd\\Desktop\\dev\\selenium/chromedriver.exe");
		
		// 드라이버 객체 생성(필수)
		WebDriver driver = new ChromeDriver();
		
		// 자바스크립트 함수를 호출할 수 있도록 준비
		JavascriptExecutor exe = (JavascriptExecutor)driver;
		
		// 명시적 대기 객체 생성
		WebDriverWait wait = new WebDriverWait(driver, 10);
		// 웹 드라이버를 10초 동안 기다리게 함.
		
		// ***** 명시적 대기(Explicitly Wait)
		// 명시적 대기명령으로 조금 더 상세하게 특정 Element가 특정 상태가 되는 것을 감지할 때까지 대기시키는 명령이다.
		
		// 명시적 대기 명령으로 삼을 수 있는 조건
		// 1. 특정 Element가 클릭 가능할 때까지 대기.
		// 2. 페이지 제목이 특정 문자열이 될 때 까지 대기.
		// 3. alert창이 뜰때까지 대기.
		
		// ***** 묵시적(암시적) 대기(implicitlyWait)
		// - 웹 드라이버가 발생시키는 예외들에 대하여 일정 시간동안 재시도하며 대기시키는 명령
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		// 테스트 중 오류 발생 시 5초 동안 재시도

		// 지정된 주소로 이동
		driver.get("http://aclass.xyz:8081/Selenium/exam3/loginInput.html");
		
		// 아이디, 비밀번호 웹 요소 얻어오기
		WebElement inputId = driver.findElement(By.id("id"));
		WebElement inputPw = driver.findElement(By.id("password"));
		
		// 아이디, 비밀번호 입력 후 로그인
		// 단, 로그인은 자바스크립트 함수를 호출하여 수행
		inputId.sendKeys("test");
		inputPw.sendKeys("test");
		exe.executeScript("fnLogin()"); // 로그인 함수 호출
		
		// ** 웹 페이지 제목에 "훈련생도 평가" 문자열이 포함될 때 까지 명시적 대기
		wait.until(ExpectedConditions.titleContains("훈련생도 평가"));
		
		// ** 자바스크립트 코드를 이용하여 게시판 화면으로 이동
		exe.executeScript("javascript:location.href='currBoard.html'");
		
		// ** 웹 페이지 제목에 "우리반 게시판" 문자열이 포함될 때 까지 명시적 대기
		wait.until(ExpectedConditions.titleContains("우리반 게시판"));
		
		// "게시판 글쓰기 시작" 버튼에 있는 함수 수행
		exe.executeScript("fnWriteForm()");
		
		// 글 제목 입력창이 나타날 때 까지 명시적 대기(아이디가 title인 요소가 클릭이 될 때까지 대기)
		wait.until(ExpectedConditions.elementToBeClickable(By.id("title")));
		
		// 글 제목 요소를 얻어와 "안녕하세요." 작성하기
		WebElement inputTitle = driver.findElement(By.id("title"));
		inputTitle.sendKeys("안녕하세요.");
		
		// 글 내용을 감싸고 있는 iframe으로 포커스 이동
		driver.switchTo().frame("iframeMsg");
		
		// 글 내용 요소를 얻어와 "반갑습니다." 작성하기
		WebElement textArea = driver.findElement(By.id("areaMsg"));
		textArea.sendKeys("반갑습니다.");
		
		// 부모 프레임으로 포커스 변경
		driver.switchTo().parentFrame();
		
		// 등록 버튼에 있는 함수 수행
	    exe.executeScript("javascript:fnRegister()");
	      
	    // ** alert창으로 포커스를 옮겨 확인 버튼 누르기
	    driver.switchTo().alert().accept();
		
		// 테스트 종료(필수)
		//driver.close();
	}
}
