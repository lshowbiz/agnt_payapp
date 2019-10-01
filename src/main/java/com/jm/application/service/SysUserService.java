package com.jm.application.service;

import java.util.List;

import com.jm.application.entity.SysUser;

public interface SysUserService {
	public List<SysUser> findEntitys(SysUser entity);

	public SysUser getEntity(String userCode);

	public void saveEntity(SysUser entity);

	public void removeEntity(String userCode);
}
