package com.jm.config;

import org.springframework.context.ApplicationContext;

/** 
 *Description: <类功能描述>. <br>
 *<p>
	自定义bean获取类,
	例:JM_SpringContext.getBean("beanName");
 </p>                        
 */
public class JM_SpringContext {
	/** 
	* @Fields applicationContext : TODO(applicationContext) 
	*/ 
	private static ApplicationContext applicationContext;

	private static JM_SpringContext springContext;

	private JM_SpringContext() {
	}

	public static JM_SpringContext getInstance() {
		if (springContext == null) {
			springContext = new JM_SpringContext();
		}
		return springContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}

	/**                                                          
	* 描述 : <获得applicationContext中的对象>. <br> 
	*<p> 
		<使用方法说明>  
	 </p>                                                                                                                                                                                                                                                
	* @param beanName
	* @return                                                                                                      
	*/  
	public static Object getBean(String beanName) {
		return JM_SpringContext.getInstance().getApplicationContext().getBean(beanName);
	}
}


