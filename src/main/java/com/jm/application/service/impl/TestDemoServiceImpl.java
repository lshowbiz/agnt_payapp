package com.jm.application.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jm.application.dao.HibernateDAO;
import com.jm.application.entity.TestDEMO;
import com.jm.application.service.TestDemoService;


/** 
 *Description: <类功能描述>. <br>
 *启用注解事务，默认策略是所有方法都必须在事务中运行，必须声明在实现类上，接口声明无效
 *不加事务会报异常：No Session found for current thread，
 *所以不能使用@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)                         
 */
@Service("demoService")
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor={Exception.class})
public class TestDemoServiceImpl implements TestDemoService{
	
	private static final Logger logger = Logger
			.getLogger(TestDemoServiceImpl.class);
	
	@Resource(name="hibernateDAO")
	private HibernateDAO dao;
	@Resource(name="dataSource")
	public DataSource dataSource;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TestDEMO> findAll() {
		// TODO Auto-generated method stub
		logger.info("List<TestDEMO> findAll()");
		return (List<TestDEMO>)dao.findAll(TestDEMO.class);
	}

	@Override 
	public TestDEMO getById(long id) {
		// TODO Auto-generated method stub
		logger.info("TestDEMO getById(int id)|| id==" + id);
		return (TestDEMO)dao.findById(TestDEMO.class, id);
	}

	@Override  
	public void save(TestDEMO TestDEMO) {
		// TODO Auto-generated method stub
		//测试异常拦截
//		Integer.valueOf("hei");
		
		dao.save(TestDEMO);
	}

	@Override 
	public void delete(TestDEMO TestDEMO) {
		// TODO Auto-generated method stub
		dao.delete(TestDEMO);
	}

	@Override
	public List<String> execfunction() {
		/**
			方式一只适用于function
			String sql = "select Fn_Build_jSys_Id('1') from dual";
			List<String> list = (List<String>)dao.getListBySql(sql);
		*/
		
		/**  
		 *  方式二 适用function和procedure
		*	String sql = "Fn_Build_jSys_Id";
		*	JMStoredProcedure sp = new JMStoredProcedure(dataSource,sql,true);
		*	sp.setOutParameter("out_p", oracle.jdbc.OracleTypes.VARCHAR);
		*	sp.setParameter("Vc_Id_Code", java.sql.Types.CHAR);
		*
		*	// 传入输入参数值
		*	Map<String, Object> inComp = new HashMap<String, Object>();
		*	inComp.put("Vc_Id_Code", "1");
		
		*	sp.SetInParam(inComp);
		*	// 执行存储过程
		*	Map resultComp = sp.execute();
		*	System.out.println(resultComp); 
		*/
		
		return null;
	}

	
	

}


