package com.jm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;

/** 
* 描述: <应用配置类>. <br>
* <p>
*	<负责注册除Controller等web层以外的所有bean，包括aop代理，service层，dao层，缓存，等等>
* </p>                     
*/  
@Configuration
@ComponentScan(
		basePackages = "com.jm.application", 
		excludeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION,value = { Controller.class }) })
//@EnableAspectJAutoProxy(proxyTargetClass=true)
@Import({DaoConfig.class})
public class AppConfig {
	



}
