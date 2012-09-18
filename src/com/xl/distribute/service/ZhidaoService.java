package com.xl.distribute.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.xl.distribute.dto.DistributeInfo;

public interface ZhidaoService {

	void submitAnswer(DistributeInfo distributeInfo);

	List<String> getQuestionPageUrl(String keyword, int page) throws UnsupportedEncodingException;
}
