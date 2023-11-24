package com.ioc.iotserver.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ioc.iotserver.constant.Device;
import com.ioc.iotserver.utils.DateUtils2;

@Component("AIDAO")
public class AIDAO extends BaseDAO {
	
	@Autowired 
	private TemperatureDAO temperatureDAO; 
	
	//no hub 
	//locations + forecasts 
	//location based : devices avg temps + user temps + (bar --> suggested temps rang) --> nextTempSign --> nextTemp 
	public Map<String, Object> calDeviceTemperature(int uId) throws Throwable { 
		
		String dateStr = LocalDate.now().toString(); 
		
		int hr = LocalDateTime.now().getHour(); 
		
		return calDeviceTemperature(uId, dateStr, hr); 
	} 
	
	public Map<String, Object> calDeviceTemperature(int uId, String dateStr, int hr) throws Throwable { 
				
		List<Map<String, Object>> locations = new ArrayList<Map<String, Object>>(); 
		Map<String, Object> calTmps = Map.of("locations", locations); 
		
		String timezone = db.queryForObject("select timezone from account where uId = ? ", String.class, uId); 
		
		Date dTz = DateUtils2.getTimezoneDate(dateStr, timezone);
		
		List<Map<String, Object>> forecasts = db.queryForList("select * from owm_hourly where status = 1 and ? >= dt and timezone = ? order by dt limit 2 ", dTz.getTime() / 1000L, timezone); 
		
		double nextOutdoorTemp = (double) forecasts.get(1).get("temp"); 
		
		for(Map<String, Object> loc : db.queryForList("select * from location where uId = ? and status = 1 ", uId)) { 
			
			Integer lId = ((Long) loc.get("lId")).intValue(); 
			String details = (String) loc.get("details"); 
			
			//get indoor temp 
			double[] indoorTempData = temperatureDAO.getIndoorTemperatureList(dateStr, lId); 	
			
			//change to use getIndoorTemperatureByDate 
			Double avgTemp = temperatureDAO.getIndoorTemperatureByDate(lId, dateStr, hr); 
			//double avgTemp = indoorTempData[hr]; 
			
			//get user temp 
			List<double[]> userTempData = new ArrayList<double[]>(); 
			
			List<Double> userTempLatest = new ArrayList<Double>(); 
			
			for(Integer wId : db.queryForList("select wId from wristband where lId = ? and status = 1 ", Integer.class, lId)) { 
				
				userTempData.add(temperatureDAO.getWristbandTemperatureList(dateStr, wId)); 
				userTempLatest.add(temperatureDAO.getWristbandTemperatureLatest(wId)); 
			}

			//get wristband + suggest_temperature_range 
			String sqlTemperatureRang = "select a.* , (a.max_degree + a.min_degree) / 2 as mid_degree from "
					+ "(select max(r.max_degree) as max_degree , min(r.min_degree) as min_degree from wristband w , suggest_temperature_range r "
					+ "where w.total_mo between r.total_min_mo and r.total_max_mo and w.gender = r.gender and w.status = 1 and r.status = 1 and w.lId = ?) as a ";  
			
			List<Map<String, Object>> suggestedTemperature = db.queryForList(sqlTemperatureRang, lId); 
			
			Double maxTemp = null; 					
			Double minTemp = null; 

			int nextTempSign = -1; 
			Double nextTemp = null; 
			
			List<Integer> dIds = new ArrayList<Integer>(); 
			
			if(suggestedTemperature.size() > 0) { 
				
				//get suggested temp 
				Map<String, Object> r = suggestedTemperature.get(0); 
				
				if(r.get("max_degree") != null) { 
					
					maxTemp = (Double) r.get("max_degree"); 					
					minTemp = (Double) r.get("min_degree"); 
	
					nextTempSign = 0; 
					nextTemp = (minTemp + maxTemp) / 2; 
					
					if(avgTemp == null) { 
						
					}else if(avgTemp >= minTemp && avgTemp <= maxTemp) { 
						
						//ass open data 
						
						//update temperature direction , avgDegree < midDegree and forecasts[1].temp < avgDegree --> device temperature setting value increase , AI calculate how many steps of changes 
						if(nextOutdoorTemp < avgTemp) { 
							
							nextTempSign = 1; 
						}
																
					} else if(avgTemp > maxTemp) { 
						
						//device temperature setting value decrease 
						nextTempSign = 0; 
						
					} else { 
						
						//device temperature setting value increase 
						nextTempSign = 1; 
					} 
					
					dIds = db.queryForList("select dId from device where status = 1 and lId = ? and deviceType = " + Device.TYPE_AC, Integer.class, lId); 
				}

			}
			
			HashMap<String, Object> locationData = new HashMap<String, Object>(); 
			
			locationData.put("lId", lId); locationData.put("details", details); 
			locationData.put("avgTemp", avgTemp); locationData.put("maxTemp", maxTemp); locationData.put("minTemp", minTemp); 
			locationData.put("nextTempSign", nextTempSign); locationData.put("nextTemp", nextTemp); 
			locationData.put("dIds", dIds); 
			locationData.put("indoorTempData", indoorTempData); 
			locationData.put("userTempData", userTempData); locationData.put("userTempLatest", userTempLatest); 
			
			locations.add(locationData); 

		} 

		return calTmps; 
		
	} 
	
	public int updateDeviceCommand(List<Map<String, Object>> locations) { 
		
		for(Map<String, Object> location : locations) { 
			
			Map<String, Object> r = db.queryForList("select * from device where status = 1 and dId = ? ", location.get("dId")).get(0); 
			
			String commandName = "nextTempSign" + (((int) location.get("nextTempSign") == 0) ? "Down" : "Up" ); 
			
			String lastCommand = getObject(db, "select command from device_command where model = ? and command_name = ? and status = 1 ", String.class, "", r.get("model"), commandName);  
			
			if(((String) r.get("band")).equalsIgnoreCase("zmote")) { 
				
				lastCommand = "sendir,1:1,0," + lastCommand; 
			}
			
			db.update("update device set lastCommand = ? where dId = ? ", lastCommand, location.get("dId")); 
			//db.update("update device set lastCommand = ? where dId = ? ", location.get("maxDegree") + "/" + location.get("minDegree") + "/" + location.get("nextTemp") + "/" + location.get("nextTempSign"), location.get("dId")); 
			
		}

		return 1; 
		
	}

}
