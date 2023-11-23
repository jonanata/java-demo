package com.ioc.iotserver.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ioc.iotserver.services.TemperatureService;

@RestController("Temperature")
@RequestMapping("/api/auth/temperature")
public class TemperatureController extends BaseController {

	@Autowired 
	private TemperatureService temperatureService; 
	
	//hub , physically connects to the devices (get sensors data , update AC temperature)  

	@RequestMapping("/addDeviceTemperature")
	@ResponseBody
	public HashMap<String, Object> addDeviceTemperature(@RequestParam("dId") int dId, @RequestParam("degree") double degree, @RequestParam("humidity") double humidity) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			rsp.put("id", temperatureService.addDeviceTemperature(dId, degree, humidity)); 
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
	
	@RequestMapping("/addWristbandTemperature")
	@ResponseBody
	public HashMap<String, Object> addWristbandTemperature(@RequestParam("wId") int wId, @RequestParam("degree") double degree) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			rsp.put("id", temperatureService.addWristbandTemperature(wId, degree)); 
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
		
}
