package com.ioc.iotserver.dao;

import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component("AccountDAO")
public class AccountDAO extends BaseDAO { 
	
	public String getApiKey(String username, String password) { 
		
		logger.info("getApiKey username / password : " + username + " / " + password);
		
		return getObject(db, "select apikey from account where username = ? and password = ? and status = 1 ", String.class, username, password);
	}
	
	public int checkApiKey(String apikey) { 
		
		Object r = getObject(db, "select uId from account where apikey = ? and status = 1 ", Integer.class, apikey); 
		
		return (r == null) ? 0 : (int) r;
	} 
	
	public int addAccount(String username, String email, String phone, String address, double lat,  double lon, String timezone, String hostIp) {  
		
		String sql = "insert into account(`username`, `email`, `phone`, `address`, `lat`, `lon`, `timezone`, `hostIp`, `password`) values (?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		
		db.update(c -> {
	        PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  
	        ps.setString(1, username); 
	        ps.setString(2, email); 
	        ps.setString(3, phone);
	        ps.setString(4, address); 
	        ps.setDouble(5, lat); 
	        ps.setDouble(6, lon); 
	        ps.setString(7, timezone); 
	        ps.setString(8, hostIp); 
	        ps.setString(9, RandomStringUtils.random(12, 0, 0, true, true, null, new SecureRandom())); 
	        return ps;
	        
		}, keyHolder); 
		
		int uid = keyHolder.getKey().intValue(); 
				
		db.update("update account set apikey = ? where uId = ? ", (uid + 10000) + "-" + java.util.UUID.randomUUID().toString(), uid);
		
		return uid; 
	} 
	
	public int login(String username, String password) { 
		
		return getObject(db, "select count(*) from account where username = ? and password = ? and status = 1 ", Integer.class, username, password);
	} 

}
