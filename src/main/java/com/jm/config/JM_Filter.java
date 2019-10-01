package com.jm.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
/**
 * 描述: 自定义过滤器
 * <p>
 * 	过滤URL
 * </p>
 */
public class JM_Filter implements Filter {

	private static final Logger logger = Logger.getLogger(JM_Filter.class);
	
	public void init(FilterConfig filterConfig) throws ServletException {
		// 启动时调用一次,仅执行一次
		System.out.println("---JM_Filter init---");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		/*
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		System.out.println("==========================================");
		String contextPath = httpRequest.getContextPath();
		System.out.println("contextPath=="+contextPath);
		String addr = httpRequest.getLocalAddr();
		System.out.println("localAddr=="+addr);
		
		String reAddr =httpRequest.getRemoteAddr();
		System.out.println("RemoteAddr=="+reAddr);
		String rehost = httpRequest.getRemoteHost();
		System.out.println("RemoteHost=="+rehost);
		
		String requestUri = httpRequest.getRequestURI();
		System.out.println("RequestURI=="+requestUri);
		String requestURL= httpRequest.getRequestURL().toString();
		System.out.println("RequestURL=="+requestURL);
		
		String serverName = httpRequest.getServerName();
		System.out.println("ServerName=="+serverName);
		int serverPort = httpRequest.getServerPort();
		System.out.println("ServerPort=="+serverPort);
		
		String servletPath = httpRequest.getServletPath();
		System.out.println("ServletPath=="+servletPath);
		*/
		
		String servletPath = httpRequest.getServletPath();
		logger.info("servletPath is:"+servletPath);
		
		if(servletPath.equals("/demo/showForm1")){
			return;
		} else {
			chain.doFilter(request, response);
		}
		
	}

	public void destroy() {
		// 在Web容器卸载 Filter 对象之前被调用,仅执行一次
		System.out.println("JM_Filter destroy....");
	}

}
