package com.ioc.iotserver.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ioc.iotserver.dao.AIDAO;

@Service("AIService")
@Scope(value = "prototype")
public class AIService extends BaseService {

	@Autowired 
	private AIDAO dao; 
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> calDeviceTemperature(int updateDeviceCommand, int uId) throws Throwable { 
		
		Map<String, Object> calTmps = dao.calDeviceTemperature(uId);  
		
		if(updateDeviceCommand == 1) { 
			
			dao.updateDeviceCommand((List<Map<String, Object>>) calTmps.get("locations")); 
		}
		
		return calTmps; 
	}
	
}
