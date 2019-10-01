package com.jm.application.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jm.application.entity.JfiAmountDetail;
import com.jm.application.entity.JfiPayLog;
import com.jm.application.entity.JfiQuota;
import com.jm.application.entity.JpoBankbookPayList;
import com.jm.application.entity.JpoMemberOrder;
import com.jm.application.entity.JpoMemberOrderFee;
import com.jm.application.entity.SysUser;
import com.jm.application.service.JfiPayLogService;
import com.jm.application.service.JpoBankbookPayListService;
import com.jm.application.service.JpoOrderFeeService;
import com.jm.application.service.JpoOrderService;
import com.jm.application.service.SysUserService;
import com.jm.application.utils.pay.IPayUtil;
import com.jm.application.utils.pay.PayFactory;
import com.jm.application.utils.pay.PayUtils;
import com.jm.application.utils.pay.RemarkBean;
import com.jm.application.utils.web.RequestUtil;

/**
 * 所有支付后台接收总控
 * 
 * @author lzg /payReceive.html
 */
@Controller
public class JfipayReceiveController {

	private final Log log = LogFactory.getLog(JfipayReceiveController.class);

	@Autowired
	private SysUserService sysService;
	@Autowired
	private JfiPayLogService payLogService;
	@Autowired
	private JpoBankbookPayListService bankbookPayListService;
	@Autowired
	private JpoOrderService jpoOrderService;
	@Autowired
	private JpoOrderFeeService jpoOrderFeeService;
	
//	private IPayUtil payUtil = null;//Modify By Wucf 20160803 修改成局部变量

	@RequestMapping("/payReceive")
	public void payReceive(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("=========第三方请求支付地址===============");
		log.info("======JfipayReceiveController:post--："+request.getRequestURL().toString() + "?" + RequestUtil.paramStr(request));
    	log.info("======JfipayReceiveController:get--："+request.getRequestURL().toString() + "?" + request.getQueryString());
		String[] config = null;
		JfiPayLog entity = null;
		try {
			String payType = request.getParameter("zmType");
			log.info("======payType:"+payType);
			IPayUtil payUtil = PayFactory.payFirm(payType);
			if (payUtil == null) {
				throw new RuntimeException("请求参数错误");
			}
			entity = payUtil.getJfiPayLog(request, "CN");
			config = payUtil.getConfiguration();
			if (entity == null) {
				throw new RuntimeException("请求参数错误,解析请求失败");
			}
			SysUser sysUser = sysService.getEntity(entity.getUserCode());
			if (sysUser == null) {
				throw new RuntimeException("请求参数错误,会员不存在");
			}
			//==================查询并判断是否已经支付==========
			JfiPayLog seach = new JfiPayLog();
//			seach.setInc("1"); 不用加此提交查询
			seach.setPayType(entity.getPayType());
			seach.setDealId(entity.getDealId());
			seach.setOrderId(entity.getOrderId());
			seach.setReturnMsg("10");//查询是否有验签成功的记录
			if (payLogService.findEntitys(seach).size() > 0) {
				entity.setReturnMsg("3");//已经进账
			}
			payLogService.saveEntity(entity);
			log.info("bankbook insert start2:"+entity.getOrderId());
			//==================查询并判断是否已经支付==========
			if ("10".equals(entity.getReturnMsg())) {
				JpoBankbookPayList bankBook = new JpoBankbookPayList();
				JpoMemberOrder order = null;
				
				bankBook.setUserCode(entity.getUserCode());
				bankBook.setUserSpCode(entity.getUserCode());
				if ("1".equals(entity.getFlag())) {// 支付类型 0:充值,1:支付,2:基金支付
					//支付才能得到订单信息
					order = jpoOrderService.getOrderById(Long.valueOf(entity.getOrderId()));
					
					List<Map<String, Object>> listMap = payLogService.getJpoMemberOrder(entity.getOrderId());
					log.info("order===========:"+order);
					//Modify By WuCF 20160825如果订单获取不到信息，从备份表中获取
					String moIdDel = null;//如果订单删除
					if(order==null || (order!=null && order.getMoId()==null)){
						order = jpoOrderService.getJpoMemberOrder(entity.getOrderId());
						listMap = payLogService.getJpoMemberOrderTemp(entity.getOrderId());
						if (listMap.size() > 0) {
							Map<String, Object> mapT = listMap.get(0);
							moIdDel = mapT.get("MEMBER_ORDER_NO") + "";
						}
					}
					log.info("order2===========:"+order);
					log.info("listMap===========:"+listMap);
					if (listMap.size() > 0) {
						Map<String, Object> map = listMap.get(0);
						if(moIdDel!=null && !"".equals(moIdDel)){
							bankBook.setMemberOrderNo(moIdDel);
						}else{
							bankBook.setMemberOrderNo(map.get("MEMBER_ORDER_NO") + "");
						}
						bankBook.setUserSpCode(map.get("USER_SP_CODE") + "");
						bankBook.setTeamCode(map.get("TEAM_CODE") + "");
						bankBook.setOrderType(map.get("ORDER_TYPE") + "");
						
						JpoMemberOrderFee fee = jpoOrderFeeService.getFeeByMoid(order.getMoId());
//						bankBook.setNotes(config[0] + "支付,支付单号：" + entity.getOrderId() +
//								" 其中物流费:"+fee.getFee().toString());
						/*bug:2886*/
						bankBook.setNotes("订单审核：" + map.get("MEMBER_ORDER_NO"));
						
						RemarkBean bean = PayUtils.getRemarkBean(entity.getRemark());
						if (bean.getIsFull() && !"1".equals(map.get("ISFULL_PAY"))) {
							getFullpay(entity, map);
						}
					}
					
					log.info("orderNo is:["+order.getMemberOrderNo()+"] "
							+ "and amount2 is:["+order.getAmount2()+"] "
									+ "and amount is:["+order.getAmount()+"] "
											+ "and online pay:"+entity.getOrderAmount());
					if(order.getAmount2().compareTo(new BigDecimal(entity.getOrderAmount())) !=0 ){
						bankBook.setInType(6);
					} else {
						bankBook.setInType(1);
					}
					

				}else {
					bankBook.setMemberOrderNo(entity.getOrderId());
					bankBook.setNotes(config[0] + "充值,充值单号-：" + entity.getOrderId());
					//汇付天下 微信 支付宝
					if("61".equals(entity.getPayType())){
						bankBook.setNotes("61,汇付天下_微信 " + "充值,充值单号-：" + entity.getOrderId());
					}
					if("62".equals(entity.getPayType())){
						bankBook.setNotes("62,汇付天下_支付宝 " + "充值,充值单号-：" + entity.getOrderId());
					}
					if("2".equals(entity.getFlag())) {
						bankBook.setInType(21);  //充值（发展基金）
					}else{
						bankBook.setInType(8);
					}
				}
				bankBook.setCreateTime(new Date());
				bankBook.setLogId(entity.getLogId());
				bankBook.setPayType(entity.getPayType());
				if(entity.getFee()==null || "".equals(entity.getFee())){
					entity.setFee("0");
				}
				bankBook.setCheckUserCode(entity.getUserCode());//审核者
				bankBook.setFee(new BigDecimal(entity.getFee()));
				bankBook.setKqAmt(new BigDecimal(entity.getOrderAmount()));
				bankBook.setDataType(Integer.parseInt(entity.getDataType()));
				bankBook.setMoneyType1(Integer.parseInt(config[1].split(",")[0]));
				bankBook.setMoneyType2(Integer.parseInt(config[1].split(",")[1]));
				if(order!=null){
					bankBook.setDzAmt(order.getAmount2().subtract(new BigDecimal(entity.getOrderAmount())));
				}
				log.info("entity.getOrderId()===========:"+entity.getOrderId());
				//Modify By WuCF 20160822 如果返回的订单编号不为空，才写入中间表
				if(entity.getOrderId()!=null && !"".equals(entity.getOrderId())){
					bankbookPayListService.saveEntity(bankBook);/**/
				}
				
				log.info("bankbook insert stop2:"+entity.getOrderId());
			} else {
				// 不成功
				switch (Integer.parseInt(entity.getReturnMsg())) {
				case 0:// 数据被篡改
					config[2] = "数据被篡改";
					break;
				case 1:// 扣款失败
					config[2] = "扣款失败";
					break;
				case 2:// 自定义MD5签名被篡改(签名被破解)
					config[2] = "自定义MD5签名被篡改(签名被破解)";
					break;
				case 3:// 数据重新发送
					config[2] = "数据重新发送";
					break;
				}
			}
			//Modify By WuCF20160803 汇付返回的格式是：RECV_ORD_ID_订单编号
			if(payType.contains("hfpay")||payType.contains("hfmobile")){
				response.getWriter().write("RECV_ORD_ID_"+entity.getOrderId());
			}else{
				response.getWriter().write(config[2]);
			}
			log.info("updateJpoMemberOrderPaymentType start2:"+entity.getOrderId());
			//Modify By WuCF20160805 修改订单支付来源标示PAYMENT_TYPE 支付则修改支付类型标示，判断是手机端还是PC端
			if ("1".equals(entity.getFlag()) && "kqpayMobile".equals(payType)) {// 支付类型 0:充值,1:支付
				payLogService.updateJpoMemberOrderPaymentType(entity.getOrderId());
			}
			log.info("updateJpoMemberOrderPaymentType stop2:"+entity.getOrderId());
		} catch (Exception e) {
			log.info("=====失败的请求地址==========================" + RequestUtil.paramStr(request));
			e.printStackTrace();
			entity.setReturnMsg("2");
			config = new String[] { "", "", e.getMessage() };
			System.err.println("=====失败的请求地址==========================" + RequestUtil.paramStr(request));
		}
//		response.getWriter().write(config[2]);// 通知不用在从新发送数据
	}

