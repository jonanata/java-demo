package com.ioc.iotserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.ioc.iotserver.dao.HubDAO;

@Service("HubService")
@Scope(value = "prototype")
public class HubService extends BaseService { 
	
	@Autowired 
	private HubDAO hubDAO; 

	public int addHub(String code, String apikey) {  
		
		return hubDAO.addHub(code, hubDAO.getUserId(apikey)); 
		
	} 
	
	public int addDevice(int hubId, String code, int deviceType, String model, String band, String host, int lId) { 
		
		return hubDAO.addDevice(hubId, code, deviceType, model, band, host, lId); 
		
	} 
	
	public int addWristband(String name, int yr, int mo, int gender, int lId, int uid) { 
		
		return hubDAO.addWristband(name, yr, mo, gender, lId, uid); 
	} 
	
	public int addLocation(double w, double d, double h, String details, String apikey) { 
		
		return hubDAO.addLocation(w, d, h, details, hubDAO.getUserId(apikey)); 
	}
	
	public int updateWristbandLocation(int lId, int wId) { 
		
		return hubDAO.updateWristbandLocation(lId, wId); 
	} 
	
	public int updateWristband(String name, int yr, int mo, int gender, int wId) { 
		
		return hubDAO.updateWristband(name, yr, mo, gender, wId); 
	} 
	
	public int updateLocationData(double w, double d, double h, int lId, String details) { 
		
		return hubDAO.updateLocationData(w, d, h, lId, details); 
	} 
	
	public List<String> getDeviceByUser(int uid, int deviceType) { 
		
		List<String> names = new ArrayList<String>(); 
		
		for(Map<String, Object> r : hubDAO.getDeviceByUser(uid, deviceType)) { 
			
			names.add((String) r.get("name")); 
			
		} 		
		
		return names; 
	}
	
	public List<Map<String, Object>> getHubSetting(int hubId, int deviceType) { 
		
		return hubDAO.getHubSetting(hubId, deviceType); 
		
	} 
	
	public int updateDeviceCommand(int dId, String lastCommand) { 
		
		return hubDAO.updateDeviceCommand(dId, lastCommand); 
	} 
	
	public ArrayList<String> updateHubData(int hubId, JsonNode devices) { 
		
		hubDAO.updateHubData(hubId); 
		
		ArrayList<String> errors = new ArrayList<String>(); 
		
		for(JsonNode device : devices) { 
			
			String code = device.get("code").asText(); 
			
			Integer dId = hubDAO.getDeviceId(hubId, code); 
			
			if(dId == null) { 
				
				errors.add("Not Found Device hubId / code : " + hubId + " / " + code); 
				
				continue; 
			} 

			hubDAO.updateDeviceData(dId); 
			
		}
		
		return errors; 
		
	}
	
}
