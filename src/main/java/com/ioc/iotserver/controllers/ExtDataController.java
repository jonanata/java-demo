package com.ioc.iotserver.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ioc.iotserver.services.TemperatureService;
import com.ioc.iotserver.utils.JsonUtils;

@RestController("ExtData")
@RequestMapping("/api/auth/extdata")
public class ExtDataController extends BaseController {
	
	@Autowired
	private RestTemplate restTemplate; 
	
	@Autowired
	private TemperatureService temperatureService; 
	
	@RequestMapping("/extractOWMonecall")
	@ResponseBody
	public HashMap<String, Object> extractOWMonecall() { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			String url = "https://api.openweathermap.org/data/2.5/onecall?lat=22.30&lon=114.18&exclude=current,minutely,daily,alerts&units=metric&appid=2a49cfebd8dbdfaa6383e76156f7eca9"; 

			temperatureService.updateHourlyOutdoorTemperature(JsonUtils.toObj(restTemplate.getForEntity(url, String.class).getBody())); 
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 

}
