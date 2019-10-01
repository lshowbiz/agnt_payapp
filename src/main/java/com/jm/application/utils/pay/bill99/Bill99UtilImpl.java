package com.jm.application.utils.pay.bill99;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.jm.application.entity.JfiPayLog;
import com.jm.application.utils.pay.IPayUtil;
import com.jm.application.utils.pay.PayUtils;
import com.jm.application.utils.pay.RemarkBean;
import com.jm.application.utils.web.RequestUtil;

/**
 * PC快钱支付证书加载、加密、验签入账处理业务实现类
 * 
 * @author Administrator
 * 
 */
public class Bill99UtilImpl implements IPayUtil {
	@Override
	public String[] getConfiguration() {
		// TODO Auto-generated method stub
		return new String[] { "1,快钱", "29,30", "<result>1</result><redirecturl>http://e.jmtop.com/gate</redirecturl>" };
	}

	@Override
	public JfiPayLog getJfiPayLog(HttpServletRequest request, String companyCode) {
		JfiPayLog entity = this.setJfiPayLog(request, companyCode);
		if (StringUtils.isEmpty(entity.getUserCode())) {
			return null;
		}
		String bindCard = request.getParameter("bindCard");
		String bindMobile = request.getParameter("bindMobile");
		return this.checkJfiPayLog(entity, bindCard, bindMobile);
	}

	private JfiPayLog setJfiPayLog(HttpServletRequest request, String companyCode) {
		JfiPayLog entity = new JfiPayLog();

		String remark = request.getParameter("ext2");
		
		
		entity.setRemarkBean(PayUtils.getRemarkBean(remark));
		// BigDecimal fee = new BigDecimal(ext2[2]);//手续费
		// 扩展字段2，该值与提交时相同。
		entity.setRemark(remark);
		entity.setUserCode(entity.getRemarkBean().getUserCode());
		entity.setFlag(entity.getRemarkBean().getPayType());
		entity.setInc("0");
		entity.setCompanyCode(companyCode);
		entity.setIp(RequestUtil.getIpAddr(request));
		entity.setUrl(request.getRequestURL().toString() + "?" + RequestUtil.paramStr(request));
		entity.setUrlRemark("merchantAcctId:人民币网关账号,version:网关版本,language:语言种类,signType:签名类型,payType:支付方式,bankId:银行代码"
				+ ",orderId:商户订单号,orderTime:订单提交时间,orderAmount:订单金额,dealId:快钱交易号,bankDealId:银行交易号,dealTime:快钱交易时间"
				+ ",payAmount:商户实际支付金额,fee:手续费,ext1:扩展字段1,ext2:扩展字段2,payResult:处理结果,errCode:错误代码,signMsg:签名字符串;");
		// 人民币网关账号，该账号为11位人民币网关商户编号+01,该值与提交时相同。
		entity.setMerchantId(request.getParameter("merchantAcctId"));
		// 网关版本，固定值：v2.0,该值与提交时相同。
		entity.setVersion(request.getParameter("version"));
		// 签名类型,该值为4，代表PKI加密方式,该值与提交时相同。
		entity.setSignType(request.getParameter("signType"));
		// 支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10,该值与提交时相同。
		entity.setPayType(request.getParameter("payType"));
		// 银行代码，如果payType为00，该值为空；如果payType为10,该值与提交时相同。
		entity.setBankId(request.getParameter("bankId"));
		// 商户订单号，该值与提交时相同。
		entity.setOrderId(request.getParameter("orderId"));
		// 订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101,该值与提交时相同。
		entity.setOrderTime(request.getParameter("orderTime"));
		// 订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试,该值与支付时相同。
		entity.setOrderAmount(request.getParameter("orderAmount"));
		// 快钱交易号，商户每一笔交易都会在快钱生成一个交易号。
		entity.setDealId(request.getParameter("dealId"));
		// 银行交易号 ，快卡支付钱交易在银行支付时对应的交易号，如果不是通过银行，则为空
		entity.setBankDealId(request.getParameter("bankDealId"));
		// 处理结果， 10支付成功，11 支付失败，00订单申请成功，01 订单申请失败
		entity.setPayResult(request.getParameter("payResult"));
		// 错误代码 ，请参照《人民币网关接口文档》最后部分的详细解释。
		entity.setErrCode(request.getParameter("errCode"));
		// 语言种类，1代表中文显示，2代表英文显示。默认为1,该值与提交时相同。
		entity.setAmtType(request.getParameter("language"));
		// 快钱交易时间，快钱对交易进行处理的时间,格式：yyyyMMddHHmmss，如：20071117020101
		entity.setOrderDate(request.getParameter("dealTime"));
		// 费用，快钱收取商户的手续费，单位为分。
		entity.setFee(request.getParameter("fee"));
		// 扩展字段1，该值与提交时相同。
		entity.setErrMsg(request.getParameter("ext1"));
		// 商户实际支付金额 以分为单位。比方10元，提交时金额应为1000。该金额代表商户快钱账户最终收到的金额。
		// String payAmount = request.getParameter("payAmount");
		// 签名字符串
		entity.setSign(request.getParameter("signMsg"));
		
		RemarkBean bean = PayUtils.getRemarkBean(entity.getRemark());
		if (bean != null) {
			entity.setRemarkBean(bean);
			entity.setDataType(bean.getDataType());// 数据来源，1：PC
		}
		return entity;
	}

