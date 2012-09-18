package com.xl.distribute.task;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xl.distribute.common.Constants;
import com.xl.distribute.util.PropertiesUtil;

public class StartUp {

	public static void main(String[] args) {

		new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml",
				"applicationContext-scheduledTask.xml" });
		Log taskLogger = LogFactory.getLog(Constants.SYS_LOGGER_TASK);
		
		System.setProperty("webdriver.firefox.bin", PropertiesUtil.getAppConfig("webdriver.firefox.bin"));
		
		try {
			Runtime.getRuntime().exec("cmd /c " + PropertiesUtil.getAppConfig("webdriver.proxy.goagent"));
		} catch (IOException e) {
			taskLogger.error("启动goagent失败", e);
		}
		
		taskLogger.info("distribute component is starting");
	}
}
