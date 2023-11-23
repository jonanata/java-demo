package com.ioc.iotserver.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.ioc.iotserver.services.HubService;
import com.ioc.iotserver.utils.JsonUtils;

@RestController("Hub")
@RequestMapping("/api/auth/hub")
public class HubController extends BaseController {
	
	@Autowired 
	private HubService hubService; 
	
	@RequestMapping("/addHub")
	@ResponseBody
	public HashMap<String, Object> addHub(@RequestParam("code") String code, @RequestParam("apikey") String apikey) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			rsp.put("hid", hubService.addHub(code, apikey)); 

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
	
	@RequestMapping("/addDevice")
	@ResponseBody
	public HashMap<String, Object> addDevice(@RequestParam("hubId") int hubId, @RequestParam("code") String code, @RequestParam("deviceType") int deviceType, 
			@RequestParam("model") String model, @RequestParam("band") String band, @RequestParam("host") String host, @RequestParam("lId") int lId) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			rsp.put("dId", hubService.addDevice(hubId, code, deviceType, model, band, host, lId)); 

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
	
	@RequestMapping("/addWristband")
	@ResponseBody
	public HashMap<String, Object> addWristband(@RequestParam("name") String name, @RequestParam("yr") int yr, @RequestParam("mo") int mo, @RequestParam("gender") int gender, 
			@RequestParam("lId") int lId) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			int uid = getCurrentUserId(); 
			
			rsp.put("wId", hubService.addWristband(name, yr, mo, gender, lId, uid)); 

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
	
	@RequestMapping("/addLocation")
	@ResponseBody
	public HashMap<String, Object> addLocation(@RequestParam("w") double w, @RequestParam("d") double d, @RequestParam("h") double h, @RequestParam("details") String details, @RequestParam("apikey") String apikey) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			rsp.put("lId", hubService.addLocation(w, d, h, details, apikey)); 

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
	
	@RequestMapping("/updateWristbandLocation")
	@ResponseBody
	public HashMap<String, Object> updateWristbandLocation(@RequestParam("lId") int lId, @RequestParam("wId") int wId) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			isOk = hubService.updateWristbandLocation(lId, wId); 

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
	
	@RequestMapping("/updateWristband")
	@ResponseBody
	public HashMap<String, Object> updateWristband(@RequestParam("name") String name, @RequestParam("yr") int yr, @RequestParam("mo") int mo, @RequestParam("gender") int gender, @RequestParam("wId") int wId) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			isOk = hubService.updateWristband(name, yr, mo, gender, wId); 

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
	
	@RequestMapping("/updateLocationData")
	@ResponseBody
	public HashMap<String, Object> updateLocationData(@RequestParam("w") double w, @RequestParam("d") double d, @RequestParam("h") double h, @RequestParam("details") String details, @RequestParam("lId") int lId) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			isOk = hubService.updateLocationData(w, d, h, lId, details); 

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
	
	@RequestMapping("/getHubSetting")
	@ResponseBody
	public HashMap<String, Object> getHubSetting(@RequestParam("hubId") int hubId, @RequestParam("deviceType") int deviceType) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		try { 
			
			rsp.put("data", hubService.getHubSetting(hubId, deviceType)); 
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 
	
	@RequestMapping("/updateHubData")
	@ResponseBody
	public HashMap<String, Object> updateHubData(@RequestParam("data") String data) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		log.info("updateHubData data : " + data);		
		
		try { 
			
			JsonNode config = JsonUtils.toObj(data); 
			
			int hubId = config.get("hubId").asInt(); 
			
			JsonNode devices = config.get("devices"); 
			
			ArrayList<String> errors = hubService.updateHubData(hubId, devices); 
			
			if(errors.size() > 0) { 
				
				isOk = 0; 
				
				rsp.put("deviceErrors", errors); 
			}

			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	}

}
