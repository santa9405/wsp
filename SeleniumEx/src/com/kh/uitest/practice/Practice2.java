package com.kh.uitest.practice;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Practice2 {
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
		
		exe.executeScript("fnWriteForm()");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("title")));
		
		WebElement inputTitle = driver.findElement(By.id("title"));
		inputTitle.sendKeys("test");
		
		driver.switchTo().frame("tx_canvas_wysiwyg");

		WebElement inputContent = driver.findElement(By.className("tx-content-container"));
		inputContent.sendKeys("test");
		
		driver.switchTo().parentFrame();
		
		exe.executeScript("fnRegister()");
		
		driver.switchTo().alert().accept();
		
		//driver.close();
	}
}
