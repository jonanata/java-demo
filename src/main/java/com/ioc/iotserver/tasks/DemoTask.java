package com.ioc.iotserver.tasks;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DemoTask { 
	
	private static final Logger logger = LoggerFactory.getLogger(DemoTask.class); 
	
	private static String domain = "http://localhost:80"; 
	
	private static String apikey = "10011-0246d92c-c8ae-4d62-925c-3833195560c0"; 
	
	private DecimalFormat f = new DecimalFormat("##.00"); 
	
	//{{domain}}/api/auth/temperature/addDeviceTemperature?apikey={{apikey}}&dId=2&degree=36.2 
	//{{domain}}/api/auth/temperature/addWristbandTemperature?apikey={{apikey}}&wId=2&degree=36.2 
	
	@Scheduled(cron = "0 * * * * ?")
	public void addDeviceTemperature() { 
		
		String url = domain + "/api/auth/temperature/addDeviceTemperature"; 
		
	    try {
	    	
	    	CloseableHttpClient client = HttpClients.createDefault(); 
			
			HttpPost httpPost = new HttpPost(url);

		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("apikey", apikey));
		    params.add(new BasicNameValuePair("dId", "2")); 
		    params.add(new BasicNameValuePair("degree", f.format(getRandoDouble(18, 30))));
		    params.add(new BasicNameValuePair("humidity", f.format(getRandoDouble(50, 80))));
		    
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			
			CloseableHttpResponse response = client.execute(httpPost); 
			
			logger.info(response.toString());
			
		    client.close(); 
		    
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		/* Map<String, Object> params = Map.of("apikey", apikey, "dId", 2, "degree", getRandoDouble(18, 30)); 
		
		RestTemplate restTemplate = new RestTemplate();  
		
		String resp = restTemplate.postForObject(url, new HttpEntity<>(params, null), String.class); */ 
		
		
		
	} 
	
	@Scheduled(cron = "0 * * * * ?")
	public void addWristbandTemperature() { 
		
		String url = domain + "/api/auth/temperature/addWristbandTemperature"; 
		
	    try {
	    	
	    	CloseableHttpClient client = HttpClients.createDefault(); 
			
			HttpPost httpPost = new HttpPost(url);

		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("apikey", apikey));
		    params.add(new BasicNameValuePair("wId", "1")); 
		    params.add(new BasicNameValuePair("degree", f.format(getRandoDouble(35.5, 37))));
	    	
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			
			CloseableHttpResponse response = client.execute(httpPost); 
			
			logger.info(response.toString());
			
		    client.close(); 
		    
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		/* Map<String, Object> params = Map.of("apikey", apikey, "wId", 2, "degree", getRandoDouble(35.5, 37)); 
		
		RestTemplate restTemplate = new RestTemplate();  
		
		String resp = restTemplate.postForObject(url, new HttpEntity<>(params, null), String.class); */ 
		
	} 
	
	private double getRandoDouble(double min, double max) { 
		
		return ((new Random()).nextDouble() * (max - min)) + min;  
	}

}