	private JfiPayLog checkJfiPayLog(JfiPayLog entity, String bindCard, String bindMobile) {
		SimpleDateFormat format = new SimpleDateFormat(datFmt);
		String merchantSignMsgVal = "";
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "merchantAcctId", entity.getMerchantId());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "version", entity.getVersion());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "language", entity.getAmtType());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "signType", entity.getSignType());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "payType", entity.getPayType());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankId", entity.getBankId());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderId", entity.getOrderId());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderTime", entity.getOrderTime());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderAmount", entity.getOrderAmount());

		merchantSignMsgVal = appendParam(merchantSignMsgVal, "bindCard", bindCard); // 手机端签名字段
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "bindMobile", bindMobile); // 手机端签名字段

		merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealId", entity.getDealId());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankDealId", entity.getBankDealId());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealTime", entity.getOrderDate());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "payAmount", entity.getOrderAmount());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "fee", entity.getFee());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext1", entity.getErrMsg());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext2", entity.getRemark());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "payResult", entity.getPayResult());
		merchantSignMsgVal = appendParam(merchantSignMsgVal, "errCode", entity.getErrCode());

		Pkipair pki = new Pkipair();
		int rtnMsg = 0;
		boolean flag = pki.enCodeByCer(merchantSignMsgVal, entity.getSign(), entity.getDataType());
		if (flag) {
			rtnMsg = Integer.parseInt(entity.getPayResult()) == 10 ? 10 : 2;
		} else {
			rtnMsg = 4;// 签名被篡改
		}
		entity.setReturnMsg(String.valueOf(rtnMsg));
		entity.setCreateTime(format.format(new Date()));
		// entity.setDataType("1");// 数据来源，1：PC
		entity.setPayType("1");// 快钱编码\
		
		
		BigDecimal orderFee = new BigDecimal(entity.getOrderAmount()).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_UP);
		entity.setOrderAmount(orderFee.toString());
		
		return entity;
	}

	private String appendParam(String returnStr, String paramId, String paramValue) {
		if (returnStr!=null && !returnStr.equals("")) {
			if (paramValue!=null && !paramValue.equals("")) {
				returnStr = returnStr + "&" + paramId + "=" + paramValue;
			}
		} else {
			if (!paramValue.equals("")) {
				returnStr = paramId + "=" + paramValue;
			}
		}
		return returnStr;
	}

}
