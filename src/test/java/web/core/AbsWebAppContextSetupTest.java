/*
 * COPYRIGHT Beijing NetQin-Tech Co.,Ltd.                                   *
 ****************************************************************************
 * 源文件名:  web.function.controller.AbsWebAppContextSetupTest.java 													       
 * 功能: cpframework框架													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年9月1日 下午3:34:18 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年9月1日    |    Administrator     |     Created 
 */
package web.core;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.jm.config.AppConfig;
import com.jm.config.MvcConfig;

@SuppressWarnings("deprecation")
// 注解风格
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@ContextHierarchy({ @ContextConfiguration(name = "parent", classes = AppConfig.class), @ContextConfiguration(name = "child", classes = MvcConfig.class) })
public abstract class AbsWebAppContextSetupTest {
	@Autowired
	public WebApplicationContext wac;

	public MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	public WebApplicationContext getWac() {
		return wac;
	}

}
