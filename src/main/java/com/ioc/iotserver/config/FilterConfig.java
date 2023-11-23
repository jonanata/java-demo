package com.ioc.iotserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.ioc.iotserver.security.AuthApiFilter;
import com.ioc.iotserver.services.AccountService;

@Configuration
public class FilterConfig {
	
	@Autowired 
	private AccountService accountService; 
	
	@Bean
    public FilterRegistrationBean logProcessTimeFilter() {
        FilterRegistrationBean<AuthApiFilter> bean = new FilterRegistrationBean<>();        
        bean.setFilter(new AuthApiFilter(accountService));
        bean.addUrlPatterns("/api/auth/*");
        bean.addUrlPatterns("/page/auth/*");
        bean.setName("AuthApiFilter");
        
        return bean;
    } 
	
	@Bean
    public RestTemplate restTesmplate() { 
		
        return new RestTemplate();
    }

}
