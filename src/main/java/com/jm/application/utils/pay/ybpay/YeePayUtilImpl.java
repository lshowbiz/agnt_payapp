package com.jm.application.utils.pay.ybpay;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jm.application.entity.JfiPayLog;
import com.jm.application.utils.pay.IPayUtil;
import com.jm.application.utils.pay.PayUtils;
import com.jm.application.utils.web.RequestUtil;

public class YeePayUtilImpl implements IPayUtil {

	private final Log log = LogFactory.getLog(YeePayUtilImpl.class);

	// public static final String keyValue =
	// "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

	@Override
	public String[] getConfiguration() {
		// TODO Auto-generated method stub
		return new String[] { "5,易宝 ", "140,2", "SUCCESS" };
	}

	@Override
	public JfiPayLog getJfiPayLog(HttpServletRequest request, String companyCode) {
		JfiPayLog entity = this.setJfiPayLog(request, companyCode);
		if (StringUtils.isEmpty(entity.getUserCode())) {
			return null;
		}
		return this.checkJfiPayLog(request, entity);
	}

	private JfiPayLog setJfiPayLog(HttpServletRequest request, String companyCode) {
		JfiPayLog entity = new JfiPayLog();
		/*String[] ext2 = request.getParameter("r8_MP").split(",");
		String userCode = ext2[0];// 获取付款会员身份
		String flag = ext2[1];// 标识：0充值, 1订单支付, 2公益基金订单支付
		 */
		entity.setRemark(request.getParameter("r8_MP"));
		entity.setRemarkBean(PayUtils.getRemarkBean(entity.getRemark()));
		entity.setUserCode(entity.getRemarkBean().getUserCode());// 会员编号
		entity.setFlag(entity.getRemarkBean().getPayType());
		
		entity.setInc("0");
		entity.setCompanyCode(companyCode);
		entity.setIp(RequestUtil.getIpAddr(request));
		log.info(request.getRequestURL().toString() + "?" + RequestUtil.paramStr(request));
		entity.setUrl(request.getRequestURL().toString() + "?" + RequestUtil.paramStr(request));
		entity.setUrlRemark("p1_MerId:'商户编号',r0_Cmd:'业务类型',r1_Code:'支付结果(固定值：1-代表支付成功)',r2_TrxId:'易宝交易流水号',r3_Amt:'支付金额',r4_Cur:'交易币种',r5_Pid:'商品名称',r6_Order:'商户订单号',r7_Uid:'易宝支付会员ID',r8_MP:'商户扩展信息(会员编号，订单类型)',r9_BType:'通知类型',rb_BankId:'支付通道编码','ro_BankOrderId:银行订单号',rp_PayDate:'支付成功时间',rq_CardNo:'神州行充值卡号',ru_Trxtime:'通知时间',rq_SourceFee:'用户手续费',rq_TargetFee:'商户手续费',hmac:'签名数据'");
		entity.setMerchantId(request.getParameter("p1_MerId"));// 商户号
		entity.setOrderId(request.getParameter("r6_Order"));// 订单ID
		entity.setOrderAmount(request.getParameter("r3_Amt"));// 商户订单金额，单位为元
		entity.setOrderTime(request.getParameter("rp_PayDate"));// 商户订单提交时间,格式：yyyy-MM-ddhh:mm:ss
		entity.setDealId(request.getParameter("r2_TrxId"));// 易宝平台流水
		entity.setOrderDate(request.getParameter("rp_Trxtime"));// 易宝平台交易时间
		entity.setBankDealId(request.getParameter("ro_BankOrderId"));// 银行处理流水
		entity.setBankId(request.getParameter("rb_BankId"));// 银行编号
		entity.setAmtType(request.getParameter("r4_Cur"));// 付款金额，单位为元
		entity.setPayResult(request.getParameter("r1_Code"));// 支付结果, 1：为成功；
		entity.setSign(request.getParameter("hmac"));// 签名字符串
		entity.setFee(request.getParameter("rq_TargetFee"));// 手续费
		return entity;
	}

	private JfiPayLog checkJfiPayLog(HttpServletRequest request, JfiPayLog entity) {
		String p1_MerId = formatString(request.getParameter("p1_MerId"));
		String r0_Cmd = formatString(request.getParameter("r0_Cmd"));
		String r1_Code = formatString(request.getParameter("r1_Code"));// 支付结果，1成功
		String r2_TrxId = formatString(request.getParameter("r2_TrxId"));// 流水
		String r3_Amt = formatString(request.getParameter("r3_Amt"));
		String r4_Cur = formatString(request.getParameter("r4_Cur"));
		String r5_Pid = formatString(request.getParameter("r5_Pid"));
		String r6_Order = formatString(request.getParameter("r6_Order"));// 订单号
		String r7_Uid = formatString(request.getParameter("r7_Uid"));
		String r8_MP = formatString(request.getParameter("r8_MP"));
		String r9_BType = formatString(request.getParameter("r9_BType"));
		String hmac = formatString(request.getParameter("hmac"));
		String[] strArr = { p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType };
		boolean flag = verifyCallbackHmac(strArr, hmac, getKeyByMerId(p1_MerId));
		if (flag) {
			entity.setReturnMsg("10");
		} else {
			entity.setReturnMsg("4");
		}
		entity.setCreateTime(format.format(new Date()));
		entity.setDataType("1");// 数据来源，1：PC
		entity.setPayType("5");// 易宝编码
		return entity;
	}

