package com.ioc.iotserver.utils;

import java.util.Map;

import org.slf4j.Logger;

public class LogUtils {
	
	@SuppressWarnings("unchecked")
	public static void printMap(Logger logger, Map d) { 
		
		d.forEach((key, value) -> logger.info(key + ":" + value)); 
		
	}

}
