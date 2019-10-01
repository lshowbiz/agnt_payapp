package com.jm.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.util.Log4jConfigListener;

/** 
 * 描述: <配置相关Listener,servlet,filter等等>.<br>
 * @version jecs2.0                             
 */
@SuppressWarnings("deprecation")
@Order(1)
public class CommonInitializer implements WebApplicationInitializer{

	public void onStartup(ServletContext servletContext) throws ServletException {
		
		//Log4jConfigListener
		servletContext.setInitParameter("log4jConfigLocation", "classpath:properties/log4j.properties");
		servletContext.addListener(Log4jConfigListener.class);
		
		//OpenSessionInViewFilter
		OpenSessionInViewFilter hibernateSessionInViewFilter = new OpenSessionInViewFilter();
		FilterRegistration.Dynamic filterRegistration = servletContext.addFilter(
				"hibernateFilter", hibernateSessionInViewFilter);
		filterRegistration.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/");
				
		JM_Filter myFilter = new JM_Filter();
		FilterRegistration.Dynamic filterReg = servletContext.addFilter("myFilter", myFilter);
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD,DispatcherType.INCLUDE), false, "/*");
	}
	
}
