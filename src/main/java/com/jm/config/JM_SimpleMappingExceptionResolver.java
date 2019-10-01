package com.jm.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/** 
 * 描述: <类功能描述>. <br>
 * <p>
 * 	HandlerExceptionResolver接口,全局异常处理
 * </p>
 */
public class JM_SimpleMappingExceptionResolver  extends SimpleMappingExceptionResolver{
	
	private static final Logger logger = Logger
			.getLogger(JM_SimpleMappingExceptionResolver.class);
	
	/**
	 * 在返回modelAndView前如果出现异常时调用
	 */
	@Override
	protected ModelAndView getModelAndView(String viewName, Exception ex,
			HttpServletRequest request) {
		
		logger.error("run time exception:", ex);//将异常信息打印到日志中
		return super.getModelAndView(viewName, ex, request);
	}
	
}

