package com.xl.distribute.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xl.distribute.common.Constants;

/**
 * 属性文件操作工具类。
 * 
 * @author tanzeng
 */
public final class PropertiesUtil {

	private static Log log = LogFactory.getLog(PropertiesUtil.class);
	
	/**
	 * Default constructor.
	 */
	private PropertiesUtil() {

	}

	/**
	 * 读取配置文件对应KEY的值
	 * 
	 * @param key
	 *            配置文件的KEY
	 * @return String 配置文件对应KEY的值
	 */
	public static String getAppConfig(String key) {

		return getParamValue(Constants.APPCONFIG_PATH, key);
	}

	/**
	 * 读取配置文件对应KEY的值，替换其中{0},{1}这样的参数为传入的参数 (从appconfig.properties文件中读取)
	 * 
	 * @param key
	 *            配置文件的KEY
	 * @param params
	 *            需要替换的值
	 * @return String 配置文件的对应key中，参数被动态植入后的值
	 */
	public static String getAppConfigWithParam(String key, String[] params) {

		String value = getParamValue(Constants.APPCONFIG_PATH, key);
		return MessageFormat.format(value, (Object[]) params);
	}

	private static String getParamValue(String path, String key) {

		try {
			ResourceBundle resource = ResourceBundle.getBundle(path);
			return resource.getString(key);
		} catch (Exception e) {
			log.error("读取文件[" + path + "]信息出错:" + e.getMessage() + "[" + key + "]");
			return Constants.BLANK_STRING;
		}
	}
}
