package com.eaa.utils;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class ServiceUtils {
	
	public static String applicationUrl(HttpServletRequest request) {
		return "http://"+ request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	
	}
	
	public static Date calculateExpirationDate(int expirationTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, expirationTime);
		return new Date(calendar.getTime().getTime());
	}

}
