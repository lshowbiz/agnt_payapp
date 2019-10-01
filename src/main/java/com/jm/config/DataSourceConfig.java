package com.jm.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/** 
 *描述: <类功能描述>. <br>
 *<p>
	DataSource
 </p>                            
 */
@Configuration
//加载资源文件
@PropertySource({"classpath:/properties/db.properties"})
public class DataSourceConfig {
	private static final Logger logger = Logger.getLogger(DataSourceConfig.class);
	/*
	 * 绑定资源属性
	 */
	@Value("${jdbc.driver}")
	String driverClass;
	
	@Value("${jdbc.url}")
	String url;
	
	@Value("${jdbc.username}")
	String userName;
	
	@Value("${jdbc.password}")
	String passWord;
	
	@Value("${maxpool.size}")
	int maxpoolSize;
	
	@Value("${init.poolsize}")
	int initPoolSize;
	
	@Value("${checkout.timeout}")
	int checkoutTimeout;
	
	@Value("${acquireIncrement}")
	int acquireIncrement;
	
//	@Value("${preferredTestQuery}")
//	String preferredTestQuery;
//	
//	@Value("${testConnectionOnCheckin}")
//	boolean testConnectionOnCheckin;
//	
//	@Value("${idleConnectionTestPeriod}")
//	int idleConnectionTestPeriod;
//	
//	@Value("${acquireRetryAttempts}")
//	int acquireRetryAttempts;
//	
//	@Value("${acquireRetryDelay}")
//	int acquireRetryDelay;
//	
//	@Value("${initialSize}")
//	int initialSize;
//	@Value("${minIdle}")
//	int minIdle;
//	@Value("${maxActive}")
//	int maxActive;
//	@Value("${maxWait}")
//	int maxWait;
//	@Value("${timeBetweenEvictionRunsMillis}")
//	int timeBetweenEvictionRunsMillis;
//	@Value("${minEvictableIdleTimeMillis}")
//	int minEvictableIdleTimeMillis;
//	@Value("${maxPoolPreparedStatementPerConnectionSize}")
//	int maxPoolPreparedStatementPerConnectionSize;
//	@Value("${validationQuery}")
//	String validationQuery;
//	@Value("${testWhileIdle}")
//	Boolean testWhileIdle;
//	
//	@Value("${testOnBorrow}")
//	Boolean testOnBorrow;
//	@Value("${testOnReturn}")
//	Boolean testOnReturn;
//	@Value("${poolPreparedStatements}")
//	Boolean poolPreparedStatements;
//	@Value("${filters}")
//	String filters;
	
	@Bean(name = "dataSource")
	public DataSource dataSource() {
		logger.info("DruidDataSource DataSource  Start, userName:["+userName+"] pwd is:"+passWord);
		DataSource dataSource = null;
		
		try{
			//切换为DruidData    houxy
			 Properties p =new Properties();  
	         p.put("initialSize", "30");  
	         p.put("minIdle", "15");  
	         p.put("maxActive", "200");  
	         p.put("maxWait", "50000");  
	         p.put("timeBetweenEvictionRunsMillis", "60000");  
	         p.put("minEvictableIdleTimeMillis", "300000");             
	         p.put("validationQuery", "SELECT 'x' from dual");  
	         p.put("testWhileIdle", "true");  
	         p.put("testOnBorrow", "false");  
	         p.put("testOnReturn", "false");  
	         p.put("poolPreparedStatements", "true");  
	         p.put("maxPoolPreparedStatementPerConnectionSize", "20");  
	         p.put("filters", "stat");  
	         p.put("url", url);  
             p.put("username", userName);  
             p.put("password", passWord);  
			
	         dataSource = DruidDataSourceFactory.createDataSource(p);
	         
	         logger.info("DruidDataSource DataSource "+dataSource);
			
//			logger.info("c3p0 create DataSource, userName:[" + this.userName + "] pwd is:" + this.passWord);
//			dataSource.setDriverClass(this.driverClass);
//	         dataSource.setJdbcUrl(this.url);
//	         dataSource.setUser(this.userName);
//	         dataSource.setPassword(this.passWord);
//	         dataSource.setMaxPoolSize(this.maxpoolSize);
//	         dataSource.setInitialPoolSize(this.initPoolSize);
//	         dataSource.setCheckoutTimeout(this.checkoutTimeout);
//	         dataSource.setAcquireIncrement(this.acquireIncrement);
//	         dataSource.setMinPoolSize(15);
//	         dataSource.setIdleConnectionTestPeriod(1000);
//	         原来生产用
	         
			
			
//			dataSource.setDriverClass(driverClass);
//			dataSource.setJdbcUrl(url);
//			dataSource.setUser(userName);
//			dataSource.setPassword(passWord);
//			dataSource.setMaxPoolSize(maxpoolSize);
//			dataSource.setInitialPoolSize(initPoolSize);
//			dataSource.setCheckoutTimeout(checkoutTimeout);
//		
//			dataSource.setAcquireIncrement(acquireIncrement);
//			dataSource.setPreferredTestQuery(preferredTestQuery);
//			dataSource.setTestConnectionOnCheckin(testConnectionOnCheckin);
//			dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
//			dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
//			dataSource.setAcquireRetryDelay(acquireRetryDelay);
		
		}catch(Exception e){
			logger.error("DataSource connetion error.",e);
		}
		return dataSource;
	}
}


