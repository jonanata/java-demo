package com.ioc.iotserver.controllers;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ioc.iotserver.utils.UserUtils;

@RestController
@RequestMapping("/api")
public class BaseController { 
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected int getCurrentUserId() {

		return UserUtils.getUidInSession(); 
	} 
	
	protected ModelAndView viewInit(String htmlName) { 
		
		ModelAndView mav = new ModelAndView(htmlName);

    	return mav;
	}
	
	public HashMap home() { 
		
		HashMap rsp = new HashMap(); 
		
		rsp.put("msg", "IOC"); 
		
		return rsp; 
		
	} 
	
	protected HashMap<String, Object> resp(String msg, int isOk, HashMap<String, Object> data) {

		data.put("msg", msg);
		data.put("isOk", isOk);

		return data;

	} 

}