	/**
	 * 修改是否全额支付
	 * 增加全额支付明细
	 * @param entity
	 * @param map
	 */
	private void getFullpay(JfiPayLog entity, Map<String, Object> map) {
		if (new BigDecimal(map.get("AMOUNT2") + "").compareTo(new BigDecimal(entity.getOrderAmount())) == 0) {
			JfiQuota quota = new JfiQuota();
			quota.setValidityPeriod(new SimpleDateFormat("yyyyMM").format(new Date()));
//			quota = payLogService.getJfiQuota(quota, entity.getMerchantId());
			String quotaId = payLogService.getJfiQuota2(quota, entity.getMerchantId());
			System.out.println("quotaId:"+quotaId);
			if (quotaId != null && !"".equals(quotaId)) {
				JfiAmountDetail amDetail = new JfiAmountDetail();
				amDetail.setCreateTime(new Date());
				amDetail.setUserCode(entity.getUserCode());
				amDetail.setMemberOrderNo(map.get("MEMBER_ORDER_NO") + "");
				amDetail.setMoney(entity.getOrderAmount());
				amDetail.setQuotaId(Long.parseLong(quotaId));
				payLogService.saveJfiAmountDetail(amDetail);// 增加明细
				//payLogService.updateJpoMemberOrderIsFull(map.get("MEMBER_ORDER_NO") + "");// 修改订单状态
			}
		}
	}

	public static void main(String[] args) {
		try {
			IPayUtil payUtil = PayFactory.payFirm("sypay");
			String[] config = payUtil.getConfiguration();
			System.out.println(config[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
