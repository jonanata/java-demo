package com.ioc.iotserver.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ioc.iotserver.services.AccountService;

@RestController("Account")
@RequestMapping("/api/auth/account")
public class AccountController extends BaseController { 
	
	@Autowired 
	private AccountService accountService; 
	
	@RequestMapping("/addAccount")
	@ResponseBody
	public HashMap<String, Object> addAccount(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("phone") String phone, 
			@RequestParam("address") String address, @RequestParam("lat") double lat,  @RequestParam("lon") double lon, @RequestParam("timezone") String timezone, 
			HttpServletRequest request) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 

		try { 
			
			rsp.put("uid", accountService.addAccount(username, email, phone, address, lat, lon, timezone, request.getRemoteAddr())); 
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 

}
