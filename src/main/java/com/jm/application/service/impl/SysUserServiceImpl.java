package com.jm.application.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jm.application.dao.HibernateDAO;
import com.jm.application.entity.SysUser;
import com.jm.application.service.SysUserService;

@Service("sysUserService")
@SuppressWarnings("unchecked")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class SysUserServiceImpl implements SysUserService {
	@Resource(name = "hibernateDAO")
	private HibernateDAO dao;

	@Override
	public List<SysUser> findEntitys(SysUser entity) {
		Object values = new Object[10];
		String hql = "SysUser from where 1=1 ";

		return (List<SysUser>) dao.find(hql, values);
	}

	@Override
	public SysUser getEntity(String userCode) {
		return (SysUser) dao.findById(SysUser.class, userCode);
	}

	@Override
	public void saveEntity(SysUser entity) {
		// TODO Auto-generated method stub
		dao.saveOrUpdate(entity);
	}

	@Override
	public void removeEntity(String userCode) {
		// TODO Auto-generated method stub
		dao.deleteByKey(userCode, SysUser.class);
	}
}
