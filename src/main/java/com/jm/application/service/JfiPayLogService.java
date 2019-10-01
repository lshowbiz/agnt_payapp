package com.jm.application.service;

import java.util.List;
import java.util.Map;

import com.jm.application.entity.JfiAmountDetail;
import com.jm.application.entity.JfiPayLog;
import com.jm.application.entity.JfiQuota;

public interface JfiPayLogService {

	public List<JfiPayLog> findEntitys(JfiPayLog entity);

	public JfiPayLog getEntity(Long logId);

	public void saveEntity(JfiPayLog entity);

	public void removeEntity(Long logId);

	public List<Map<String, Object>> getJpoMemberOrder(String moid);

	public int updateJpoMemberOrderIsFull(String moid);

	public JfiQuota getJfiQuota(JfiQuota jfiQuota, String merchantId);
	
	public String getJfiQuota2(JfiQuota jfiQuota, String merchantId);

	public void saveJfiAmountDetail(JfiAmountDetail jfiAmountDetail);

	//Add By WuCF 20160805
	public int updateJpoMemberOrderPaymentType(String moid);
	
	//Add By WuCF 20160825
	public List<Map<String, Object>> getJpoMemberOrderTemp(String moid);
}
