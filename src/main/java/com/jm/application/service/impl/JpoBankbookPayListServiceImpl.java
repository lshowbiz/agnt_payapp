package com.jm.application.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jm.application.dao.HibernateDAO;
import com.jm.application.entity.JpoBankbookPayList;
import com.jm.application.service.JpoBankbookPayListService;

@SuppressWarnings("unchecked")
@Service("jpoBankbookPayListService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class JpoBankbookPayListServiceImpl implements JpoBankbookPayListService {
	@Resource(name = "hibernateDAO")
	private HibernateDAO dao;

	@Override
	public List<JpoBankbookPayList> findEntitys(JpoBankbookPayList entity) {
		Object values = new Object[10];
		String hql = "JpoBankbookPayList from where 1=1 ";

		return (List<JpoBankbookPayList>) dao.find(hql, values);
	}

	@Override
	public JpoBankbookPayList getEntity(Long logId) {
		return (JpoBankbookPayList) dao.findById(JpoBankbookPayList.class, logId);
	}

	@Override
	public void saveEntity(JpoBankbookPayList entity) {
		// TODO Auto-generated method stub
		dao.saveOrUpdate(entity);
	}

	@Override
	public void removeEntity(Long logId) {
		// TODO Auto-generated method stub
		dao.deleteByKey(logId, JpoBankbookPayList.class);
	}
}
