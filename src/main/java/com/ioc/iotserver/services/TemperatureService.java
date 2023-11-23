package com.ioc.iotserver.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.ioc.iotserver.constant.Device;
import com.ioc.iotserver.dao.TemperatureDAO;
import com.ioc.iotserver.utils.DateUtils2;
import com.ioc.iotserver.utils.JsonUtils;

@Service("TemperatureService")
@Scope(value = "prototype")
public class TemperatureService extends BaseService { 
	
	@Autowired 
	private TemperatureDAO temperatureDAO; 
	
	public Map<Integer, Double> getDeviceTemperatures(int hubId) { 
		
		//get lIds by hubId  
		Map<Integer, Double> temps = new HashMap<Integer, Double>(); 
		
		for(Integer lId : temperatureDAO.getLidsByHubId(hubId, Device.TYPE_SENSOR)) { 
			
			temps.put(lId, temperatureDAO.getIndoorTemperatureRT(lId)); 
		}
		
		return temps; 
	}
	
	public double getIndoorTemperatureRT(int lId) { 
		
		return temperatureDAO.getIndoorTemperatureRT(lId); 
	}
	
	public List<Double> getOutdoorTemperatureList() throws Throwable { 
		
		long cDT = DateUtils2.getCurrentDateUT(); 

		log.info("cDT : " + cDT);
		
		return temperatureDAO.getOutdoorTemperatureList(cDT ); 
	} 
	
	public double[] getUserTemperatureList() { 

		return temperatureDAO.getWristbandTemperatureList(LocalDate.now().toString(), 2); 
	} 
	
	public double[] getIndoorTemperatureList() { 
		
		return temperatureDAO.getIndoorTemperatureList(LocalDate.now().toString(), 1); 

	} 
	
	public void updateHourlyOutdoorTemperature(JsonNode jsons) throws Throwable { 
		
		double lat = jsons.get("lat").asDouble(); 
		double lon = jsons.get("lon").asDouble(); 
		String timezone = jsons.get("timezone").asText(); 
		List<Map<String, Object>> hourlyData = JsonUtils.toObj(jsons.get("hourly"), new TypeReference<List<Map<String, Object>> >(){}); 
		
		temperatureDAO.updateHourlyOutdoorTemperature(hourlyData, lat, lon, timezone); 
		
	} 
	
	public int addWristbandTemperature(int wId, double degree) { 
		
		return temperatureDAO.addWristbandTemperature(wId, degree); 
	}
	
	public int addDeviceTemperature(int dId, double degree, double humidity) { 
		
		return temperatureDAO.addDeviceTemperature(dId, degree, humidity); 
	}
	
}
