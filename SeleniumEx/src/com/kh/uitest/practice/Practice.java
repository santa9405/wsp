package com.kh.uitest.practice;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Practice {
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", 
				"C:\\Users\\tnejd\\Desktop\\dev\\selenium/chromedriver.exe");
		
		WebDriver driver = new ChromeDriver();
		
		JavascriptExecutor exe = (JavascriptExecutor)driver;
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		driver.get("https://iei.or.kr/main/main.kh");
		
		exe.executeScript("javascript:location.href='/login/login.kh'");
		
		WebElement inputId = driver.findElement(By.id("id"));
		WebElement inputPw = driver.findElement(By.id("password"));
		
		inputId.sendKeys("tyty9336");
		inputPw.sendKeys("qyqy2400");
		exe.executeScript("fnLogin()");
		
		wait.until(ExpectedConditions.titleContains("KH정보교육원 - 수강생 평가"));
		
		exe.executeScript("javascript:location.href='/login/currBoard.kh'");
		
		wait.until(ExpectedConditions.titleContains("KH정보교육원 - 우리반 게시판"));
		
		exe.executeScript("fnSelect('19213')");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("comment_area")));
		
		WebElement inputComment = driver.findElement(By.id("comment_area"));
		inputComment.sendKeys("test");
		
		exe.executeScript("javascript:fnComment(19213)");
		
		driver.switchTo().alert().accept();
		
		//driver.close();
	}
}
