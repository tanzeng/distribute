package com.xl.distribute.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

/**
 * 键盘操作工具类
 * 
 * @author tanzeng
 *
 */
public class KeyBoradUtil {

	/**
	 * copy字符串到剪贴板
	 * 
	 * @param str
	 */
	public static void copyToClipboard(String str){
		
		StringSelection stringSelection = new StringSelection(str);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}
	
	/**
	 * 利用Ctrl+V组件键进行粘贴操作
	 * 
	 */
	public static void paste(){
		
		Robot robot;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
