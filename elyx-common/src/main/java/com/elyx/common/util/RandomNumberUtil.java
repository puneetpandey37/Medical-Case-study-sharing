package com.elyx.common.util;

import java.util.Random;

public class RandomNumberUtil {

	public static int getRandomNumber() {
		
		Random random = new Random();
		
		Float floatVal = random.nextFloat();
		int randomNumber = (int) (100000 + floatVal * 900000);	
		return randomNumber;
	}
	
}
