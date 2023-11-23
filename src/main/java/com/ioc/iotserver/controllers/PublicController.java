package com.ioc.iotserver.controllers;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ioc.iotserver.services.AccountService;

@Controller
@CrossOrigin(origins="*")
@RequestMapping("/api/public")
public class PublicController extends BaseController {

	@Autowired 
	private AccountService accountService; 
	
	@RequestMapping("/login")
	@ResponseBody
	public HashMap<String, Object> login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) { 
		
		int isOk = 1;
		String msg = "";
		HashMap<String, Object> rsp = new HashMap<String, Object>(); 
		
		log.info("login username / password : " + username + " / " + password);
		
		try { 
			
			rsp.put("login", accountService.login(username, password)); 
			
			Cookie c = new Cookie("apikey", accountService.getApiKey(username, password)); 
			c.setPath("/");
			
			response.addCookie(c);

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			isOk = 0;
			msg = e.getMessage();
		}
		
		return resp(msg, isOk, rsp); 
		
	}
		
	
}
