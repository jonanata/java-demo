package com.ioc.iotserver.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ioc.iotserver.services.AccountService;

@RestController("Login")
@RequestMapping("/api/p/login")
public class LoginController extends BaseController {
	
	@Autowired 
	private AccountService accountService; 
	
	@RequestMapping("/login")
	@ResponseBody
	public HashMap<String, Object> login(@RequestParam("username") String username, @RequestParam("password") String password) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 

		try { 
			
			isOk = accountService.login(username, password);  
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp);
		
	} 

}
