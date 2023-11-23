package com.ioc.iotserver.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ioc.iotserver.services.AIService;

@RestController("AI")
@RequestMapping("/api/auth/ai")
public class AIController extends BaseController { 
	
	@Autowired 
	private AIService aIService; 
	
	//get AI cal data 
	@RequestMapping("/calDeviceTemperature")
	@ResponseBody
	public HashMap<String, Object> calDeviceTemperature() { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		//check is admin 
		
		try { 
			
			//rsp.put("data", aIService.calDeviceTemperature(0)); 
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
	
	//AI update the device command 
	@RequestMapping("/updateDeviceCommand")
	@ResponseBody
	public HashMap<String, Object> updateDeviceCommand() { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		//check is admin 
		
		try { 
			
			//rsp.put("data", aIService.calDeviceTemperature(1)); 
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 

}
