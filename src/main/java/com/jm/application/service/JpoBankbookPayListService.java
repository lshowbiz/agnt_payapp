package com.jm.application.service;

import java.util.List;

import com.jm.application.entity.JpoBankbookPayList;

public interface JpoBankbookPayListService {
	public List<JpoBankbookPayList> findEntitys(JpoBankbookPayList entity);

	public JpoBankbookPayList getEntity(Long id);

	public void saveEntity(JpoBankbookPayList entity);

	public void removeEntity(Long id);
}
