package com.jm.application.service;

import java.util.List;

import com.jm.application.entity.TestDEMO;

/** 
 *Description: <类功能描述>. <br>
 *<p>
	<使用说明>
 </p>                         
 */
public interface TestDemoService {

	public List<TestDEMO> findAll();
	
	public TestDEMO getById(long id);
	
	public void save(TestDEMO demo);
	
	public void delete(TestDEMO demo);
	
	/**
	 * 仅限执行数据库中function
	 * @return list
	 */
	public List<String> execfunction();
}


