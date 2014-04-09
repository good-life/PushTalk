package org.pushtalk.server.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

public class NumberUtils {
	public static boolean isNumberic(Object obj){
		if(obj==null){
			return false;
		}
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(String.valueOf(obj));
		return m.matches();
	}
	public static  int getIntValueFromRequest(HttpServletRequest request, String paramName) {
		String param = request.getParameter(paramName);
		int intValue = -1;
		if (!StringUtils.isEmpty(param)) {
			intValue = Integer.parseInt(param);
		}
		return intValue; 
	}
	
	public static  double getDoubleValueFromRequest(HttpServletRequest request, String paramName) {
		String param = request.getParameter(paramName);
		double value = 0;
		if (!StringUtils.isEmpty(param)) {
			value = Double.parseDouble(param);
		}
		return value; 
	}
}
