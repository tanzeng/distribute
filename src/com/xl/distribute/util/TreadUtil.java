package com.xl.distribute.util;

public class TreadUtil {

	public static void sleep(long millis){
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
