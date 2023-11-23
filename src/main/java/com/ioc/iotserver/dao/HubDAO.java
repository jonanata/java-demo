package com.ioc.iotserver.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component("HubDAO")
public class HubDAO extends BaseDAO {

	public int addHub(String code, int userId) {  
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		
		db.update(c -> {
	        PreparedStatement ps = c.prepareStatement("insert into hub(`code`, `userId`) values (?, ?)", Statement.RETURN_GENERATED_KEYS);  
	        ps.setString(1, code); 
	        ps.setInt(2, userId);
	        return ps;
	        
		}, keyHolder); 
		
		return (int) keyHolder.getKey().intValue(); 
		
	} 
	
	public int addDevice(int hubId, String code, int deviceType, String model, String band, String host, int lId) {  
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		
		db.update(c -> {
	        PreparedStatement ps = c.prepareStatement("insert into device(`hubId`, `code`, `deviceType`, `model`, `band`, `host`, `lId`) values (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);  
	        ps.setInt(1, hubId); 
	        ps.setString(2, code); 
	        ps.setInt(3, deviceType); 
	        ps.setString(4, model); 
	        ps.setString(5, band); 
	        ps.setString(6, host); 
	        ps.setInt(7, lId);
	        return ps;
	        
		}, keyHolder); 
		
		return (int) keyHolder.getKey().intValue(); 
		
	} 
	
	public int addWristband(String name, int yr, int mo, int gender, int uId, int lId) {  
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		
		db.update(c -> {
	        PreparedStatement ps = c.prepareStatement("insert into wristband(`name`, `yr`, `mo`, `gender`, `uId`, `lId`) values (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);  
	        ps.setString(1, name); 
	        ps.setInt(2, yr); 
	        ps.setInt(3, mo); 
	        ps.setInt(4, gender); 
	        ps.setInt(5, uId); 
	        ps.setInt(6, lId); 
	        return ps;
	        
		}, keyHolder); 
		
		return (int) keyHolder.getKey().intValue(); 
		
	} 
	
	public int addLocation(double w, double d, double h, String details, int uId) {  
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		
		db.update(c -> {
	        PreparedStatement ps = c.prepareStatement("insert into location(`w`, `d`, `h`, `details`, `uId`) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);  
	        ps.setDouble(1, w); 
	        ps.setDouble(2, d); 
	        ps.setDouble(3, h); 
	        ps.setString(4, details); 
	        ps.setInt(5, uId); 
	        return ps;
	        
		}, keyHolder); 
		
		return (int) keyHolder.getKey().intValue(); 
	} 
	
	public List<Map<String, Object>> getDeviceByUser(int uid, int deviceType) { 
		
		return db.queryForList("SELECT d.* FROM device d , hub h WHERE d.hubId = h.hubId AND h.uId = ? and deviceType = ? AND d.status = 1 ", uid, deviceType); 
	}
	
	public List<Map<String, Object>> getHubSetting(int hubId, int deviceType) { 
		
		return db.queryForList("select * from device where hubId = ? and deviceType = ? and status = 1 ", hubId, deviceType); 
		
	} 
	
	public int updateDeviceCommand(int dId, String lastCommand) { 
		
		return db.update("update device set lastCommand = ? where dId = ? ", lastCommand, dId); 
		
	} 
	
	public int updateHubData(int hubId) { 
		
		return db.update("update hub set lastRequest = now() where hubId = ? ", hubId); 
		
	} 
	
	public int updateDeviceData(int dId) { 
		
		return db.update("update device set lastEdgeStatus = 1 , lastRequest = now() where dId = ? ", dId); 
		
	} 
	
	public int updateWristbandLocation(int lId, int wId) { 
		
		return db.update("update wristband set lId = ? , lastRequest = now() where wId = ? ", lId, wId); 
		
	} 
	
	public int updateWristband(String name, int yr, int mo, int gender, int wId) { 
		
		return db.update("update wristband set name = ? , yr = ? , mo = ? , gender = ? , lastRequest = now() where wId = ? ", name, yr, mo , gender , wId); 
		
	} 
	
	public int updateLocationData(double w, double d, double h, int lId, String details) {  
		
		return db.update("update location set w = ? , d = ? , h = ? , details = ? where lId = ? ", w, d, h, details, lId); 
	}
	
	public Integer getDeviceId(int hubId, String code) { 
		
		return getObject(db, "select dId from device where hubId = ? and code = ? ", Integer.class, hubId, code); 
	}
	
}
