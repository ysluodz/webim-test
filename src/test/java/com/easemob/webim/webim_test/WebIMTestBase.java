package com.easemob.webim.webim_test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.google.common.base.Preconditions;

public class WebIMTestBase {
	private static final Logger logger = LoggerFactory.getLogger(WebIMTestBase.class);

	protected WebDriver driver;
	protected String baseUrl = "http://webim.easemob.com/";
	protected String screenshotPath = "target";
	protected String screenshotSuffix = "png";

	public void login(WebDriver driver, String username, String password) {
		Preconditions.checkArgument(null != driver, "webdriver was missing");
		Preconditions.checkArgument(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password),
				"username or password was missing!");
		String path = screenshotPath + "/" + Thread.currentThread().getStackTrace()[1].getMethodName();
		driver.get(baseUrl);
		driver.manage().window().maximize();
		sleep(5);
		logger.info("find username box and input username: {}", username);
		String xpath = "//input[@id='username']";
		WebElement usernameInput = findElementByXpath(driver, xpath);
		if (null == usernameInput) {
			screenshot(driver, getPath(path));
		}
		Assert.assertNotNull(usernameInput);
		usernameInput.sendKeys(username);

		logger.info("find password box and input password: {}", password);
		xpath = "//input[@id='password']";
		WebElement passwordInput = findElementByXpath(driver, xpath);
		if (null == passwordInput) {
			screenshot(driver, getPath(path));
		}
		Assert.assertNotNull(passwordInput);
		passwordInput.sendKeys(password);

		logger.info("click login button");
		xpath = "//div[@id='loginmodal']/div[3]/button[1]";
		WebElement login = findElementByXpath(driver, xpath);
		if (null == login) {
			screenshot(driver, getPath(path));
		}
		Assert.assertNotNull(login);
		login.click();
		sleep(10);

		logger.info("check if login webim successfully");
		xpath = "//a[@id='accordion1']";
		WebElement ele = findElementByXpath(driver, xpath);
		if (null == ele) {
			screenshot(driver, getPath(path));
		}
		Assert.assertNotNull(ele);
	}

	public String getPath(String path) {
		return path + "_" + System.currentTimeMillis() + "." + screenshotSuffix;
	}

	public void sleep(int seconds) {
		logger.info("Start to sleep {} seconds...", seconds);
		try {
			Thread.currentThread().sleep(seconds * 1000L);
		} catch (InterruptedException e) {
			logger.error("Failed to sleep {} seconds", seconds);
		}
	}

	public void screenshot(WebDriver driver, String path) {
		Preconditions.checkArgument(StringUtils.isNotBlank(path), "screenshot file path was missing!");
		try {
			TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
			File srcFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFile, new File(path));
		} catch (Exception e) {
			logger.error("Failed to get screenshot: path[{}]", path, e);
		}
	}

	public WebElement findElementByXpath(WebDriver driver, String xpath) {
		WebElement element = null;
		try {
			element = driver.findElement(By.xpath(xpath));
		} catch (Exception e) {
			logger.error("Failed to find element: xpath[{}]", xpath, e);
			return null;
		}
		return element;
	}

	public WebElement findElement(WebDriver driver, String xpath, String path) {
		WebElement element = findElementByXpath(driver, xpath);
		if (null == element) {
			screenshot(driver, getPath(path));
		}
		Assert.assertNotNull(element);
		return element;
	}

	public String getRandomStr(int count) {
		return RandomStringUtils.randomAlphanumeric(count);
	}

	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new FirefoxDriver();
		// WebDriver driver = new SafariDriver();

		String baseUrl = "http://webim.easemob.com";
		// driver.navigate().to(baseUrl);
		driver.get(baseUrl);
		driver.manage().window().maximize();
		logger.info("window title1: " + driver.getTitle() + ", handle = " + driver.getWindowHandle());
		Thread.currentThread().sleep(3000L);
		WebDriver driver2 = new FirefoxDriver();
		driver2.get(baseUrl);
		logger.info("window title2: " + driver.getTitle() + ", handle = " + driver.getWindowHandle());
		Thread.currentThread().sleep(3000L);
		driver.close();
		driver2.close();
	}
}
