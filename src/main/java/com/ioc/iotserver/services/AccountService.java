package com.ioc.iotserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ioc.iotserver.dao.AccountDAO;

@Service("AccountService")
@Scope(value = "prototype")
public class AccountService extends BaseService {

	@Autowired 
	private AccountDAO accountDAO; 
	
	public String getApiKey(String username, String password) { 
		
		return accountDAO.getApiKey(username, password); 
	}
	
	public int checkApiKey(String apikey) { 
		
		return accountDAO.checkApiKey(apikey); 
		
		
	} 
	
	public int addAccount(String username, String email, String phone, String address, double lat,  double lon, String timezone, String hostIp) { 
		
		return accountDAO.addAccount(username, email, phone, address, lat, lon, timezone, hostIp); 
		
	} 
	
	public int login(String username, String password) { 
		
		return accountDAO.login(username, password); 
	}
	
}
