package com.jm.application.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jm.application.dao.HibernateDAO;
import com.jm.application.entity.JpoMemberOrder;
import com.jm.application.service.JpoOrderService;

@Service("jpoOrderService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
public class JpoOrderServiceImpl implements JpoOrderService{
	
	@Resource(name = "hibernateDAO")
	private HibernateDAO dao;

	@Override
	public JpoMemberOrder getOrderById(Long id) {
		JpoMemberOrder order = (JpoMemberOrder) dao.findById(JpoMemberOrder.class, id);
		return order;
	}
	
	@Override
	public JpoMemberOrder getJpoMemberOrder(String moId){
		JpoMemberOrder jpoMemberOrder = new JpoMemberOrder();
		//只需要返回3个字段即可
		String sql = "SELECT MO_ID,MEMBER_ORDER_NO,AMOUNT2 FROM JPO_MEMBER_ORDER_TEMP WHERE MO_ID='"+moId+"'";
		List<Map<String, Object>> listMap = dao.getListMapBySql(sql);
		if (listMap.size() > 0) {
			Map<String, Object> map = listMap.get(0);
			jpoMemberOrder.setMoId(Long.parseLong(map.get("MO_ID").toString()));
			jpoMemberOrder.setMemberOrderNo(map.get("MEMBER_ORDER_NO").toString());
			jpoMemberOrder.setAmount2(new BigDecimal(map.get("AMOUNT2") + ""));
		}
		return jpoMemberOrder;
	}
}
