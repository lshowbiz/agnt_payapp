package com.jm.config;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/** 
 *描述: <类功能描述>. <br>
 *<p>
	拦截器,在调用controller前调用此拦截器
 </p>                      
 */
public class JM_InitializingInterceptor extends HandlerInterceptorAdapter {
	Logger logger = Logger.getLogger(JM_InitializingInterceptor.class);

	/**
	 * 调用controller前,进入此方法
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
	
		// log追加：用户名 - sessionID - IP - URL - 请求参数
		StringBuffer log = new StringBuffer();
		log.append(" - ").append(request.getSession().getId());
		log.append(" - ").append(request.getRemoteAddr());
		log.append(" - ").append(request.getServletPath());
		if (request.getQueryString() != null) {
			log.append(" - ").append(request.getQueryString()).append(" - ");
		} else {
			Map<String, String[]> parameters = request.getParameterMap();
			if (parameters.size() != 0) {
				log.append(" - [");
			}
			for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				String message = "";
				if (value.getClass().isArray()) {
					Object[] args = (Object[]) value;
					message = " " + key + "=" + Arrays.toString(args) + " ";
				} else {
					message = key + "=" + (String) value + " ";
				}
				log.append(message);
			}
			if (parameters.size() != 0) {
				log.append("]");
			}
		}
		logger.info(log.toString());

		return true;
	}
	
	/**
	 * 进入controller后, 在返回的时候调用此方法
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		request.setAttribute("_contextPath", request.getContextPath());
		String serverName = "http://" + request.getServerName();
		String serverPort = ":" + request.getServerPort();
		String httpPath = serverName + serverPort ;
		request.setAttribute("_serverPath", httpPath);
		
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

}


