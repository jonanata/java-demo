package com.ioc.iotserver.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component("TemperatureDAO")
public class TemperatureDAO extends BaseDAO { 
	
	public List<Integer> getLidsByHubId(int hubId, int deviceType) { 
		
		return db.queryForList("SELECT DISTINCT d.lId FROM hub h , device d WHERE h.hubId = d.hubId AND d.status = 1 AND h.status = 1 AND d.deviceType = ? AND h.hubId = ? ", Integer.class, 
				deviceType, hubId); 
	}
	
	public Double getIndoorTemperatureByDate(int lId, String dateStr, int hr) { 
		
		String sql = "select avg(dt.degree) as degree from device_temperature dt , device d where dt.dId = d.dId and d.lId = ? and date_str = ? and hr = ? and dt.status = 1 and d.status = 1 "; 
		
		return getObject(db, sql, Double.class, lId, dateStr, hr); 
	}
	
	public Double getIndoorTemperatureRT(int lId) { 
		
		return getObject(db, "select avg(dt.degree) as degree from device_temperature dt , device d where dt.dId = d.dId and d.lId = ? ", Double.class, lId); 
	}
	
	public void updateHourlyOutdoorTemperature(List<Map<String, Object>> hourlyData, double lat, double lon, String timezone) { 
		
		String sql = "insert into owm_hourly(`dt`, `temp`, `feels_like`, `pressure`, `humidity`, `clouds`, `wind_speed`, `wind_deg`, `lat`, `lon`, `timezone`) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
		
		for(Map<String, Object> d : hourlyData) { 
			
			//d.forEach((key, value) -> System.out.println(key + ":" + value)); 
			
			int dt = (int) d.get("dt"); 
			
			db.update("delete from owm_hourly where dt = ? and lat = ? and lon = ? and timezone = ? ", dt, lat, lon, timezone); 
			
			db.update(sql, d.get("dt"), d.get("temp"), d.get("feels_like"), d.get("pressure"), d.get("humidity"), d.get("clouds"), d.get("wind_speed"), d.get("wind_deg"), lat, lon, timezone); 

		}
		
		
	}
	
	public List<Double> getOutdoorTemperatureList(long dt) { 
		
		return db.queryForList("select temp from owm_hourly where dt >= ? and status = 1 limit 24 ", Double.class, dt); 
		
	} 
	
	//hourly avg 
	public double[] getWristbandTemperatureList(String date, int wId) { 
		
		String sql = "select round(avg(th.degree), 2) AS degree, th.hr "
				+ "from wristband_temperature_history th , wristband w where th.wId = w.wId AND th.date_str = ? and w.wId = ? and th.status = 1 "
				+ "group by hr order by hr "; 
		
		double[] temps = new double[24]; 

		for(Map<String, Object> r : db.queryForList(sql, date, wId) ) { 
			
			temps[(int) r.get("hr")] = (double) r.get("degree"); 
		}
		
		return temps; 
		
	} 
	
	//hourly avg by location  
	public double[] getIndoorTemperatureList(String date, int lId) { 
		
		String sql = "SELECT round(avg(th.degree), 2) AS degree, th.hr " + 
				"FROM device_temperature_history th , device d WHERE th.dId = d.dId AND th.date_str = ? AND d.lId = ? AND th.`status` = 1 " + 
				"group BY th.hr "; 
		
		logger.debug("getIndoorTemperatureList sql : " + sql);
		
		double[] temps = new double[24]; 

		for(Map<String, Object> r : db.queryForList(sql, date, lId) ) { 
			
			temps[(int) r.get("hr")] = (double) r.get("degree"); 
		}
		
		return temps; 

	} 
	
	public double getWristbandTemperatureLatest(int wId) { 
		
		return getObject(db, "select degree from wristband_temperature where wId = ? ", Double.class, wId);  
	}
	
	public int addWristbandTemperature(int wId, double degree) {  
		
		db.update("delete from wristband_temperature where wId = ? ", wId); 
		
		String sql = "insert into wristband_temperature(`wId`, `degree`, `date_str`, `hr`, `created`) values (?, ?, DATE_FORMAT(NOW(), '%Y-%m-%d'), DATE_FORMAT(NOW(), '%H'), now())"; 
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		
		db.update(c -> {
	        PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  
	        ps.setInt(1, wId); 
	        ps.setDouble(2, degree);
	        return ps;
	        
		}, keyHolder); 
		
		int id = (int) keyHolder.getKey().intValue(); 
		
		updateHistory("wristband_temperature", "wristband_temperature_history", "wtId", id); 
		
		return id; 
		
	} 
	
	public int addDeviceTemperature(int dId, double degree, double humidity) {  
		
		db.update("delete from device_temperature where dId = ? ", dId); 
		
		String sql = "insert into device_temperature(`dId`, `degree`, `humidity`, `date_str`, `hr`, `created`) values (?, ?, ?, DATE_FORMAT(NOW(), '%Y-%m-%d'), DATE_FORMAT(NOW(), '%H'), now())"; 
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		
		db.update(c -> {
	        PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  
	        ps.setInt(1, dId); 
	        ps.setDouble(2, degree); 
	        ps.setDouble(3, humidity);
	        return ps;
	        
		}, keyHolder); 
		
		int id = (int) keyHolder.getKey().intValue(); 
		
		updateHistory("device_temperature", "device_temperature_history", "dtId", id); 
		
		return id; 
		
	} 
	
}
