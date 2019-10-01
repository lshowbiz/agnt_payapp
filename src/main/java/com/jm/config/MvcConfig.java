package com.jm.config;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 描述: springMVC 配置
 * <p>
 * 	类似applicationContext.xml,在启动时加载
 * </p>
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.jm.application", useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})
})
public class MvcConfig extends WebMvcConfigurationSupport {
	
	private static final Logger logger = Logger
			.getLogger(MvcConfig.class);

    /**                                                          
    * 描述 : <注册试图处理器>. <br>                                                                                                                                                                                                                                               
    * @return                                                                                                      
    */  
    @Bean
    public ViewResolver viewResolver() {
    	logger.info("ViewResolver");
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
    
    /**                                                          
    * 描述 : <注册消息资源处理器>. <br>                                                                                                                                                                                                                                              
    * @return                                                                                                      
    */  
    @Bean
    public MessageSource messageSource() {
    	logger.info("MessageSource");
    	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    	messageSource.setBasename("properties.messages");
    	
    	return messageSource;
    }
    
    /**                                                          
    * 描述 : <注册servlet适配器>. <br>                                                                                                                                                                                                                                               
    * @return                                                                                                      
    */  
    @Bean
    public HandlerAdapter servletHandlerAdapter(){
    	logger.info("HandlerAdapter");
    	return new SimpleServletHandlerAdapter();
    }
    
    /**                                                          
    * 描述 : <本地化拦截器>. <br>                                                                                                                                                                                                                                               
    * @return                                                                                                      
    */  
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
    	logger.info("LocaleChangeInterceptor");
    	
    	LocaleChangeInterceptor localInterceptor = new LocaleChangeInterceptor();
    	localInterceptor.setParamName("UTF-8");
    	
    	return localInterceptor;
    }
    
    /**                                                          
     * 描述 : <RequestMappingHandlerMapping需要显示声明，否则不能注册自定义的拦截器>. <br> 
     *  RequestMappingHandlerMapping这个类的对象，只能在controller层用，
     *	并且要在申明了@RequestMapping的方法里面用 <br>
     *	想查看应用请求对应的url和方法，可以用RequestMappingHandlerMapping这个类来处理                                                                                                                                                                                                                                                
     * @return                                                                                                      
     */ 
    @Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
    	logger.info("RequestMappingHandlerMapping");
		
		return super.requestMappingHandlerMapping();
	}

    /**                                                          
    * 描述 : <添加拦截器>. <br> 
    * 
    * 这里可以添加自定义的拦截器                                                                                                                                                                                                                                              
    * @param registry                                                                                                      
    */  
    @Override
	protected void addInterceptors(InterceptorRegistry registry) {
		
    	logger.info("addInterceptors start");
		registry.addInterceptor(localeChangeInterceptor());
		logger.info("addInterceptors end");
	}

    /**                                                          
     * 描述 : <注册资源访问处理器>. <br>                                                                                                                                                                                                                                            
     * @return                                                                                                      
     */ 
    @Bean
	public HandlerMapping resourceHandlerMapping() {
    	logger.info("HandlerMapping");
    	return super.resourceHandlerMapping();
    }
    
    /**                                                          
     * 描述 : <资源访问处理器>. <br>                                                                                                                                                                                                                                                
     * @param registry                                                                                                      
     */  
	@Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		logger.info("addResourceHandlers");
        registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/");
    }
    
	/**                                                          
	* 描述 : <文件上传处理器>. <br>                                                                                                                                                                                                                                            
	* @return                                                                                                      
	*/  
	@Bean(name="multipartResolver")
	public CommonsMultipartResolver commonsMultipartResolver(){
		logger.info("CommonsMultipartResolver");
		return new CommonsMultipartResolver();
	}
	
	/**                                                          
	* 描述 : <异常处理器>. <br> 
	* <系统运行时遇到指定的异常将会跳转到指定的页面>                                                                                                                                                                                                                                                
	* @return                                                                                                      
	*/  
	@Bean(name="exceptionResolver")
	public JM_SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
		logger.info("SimpleMappingExceptionResolver");
		JM_SimpleMappingExceptionResolver simpleMappingExceptionResolver= new JM_SimpleMappingExceptionResolver();
		simpleMappingExceptionResolver.setDefaultErrorView("/common/error");
		simpleMappingExceptionResolver.setExceptionAttribute("exception");
		Properties properties = new Properties();
		properties.setProperty("java.lang.RuntimeException>>>", "error");
		simpleMappingExceptionResolver.setExceptionMappings(properties);
		return simpleMappingExceptionResolver;
	}
	
	 /**                                                          
     * 描述 : <否则不能注册通用属性编辑器>. <br>                                                                                                                                                                                                                                               
     * @return                                                                                                      
     */ 
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		logger.info("RequestMappingHandlerAdapter");
    	return super.requestMappingHandlerAdapter();
	}
	
	/**                                                          
	* 描述 : <注册通用属性编辑器>. <br>                                                                                                                                                                                                                                                
	* @return                                                                                                      
	*/  
	@Override
	protected ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer() {
		logger.info("ConfigurableWebBindingInitializer");
		ConfigurableWebBindingInitializer initializer = super.getConfigurableWebBindingInitializer();
		JM_PropertyEditorRegistrar register = new JM_PropertyEditorRegistrar();
		register.setFormat("yyyy-MM-dd");
		initializer.setPropertyEditorRegistrar(register);
		return initializer;
	}
    
    
}

