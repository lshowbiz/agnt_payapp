package com.jm.application.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jm.application.dao.HibernateDAO;
import com.jm.application.entity.JfiAmountDetail;
import com.jm.application.entity.JfiPayLog;
import com.jm.application.entity.JfiQuota;
import com.jm.application.service.JfiPayLogService;

@Service("jfiPayLogService")
@SuppressWarnings("unchecked")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class JfiPayLogServiceImpl implements JfiPayLogService {

	@Resource(name = "hibernateDAO")
	private HibernateDAO dao;

	@Override
	public List<JfiPayLog> findEntitys(JfiPayLog entity) {
		String hql = " FROM JfiPayLog WHERE 1=1 ";
		if (StringUtils.isNotEmpty(entity.getDealId())) {
			hql += "AND dealId = '" + entity.getDealId() + "'";
		}
		if (StringUtils.isNotEmpty(entity.getOrderId())) {
			hql += "AND orderId = '" + entity.getOrderId() + "'";
		}
		if (StringUtils.isNotEmpty(entity.getInc())) {
			hql += "AND inc = '" + entity.getInc() + "'";
		}
		if (StringUtils.isNotEmpty(entity.getPayType())) {
			hql += "AND payType = '" + entity.getPayType() + "'";
		}
		if (StringUtils.isNotEmpty(entity.getReturnMsg())) {//查询是否有验签成功的记录
			hql += "AND returnMsg = '" + entity.getReturnMsg() + "'";
		}
		return (List<JfiPayLog>) dao.find(hql);
	}

	@Override
	public JfiPayLog getEntity(Long logId) {
		return (JfiPayLog) dao.findById(JfiPayLog.class, logId);
	}

	@Override
	public void saveEntity(JfiPayLog entity) {
		// TODO Auto-generated method stub
		dao.saveOrUpdate(entity);
	}

	@Override
	public void removeEntity(Long logId) {
		// TODO Auto-generated method stub
		dao.deleteByKey(logId, JfiPayLog.class);
	}

	public List<Map<String, Object>> getJpoMemberOrder(String moid) {
		String sql = "select * from jpo_member_order a where a.mo_id='" + moid + "'";
		return dao.getListMapBySql(sql);
	}

	@Override
	public int updateJpoMemberOrderIsFull(String moid) {
		String sql = "UPDATE JPO_MEMBER_ORDER SET ISFULL_PAY = 1 WHERE MEMBER_ORDER_NO='" + moid + "'";
		return dao.updateBySQL(sql);
	}

	@Override
	public JfiQuota getJfiQuota(JfiQuota jfiQuota, String merchantId) {
		String hql = "from JfiQuota a where a.status=0 ";
		if (StringUtils.isNotEmpty(jfiQuota.getValidityPeriod())) {
			hql += " and a.validityPeriod ='" + jfiQuota.getValidityPeriod() + "'";
		}
		if (jfiQuota.getAccountId() != null) {
			hql += " and a.accountId =" + jfiQuota.getAccountId();
		}
		if (StringUtils.isNotEmpty(merchantId)) {
			hql += " and a.fiBillAccount.billAccountCode ='" + merchantId + "'";
		}
		List<JfiQuota> list = (List<JfiQuota>) dao.findAllByHQL(hql);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	/*@Override
	public JfiQuota getJfiQuota2(JfiQuota jfiQuota, String merchantId) {
		String hql = "select * from jfi_quota a,fi_bill_account b where a.account_id=b.account_id and  a.status=0 ";
		if (StringUtils.isNotEmpty(jfiQuota.getValidityPeriod())) {
			hql += " and a.validity_period='" + jfiQuota.getValidityPeriod() + "' ";
		}
		if (jfiQuota.getAccountId() != null) {
			hql += " and a.account_id='" + jfiQuota.getAccountId()+"' ";
		}
		if (StringUtils.isNotEmpty(merchantId)) {
			hql += " and b.bill_account_code='" + merchantId + "' ";
		}
		List<Map<String, Object>> listMap = dao.getListMapBySql(hql);
		return list.size() > 0 ? list.get(0) : null;
	}*/

	@Override
	public String getJfiQuota2(JfiQuota jfiQuota, String merchantId) {
		String quotaId = "";
		String hql = "select a.quota_id QUOTA_ID from jfi_quota a,fi_bill_account b where a.account_id=b.account_id and  a.status=0 ";
		if (StringUtils.isNotEmpty(jfiQuota.getValidityPeriod())) {
			hql += " and a.validity_period='" + jfiQuota.getValidityPeriod() + "' ";
		}
		if (jfiQuota.getAccountId() != null) {
			hql += " and a.account_id='" + jfiQuota.getAccountId()+"' ";
		}
		if (StringUtils.isNotEmpty(merchantId)) {
			hql += " and b.bill_account_code='" + merchantId + "' ";
		}
		System.out.println("hql:"+hql);
		List<Map<String, Object>> listMap = dao.getListMapBySql(hql);
		if (listMap.size() > 0) {
			Map<String, Object> map = listMap.get(0);
			quotaId = map.get("QUOTA_ID").toString();
		}
		return quotaId;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void saveJfiAmountDetail(JfiAmountDetail jfiAmountDetail) {
		List list = dao.findAllByHQL("from JfiAmountDetail a where a.memberOrderNo='" + jfiAmountDetail.getMemberOrderNo()+"' ");
		if (list == null || (list!=null && list.size() ==0)) {
			dao.saveOrUpdate(jfiAmountDetail);
			updateJpoMemberOrderIsFull(jfiAmountDetail.getMemberOrderNo());
		}
	}
	
	@Override
	public int updateJpoMemberOrderPaymentType(String moid) {
		String sql = "UPDATE JPO_MEMBER_ORDER SET PAYMENT_TYPE = '2' WHERE MO_ID='" + moid + "'";
		return dao.updateBySQL(sql);
	}
	
	@Override
	public List<Map<String, Object>> getJpoMemberOrderTemp(String moid) {
		String sql = "select * from jpo_member_order_temp a where a.mo_id='" + moid + "'";
		return dao.getListMapBySql(sql);
	}
}
