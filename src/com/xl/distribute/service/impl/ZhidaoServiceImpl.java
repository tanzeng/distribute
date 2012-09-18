package com.xl.distribute.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Service;

import com.xl.distribute.common.Constants;
import com.xl.distribute.dto.DistributeInfo;
import com.xl.distribute.service.ZhidaoService;
import com.xl.distribute.util.KeyBoradUtil;
import com.xl.distribute.util.TreadUtil;

@Service
public class ZhidaoServiceImpl implements ZhidaoService {

	private static Log log = LogFactory.getLog(Constants.SYS_LOGGER_SERVICE);

	@Override
	public void submitAnswer(DistributeInfo distributeInfo) {

		Proxy proxy = new Proxy();  
		proxy.setHttpProxy(distributeInfo.getProxyServer()).setFtpProxy(distributeInfo.getProxyServer()).setSslProxy(distributeInfo.getProxyServer());  
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(CapabilityType.PROXY, proxy);
        		
		WebDriver driver = new FirefoxDriver(cap);

		TreadUtil.sleep(1000);

		driver.get(distributeInfo.getPageUrl());
       
		// 进行登录
		WebElement username = driver.findElement(By.id("pass_loginLite_input_username"));
		username.sendKeys(distributeInfo.getUser().getName());

		WebElement pwd = driver.findElement(By.id("pass_loginLite_input_password"));
		pwd.sendKeys(distributeInfo.getUser().getPwd());

		WebElement btn_login = driver.findElement(By.id("pass_loginLite_input_submit"));
		btn_login.click();

		// 等待登录成功
		boolean isLoginSuccess = false;
		long loginDelaySecond = System.currentTimeMillis() + 20000;
		while (System.currentTimeMillis() < loginDelaySecond) {
			WebElement signInBtn = null;
			try {
				TreadUtil.sleep(500);
				signInBtn = driver.findElement(By.id("signInBtn"));
			} catch (NoSuchElementException nsee) {
				log.debug("正在登录......");
			} finally {
				if (signInBtn != null) {
					log.debug("登录成功......");
					isLoginSuccess = true;
					break;
				}
			}
		}

		if (!isLoginSuccess) {
			log.debug("登录失败");
			driver.close();
			return;
		}

		// 打开富文本编辑器
		WebElement openEditorLink = null;
		try {
			openEditorLink = driver.findElement(By.id("question-box")).findElement(By.xpath("div/div/span"));
		} catch (Exception e) {
			log.debug("无法进行回答操作");
			driver.close();
			return;
		}
		openEditorLink.click();

		// 上传图片
		KeyBoradUtil.copyToClipboard(distributeInfo.getPicPath());

		WebElement openFileBtn = driver.findElement(By.id("edui3"));
		openFileBtn.click();

		TreadUtil.sleep(1000);

		KeyBoradUtil.paste();

		// 等待上传图片完成
		boolean isUploadSuccess = false;
		long uploadDelaySecond = System.currentTimeMillis() + 300000;
		WebDriver eidtorFrameDriver = driver.switchTo().frame(driver.findElement(By.id("baidu_editor_0")));
		while (System.currentTimeMillis() < uploadDelaySecond) {

			try {
				TreadUtil.sleep(1000);
				log.debug("上传图片中......");
				eidtorFrameDriver.findElement(By
						.xpath("/html/body/p/img[@src='http://img.baidu.com/img/iknow/editor/uploading.gif']"));
			} catch (NoSuchElementException nsee) {
				log.debug("上传图片成功......");
				isUploadSuccess = true;
				break;
			}
		}

		if (!isUploadSuccess) {
			log.debug("上传图片超时.....");
			driver.close();
			return;
		}

		TreadUtil.sleep(1000);

		// 提交回答
		String currentUrl = driver.getCurrentUrl();
		WebElement submitBtn = driver.switchTo().defaultContent().findElement(By.tagName("button"));
		submitBtn.click();

		// 等待提交完成
		long submitDelaySecond = System.currentTimeMillis() + 60000;
		while (System.currentTimeMillis() < submitDelaySecond) {

			TreadUtil.sleep(500);
			log.debug("提交中......");
			if (!currentUrl.equals(driver.getCurrentUrl())) {
				log.debug("提交完成......");
				break;
			}
		}

		driver.close();
	}

	@Override
	public List<String> getQuestionPageUrl(String keyword, int page) throws UnsupportedEncodingException {
		
		List<String> urlList = new ArrayList<String>();
		
		WebDriver driver = new HtmlUnitDriver();
		
		driver.get("http://zhidao.baidu.com/search?word=" + URLEncoder.encode(keyword, "GBK") + "&lm=0&rn=10&ie=gbk&pn=" + page + "0");

		List<WebElement> searchResults = driver.findElements(By.tagName("dt"));
		
		String url = "";
		for(WebElement searchResult : searchResults){
			
			url = searchResult.findElement(By.tagName("a")).getAttribute("href");
			urlList.add(url);
		}
		
		driver.close();
		
		return urlList;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		ZhidaoServiceImpl f = new ZhidaoServiceImpl();
		
		f.getQuestionPageUrl("孕妇", 4);

	}
}
