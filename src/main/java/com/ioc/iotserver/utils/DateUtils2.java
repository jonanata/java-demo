package com.ioc.iotserver.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils2 { 
	
	public static long getCurrentDateUT() throws Throwable { 
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        
        return dateFormat.parse(LocalDate.now().toString()).getTime() / 1000L; 
		
	} 
	
	public static Date getTimezoneDate(String str, String tz) throws Throwable {
	  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		sdf.setTimeZone(TimeZone.getTimeZone(tz) ); 
		
		return sdf.parse(str);
	}

}
