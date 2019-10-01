package com.jm.application.service;

import com.jm.application.entity.JpoMemberOrder;

public interface JpoOrderService {
	public JpoMemberOrder getOrderById(Long id);
	
	public JpoMemberOrder getJpoMemberOrder(String moId);
}