	private String formatString(String text) {
		return (text == null) ? "" : text.trim();
	}

	public static boolean verifyCallbackHmac(String[] stringValue, String hmacFromYeepay, String keyValue) {
		StringBuffer sourceData = new StringBuffer();
		for (int i = 0; i < stringValue.length; i++) {
			sourceData.append(stringValue[i]);
		}
		String localHmac = DigestUtil.getHmac(stringValue, keyValue);

		StringBuffer hmacSource = new StringBuffer();
		for (int i = 0; i < stringValue.length; i++) {
			hmacSource.append(stringValue[i]);
		}
		return (localHmac.equals(hmacFromYeepay) ? true : false);
	}

	/**
	 * 根据商户号获取key
	 * 
	 * @param merId
	 * @return
	 */
	public String getKeyByMerId(String merId) {
		Map<String, String> map = new HashMap<String, String>();
		//Modify By WuCF 20160714 新增商户号
		map.put("10012530614", "4m35T3y50a02Dd6ZdGc0r7Lu865NQ4c5wJp1283f91Be7p4tr6g3L3Fh0oh0");//武汉吉康福贸易有限公司
		map.put("10012530568", "q9z762Qa260z35121tU9UI7668nTut1mp75WUf474jAsz09oC16w5TG89220");//重庆丽翔贸易有限公司
		
		map.put("10012475322", "XLrJ33M177YX2B857Jv3XwKyNi9C9AmhpboG0p7391U7Mzao6X1pH7g0YKo1");//河南	
		map.put("10012474429", "6133ZSjo43BojuQydX60966RkgJT5X2TFETs92n6641u865T46245729R20c");// 徐州佰兰德商贸有限公司  广东
		map.put("10012552798", "W2jW2B31d1212444e45sqSb4H5r78o9Ro8p66680v5i82oFF2Rgr629898h1");// 重庆美森商贸有限责任公司
		map.put("10012553418", "IVMPb75eS6f0q7DT9YhD50koKUdA53q5hzn158Dh6917VV4P2ow4B4085Cxd");// 哈密市小本商贸有限责任公司
		map.put("10012553444", "3a4aFRv82K3KjJ7A562q47zs2p2R852Ds7YbN51Wf3G62wXb09oN91591tYq");// 成武县圣和商贸有限公司
		map.put("10012553386", "iyd8Jec5EL81uI80v5G5p753iF5E4GNvHG36WJ721D0B98PL8924056760vY");// 潍坊乌拉商贸有限公司
		map.put("10012563264", "438mv0Lo38n05Ghjr9B57644F91dr62PW5Ah56G67x6pe48694U77pX8RTt0");
		map.put("10012562338", "92j716Vu3cX62b2b79z1PS787Asr392Y81B97V5Y7jw2f6690y433F9xy92U");
		map.put("10012562222", "4Z49WX140x30t5q01T0vT8Vol72qIM1y1Q1JFH05m8978r73Z5urtfsnVq10");
		map.put("10012562279", "2VI46B90e1Esf103ZEy8p5hK3G9Z5728476U6sV226n8T8j98kj0z4C0i2w4");
		map.put("10012562324", "JJ0r270IVF9Je58pP245tY66JL8B8lP9av555604c0MZ3s90NQ7Thn42u477");
		map.put("10012562193", "2vBuu845Rq84Wl33Yt8939G3oz4H087QoEl5Fv5G0B221lh001v3N49214i5");
		map.put("10012562366", "24S0kbu8jm7o50yJ8N1726pqUjjPJ6046358802lUf1g554136742p03W9o6");
		map.put("10012592595", "l5Qk3N39Bf71l2X3FW1uK5oR98D0Y885319gL325x2vW9HaRl474k8pm2Xco");
		
		map.put("10012563284", "M1j8N7HW6gT9uU746t477495bzV23D724iV3Ij0tN42056782J1HS4F3o926");//大连欧德玛贸易有限公司
		map.put("10012530575", "0l80Ja8V04n2YHP096y2J6ob94LiA5i7Ys41X9lyt4tl49S8BU5T545wZ5h8");//四川省智能生态家商贸有限责任公司
		map.put("10012530553", "0vG6K118rBjEAfiZe5D83pn9y2qst99T8R2iSf6F7S5UQ0g22235mjtp8661");//成都梦娇商贸有限公司
		
		return map.get(merId);
	}

}
