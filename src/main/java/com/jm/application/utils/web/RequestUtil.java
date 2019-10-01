package com.jm.application.utils.web;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings({ "rawtypes" })
public class RequestUtil {
	private transient static Log log = LogFactory.getLog(RequestUtil.class);

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String paramStr(HttpServletRequest request) {
		String returnStr = request.getQueryString();
		if (StringUtils.isEmpty(returnStr) || returnStr.replace("zmType=", "").length()<40) {
			Enumeration rnames = request.getParameterNames();
			for (Enumeration e = rnames; e.hasMoreElements();) {
				String thisName = e.nextElement().toString();
				String thisValue = request.getParameter(thisName);
				returnStr += (thisName + "=" + thisValue + "&");
				log.info(thisName + "----------------------------" + thisValue);
			}
		}
		return returnStr;
	}

	public static String paramStr2(HttpServletRequest request) {
		String returnStr = request.getQueryString();
		if (StringUtils.isEmpty(returnStr) || returnStr.replace("zmType=", "").length()<40) {
			Enumeration rnames = request.getParameterNames();
			for (Enumeration e = rnames; e.hasMoreElements();) {
				String thisName = e.nextElement().toString();
				String thisValue = request.getParameter(thisName);
				returnStr += (thisName + "=" + thisValue + "&");
			}
		}
		returnStr = request.getRequestURL().toString() + "?" + returnStr;
		return returnStr;
	}
}
