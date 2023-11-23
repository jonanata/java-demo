package com.ioc.iotserver.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

import com.ioc.iotserver.services.AccountService;

public class AuthApiFilter implements Filter {

	protected Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	private AccountService accountService; 
	
	public AuthApiFilter(AccountService accountService) { 
		
		this.accountService = accountService; 
	}

	@Override
	public void destroy() {}
	
	public void doFilter(ServletRequest res, ServletResponse rep, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub 
		
		logger.info("apikey res.getParameter : " + res.getParameter("apikey"));
		
		Cookie c = WebUtils.getCookie((HttpServletRequest) res, "apikey"); 
		String apikey = (c == null) ? res.getParameter("apikey") : c.getValue(); 

		logger.info("apikey : " + apikey);
		
		int uId = accountService.checkApiKey(apikey); 

		if(uId > 0) { 
			
			logger.info("apikey valid"); 
			
			res.setAttribute("uId", uId);
			
			filterChain.doFilter(res, rep);
		} else { 
		
			logger.info("apikey invalid");
			
			((HttpServletResponse) rep).setStatus(HttpServletResponse.SC_FORBIDDEN);
		}

	} 
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
 
    } 

}
