package com.ioc.iotserver.controllers;

import java.util.Arrays;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ioc.iotserver.constant.Device;
import com.ioc.iotserver.services.AIService;
import com.ioc.iotserver.services.HubService;
import com.ioc.iotserver.services.TemperatureService;

@RestController("Dashboard")
@RequestMapping("/api/auth/dashboard")
@CrossOrigin(origins="*")
public class DashboardController extends BaseController {

	@Autowired 
	private TemperatureService temperatureService; 
	
	@Autowired 
	private AIService aIService; 
	
	@Autowired 
	private HubService hubService; 
	
	@RequestMapping("/getData")
	@ResponseBody
	public HashMap<String, Object> getData() { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		/* 
		{"outdoorTempData": [ 18, 19, 19, 17, 18, 19, 18, 19, 20, 21, 22, 24, 26, 22, 24, 22, 21, 21, 21, 19, 19, 18, 17, 16, 16 ], 
	"userTempData": [ 35, 35, 35, 35, 35, 35.5, 35.5, 35.5, 36, 36, 36.5, 36, 35, 35.5, 36, 35, 35, 35, 35.5, 35, 35, 35, 35, 35, 35 ], 
	"indoorTempData": [ 21, 21, 21, 20, 20, 21.5, 21.5, 22, 22, 22, 22, 22, 22, 21, 22, 21, 21, 21, 21, 20.5, 20.5, 20.5, 20.5, 20, 20 ], 
	"minTemp": 20, "maxTemp": 22, "nextTempSign": "Turn Up to", "nextTemp": 22, "devices": ["AC 2", "TV 1"], "profile": "Jonathan"} 
		 */
		
		try { 
			
			int uid = getCurrentUserId(); 
			
			log.info("uid : " + uid);
			
			rsp.put("outdoorTempData", temperatureService.getOutdoorTemperatureList()); 
			rsp.put("devices", hubService.getDeviceByUser(uid, Device.TYPE_AC)); 
			rsp.put("profile", "Jonathan"); 
			rsp.put("calTmps", aIService.calDeviceTemperature(0, uid));  

			rsp.put("outdoorTempData", Arrays.stream("18, 19, 19, 17, 18, 19, 18, 19, 20, 21, 22, 24, 26, 22, 24, 22, 21, 21, 21, 19, 19, 18, 17, 16, 16".split(", ")).mapToDouble(Double::parseDouble).toArray()); 
			rsp.put("userTempData", Arrays.stream("35, 35, 35, 35, 35, 35.5, 35.5, 35.5, 36, 36, 36.5, 36, 35, 35.5, 36, 35, 35, 35, 35.5, 35, 35, 35, 35, 35, 35".split(", ")).mapToDouble(Double::parseDouble).toArray()); 
			rsp.put("indoorTempData", Arrays.stream("21, 21, 21, 20, 20, 21.5, 21.5, 22, 22, 22, 22, 22, 22, 21, 22, 21, 21, 21, 21, 20.5, 20.5, 20.5, 20.5, 20, 20".split(", ")).mapToDouble(Double::parseDouble).toArray()); 
			rsp.put("minTemp", 20); 
			rsp.put("maxTemp", 24); 
			rsp.put("nextTempSign", 1); 
			rsp.put("nextTemp", 23); 
			rsp.put("devices", "AC 2,TV 1".split(",")); 
			rsp.put("profile", "Jonathan"); 
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 

}
