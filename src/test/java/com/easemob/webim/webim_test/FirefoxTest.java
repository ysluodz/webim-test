package com.easemob.webim.webim_test;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Preconditions;

public class FirefoxTest extends WebIMTestBase {
	private static final Logger logger = LoggerFactory.getLogger(FirefoxTest.class);

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		logger.info("Start to webim auto test on firefox...");
		driver = new FirefoxDriver();
	}

	@Test(dependsOnMethods = { "register" })
	public void login() {
		String username = "zhouhuhu";
		String password = "qQ123456";
		super.login(driver, username, password);
	}

	@Test
	public void register() {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		String path = screenshotPath + "/" + Thread.currentThread().getStackTrace()[1].getMethodName();
		String username = getRandomStr(8);
		String password = getRandomStr(8);
		String nickname = getRandomStr(8);
		logger.info("generate random username: {}, password: {}, nickname: {}", username, password, nickname);
		driver.get(baseUrl);
		driver.manage().window().maximize();
		sleep(5);
		String xpath = "//button[@class='flatbtn-blu'][@tabindex='4']";
		WebElement reg = findElement(driver, xpath, path);
		reg.click();
		sleep(5);
		xpath = "//input[@id='regist_username']";
		WebElement ele = findElement(driver, xpath, path);
		ele.sendKeys(username);

		xpath = "//input[@id='regist_password']";
		ele = findElement(driver, xpath, path);
		ele.sendKeys(password);

		xpath = "//input[@id='regist_nickname']";
		ele = findElement(driver, xpath, path);
		ele.sendKeys(nickname);

		logger.info("click ok button");
		xpath = "//button[@id='confirm-regist-confirmButton']";
		ele = findElement(driver, xpath, path);
		ele.click();
		sleep(10);

		Alert alert = driver.switchTo().alert();
		;
		String text = alert.getText();
		Assert.assertTrue(text.contains("注册成功"), "alert should indecate successful register");
		;
		alert.accept();
		sleep(3);
		driver.quit();
		driver = new FirefoxDriver();
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		if (null != driver) {
			driver.quit();
		}
	}
}
