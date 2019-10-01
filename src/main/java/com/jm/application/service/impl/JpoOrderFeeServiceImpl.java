package com.jm.application.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jm.application.dao.HibernateDAO;
import com.jm.application.entity.JpoMemberOrderFee;
import com.jm.application.service.JpoOrderFeeService;

@Service("jpoOrderFeeService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class JpoOrderFeeServiceImpl implements JpoOrderFeeService {
	
	@Resource(name = "hibernateDAO")
	private HibernateDAO dao;

	@Override
	public JpoMemberOrderFee getFeeByMoid(Long moid) {
		String hql = "from JpoMemberOrderFee where moId=?";
		List<JpoMemberOrderFee> feeList = 
				(List<JpoMemberOrderFee>) dao.find(hql, moid);
		JpoMemberOrderFee fee = new JpoMemberOrderFee();
		if(! feeList.isEmpty()){
			fee = feeList.get(0);
		}
		return fee;
	}
}
