package com.jm.application.utils.pay.channel;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import chinapnr.SecureLink;

import com.jm.application.entity.JfiPayLog;
import com.jm.application.utils.pay.IPayUtil;
import com.jm.application.utils.pay.PayUtils;
import com.jm.application.utils.web.RequestUtil;

public class ChinapnrUtilImpl implements IPayUtil {

	private final Log log = LogFactory.getLog(ChinapnrUtilImpl.class);

	public static final String KEY_NAME = "key/PgPubk.key";//

	@Override
	public String[] getConfiguration() {
		return new String[] { "6,汇付天下 ", "130,2", "000000" };
	}

	@Override
	public JfiPayLog getJfiPayLog(HttpServletRequest request, String companyCode) {
		JfiPayLog entity = this.setJfiPayLog(request, companyCode);
		if (StringUtils.isEmpty(entity.getUserCode())) {
			return null;
		}
		return this.checkJfiPayLog(entity);
	}

	private JfiPayLog setJfiPayLog(HttpServletRequest request, String companyCode) {
		JfiPayLog entity = new JfiPayLog();
		try {
			request.setCharacterEncoding("gbk");
			String remark = request.getParameter("MerPriv");
			/*String userCode = "";
			String flag = "";
			if (StringUtils.isNotEmpty(remark)) {
				String[] ext2 = remark.substring(remark.indexOf("[") + 1, remark.indexOf("]")).split(",");
				userCode = ext2[0];
				flag = ext2[1];
			}*/
			entity.setRemarkBean(PayUtils.getRemarkBean(remark));
			entity.setUserCode(entity.getRemarkBean().getUserCode());
			entity.setFlag(entity.getRemarkBean().getPayType());

			entity.setInc("0");
			entity.setCompanyCode(companyCode);
			entity.setUrl(request.getRequestURL().toString() + "?" + RequestUtil.paramStr(request));
			log.info(entity.getUrl());
			entity.setUrlRemark("CmdId:'消息类型',MerId:'商户号',RespCode:'应答返回码',TrxId:'钱管家交易唯一标识',OrdAmt:'金额',CurCode:'币种',Pid:'商品编号',OrdId:'订单号',MerPriv:'商户私有域',RetType:'返回类型',DivDetails:'分账明细',GateId:'银行ID',ChkValue:'签名信息 ',remark:'备注【用户编码，支付方式】'");
			entity.setMerchantId(request.getParameter("MerId")); // MerId商户编号
			entity.setDealId(request.getParameter("TrxId"));// 钱管家交易唯一标识
			entity.setOrderId(request.getParameter("OrdId"));// 订单编号
			entity.setOrderAmount(request.getParameter("OrdAmt"));// 订单金额
			entity.setAmtType(request.getParameter("CurCode"));// 币种，
			entity.setPayResult(request.getParameter("RespCode"));// state状态：000000→成功相符；其它→失败；
			entity.setSign(request.getParameter("ChkValue"));// 签名信息
			entity.setSignType(request.getParameter("CmdId"));// 消息类型
			entity.setBankId(request.getParameter("GateId"));// paybank 支付银行
			entity.setVersion("10"); // interface版本号
			entity.setErrCode(request.getParameter("Pid")); // interface版本号
			entity.setIp(RequestUtil.getIpAddr(request));
			entity.setRemark(request.getParameter("MerPriv") + request.getParameter("RetType") + request.getParameter("DivDetails")); // 分账明细
			entity.setPayType("6");
		} catch (Exception e) {
			entity = null;
			e.printStackTrace();
		}
		return entity;
	}

	private JfiPayLog checkJfiPayLog(JfiPayLog entity) {
		SimpleDateFormat format = new SimpleDateFormat(datFmt);
		StringBuffer sb = new StringBuffer();
		sb.append(entity.getSignType()); // CmdId
		sb.append(entity.getMerchantId());// MerId
		sb.append(entity.getPayResult());// RespCode
		sb.append(entity.getDealId());// TrxId
		sb.append(entity.getOrderAmount());// OrdAmt
		sb.append(entity.getAmtType());// CurCode
		sb.append(entity.getErrCode());// Pid
		sb.append(entity.getOrderId());// OrdId
		sb.append(entity.getRemark());// MerPriv + RetType + DivDetails
		sb.append(entity.getBankId());// GateId
		// CmdId + MerId + RespCode + TrxId + OrdAmt + CurCode + Pid + OrdId +
		// MerPriv + RetType + DivDetails + GateId;
		int rtnMsg = 1;
		if ("000000".equals(entity.getPayResult())) {
			String path = "";//"F:/cert/hf"+KEY_NAME;//
			path = this.getClass().getClassLoader().getResource(KEY_NAME).getPath();
			SecureLink sl = new SecureLink();
			int ret = sl.VeriSignMsg(path, sb.toString(), entity.getSign());
			if (ret != 0) {
				entity.setSignType("KEY_NAME" + ret);
				rtnMsg = 4;// 签名被篡改
			} else {
				rtnMsg = 10;// 验签成功

			}
		}
		entity.setReturnMsg(String.valueOf(rtnMsg));
		entity.setCreateTime(format.format(new Date()));
		entity.setDataType("1");// 数据来源，1：PC
		return entity;
	}

}
