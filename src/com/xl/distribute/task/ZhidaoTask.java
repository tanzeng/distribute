package com.xl.distribute.task;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xl.distribute.common.Constants;
import com.xl.distribute.dto.DistributeInfo;
import com.xl.distribute.dto.User;
import com.xl.distribute.service.ZhidaoService;
import com.xl.distribute.util.PropertiesUtil;

@Component
public class ZhidaoTask implements Task {

	@Autowired
	private ZhidaoService zhidaoService;

	private static Log log = LogFactory.getLog(Constants.SYS_LOGGER_TASK);

	@Override
	public void execute() {

		DistributeInfo distributeInfo = new DistributeInfo();
		
		
		try {
			int page = Integer.valueOf(PropertiesUtil.getAppConfig("task.search.page"));
			
			for(int i = 0; i < page; i++){
			
				List<String> urlList = zhidaoService.getQuestionPageUrl(PropertiesUtil.getAppConfig("task.search.keyword"), page);
				
				for(String url : urlList){
					
					log.info("------------------开始回答问题");
					
					User user = new User();
					user.setName("windgone");
					user.setPwd("hack119");
					
					distributeInfo.setPageUrl(url);
					distributeInfo.setPicPath(PropertiesUtil.getAppConfig("task.distribute.pic.path"));
					distributeInfo.setProxyServer(PropertiesUtil.getAppConfig("webdriver.proxy"));
					distributeInfo.setText(PropertiesUtil.getAppConfig("task.distribute.text"));
					distributeInfo.setUser(user);
					
					zhidaoService.submitAnswer(distributeInfo);
					
					log.info("------------------结束回答问题");
				}
			}
			
		} catch (Exception e) {
			
			log.error("执行百度知道回答问题出错", e);
		}
	}
}
