package com.ioc.iotserver.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class UserUtils {

	public static int getUidInSession() {

		Object uid = RequestContextHolder.currentRequestAttributes().getAttribute("uId", RequestAttributes.SCOPE_REQUEST);
		
		return (uid == null) ? 0: (int) uid;  
	}

}
