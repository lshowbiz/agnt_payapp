package com.jm.application.service;

import com.jm.application.entity.JpoMemberOrderFee;

public interface JpoOrderFeeService {

	public JpoMemberOrderFee getFeeByMoid(Long moid);
}
