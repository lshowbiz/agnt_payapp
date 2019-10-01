package com.jm.application.utils.pay.yspay;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import yspay.util.SignUtil;
import yspay.util.base64.Base64;

import com.jm.application.entity.JfiPayLog;
import com.jm.application.utils.pay.IPayUtil;
import com.jm.application.utils.pay.PayUtils;
import com.jm.application.utils.web.RequestUtil;

@SuppressWarnings("unchecked")
public class YspayUtilImpl implements IPayUtil {
	private final Log log = LogFactory.getLog(YspayUtilImpl.class);

	public static final String KEY_NAME = "key/businessgate.cer";// 加密文件

	@Override
	public String[] getConfiguration() {
		return new String[] { "8,银盛", "170,2", "0000" };
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
		JfiPayLog entity = null;
		try {
			request.setCharacterEncoding("GBK");
			String src = request.getParameter("src");// 支付方商户号
			String msgCode = request.getParameter("msgCode");
			String msgId = request.getParameter("msgId");
			String check = request.getParameter("check");
			String msg = request.getParameter("msg");
			log.info("【接收银盛支付后台通知数据】.............[src=" + src + ",msgCode=" + msgCode + ",msgId=" + msgId + ",check=" + check + ",msg=" + msg + "]");

			log.info("【接收银盛支付后台通知数据】.............[,msg=" + new String(Base64.decode(msg), "GBK").length() + "]");

			String xml = new String(Base64.decode(msg), "GBK"); // 解析xml
			Document doc = DocumentHelper.parseText(xml);
			Element element = doc.getRootElement();
			Map<String, String> map = new HashMap<String, String>();
			getElementList(element, map);

			String reserved = map.get("Remark");// 商户附加信息中存储【用户编码，支付方式】
			//String[] ext = reserved.substring(reserved.indexOf("[") + 1, reserved.indexOf("]")).split(",");
			//reserved = "[{userCode:\""+ext[0]+"\",payType:\""+ext[1]+"\",payFee:0,dataType:2,isFull:false,merchantId:\""+ext[2]+"\"}]";
			if (StringUtils.isNotEmpty(reserved)) {
				entity = new JfiPayLog();
				entity.setRemarkBean(PayUtils.getRemarkBean(reserved));
				entity.setUserCode(entity.getRemarkBean().getUserCode());
				entity.setFlag(entity.getRemarkBean().getPayType());
				entity.setMerchantId(entity.getRemarkBean().getMerchantId());// [商户号]
				//entity.setRemarkBean(bean);
				/*ext = reserved.substring(reserved.indexOf("[") + 1, reserved.indexOf("]")).split(",");
				entity.setUserCode(ext[0]);
				entity.setFlag(ext[1]);
				entity.setMerchantId(ext[2]);// [商户号]*/
				entity.setInc("0");
				entity.setCompanyCode(companyCode);
				entity.setUrl(request.getRequestURL().toString() + "?" + RequestUtil.paramStr(request));
				log.info(entity.getUrl());
				entity.setIp(RequestUtil.getIpAddr(request));
				entity.setDataType("1");
				entity.setPayType("8"); // 银盛支付

				entity.setUrlRemark(xml + "======Ver[版本],Src[支付者商户号],MsgCode[支付版本],Time[支付时间],OrderId[商家订单号],ShopDate[商户日期],"
						+ "Cur[币种（CNY）],Amount[交易金额],Remark[备注(其中包含用户编码、支付方式、商户号)],BusiState[业务状态],Code[返回码],Note[返回码说明],TradeSN[交易流水]");
				// MsgCode[支付版本]// Fee[]
				entity.setRemark(reserved);// Remark[备注]
				entity.setPayResult(map.get("BusiState"));// BusiState[业务状态]
				entity.setErrCode(map.get("Code"));// Code[返回码]
				entity.setErrMsg(map.get("Note"));// Note[返回码说明]
				entity.setExpireTime(map.get("Time"));// Time[支付时间]
				entity.setOrderTime(map.get("Time"));// Time[支付时间]
				BigDecimal OrderAmount = new BigDecimal(map.get("Amount"));// Amount[交易金额]
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				entity.setOrderAmount(decimalFormat.format(OrderAmount.divide(new BigDecimal(100))));
				entity.setOrderDate(map.get("ShopDate"));// ShopDate[商户日期]
				entity.setOrderId(map.get("OrderId"));// OrderId[商家订单号]
				entity.setAmtType(map.get("Cur"));// Cur[币种（CNY）]
				entity.setBankDealId("");// 银行流水号
				entity.setBankId("");// 直连银行编码
				entity.setMerkey(check);// 秘药
				entity.setSign(msg);
				entity.setSignType("businessgate.cer"); // 签名方式
				entity.setVersion(map.get("Ver"));// Ver[版本]
				entity.setDealId(map.get("TradeSN"));// // TradeSN[银行交易流水号]
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return entity;
	}

	private JfiPayLog checkJfiPayLog(JfiPayLog entity) {
		SimpleDateFormat format = new SimpleDateFormat(datFmt);
		int rtnMsg = 0;
		// 验证签名
		try {
			String path = this.getClass().getClassLoader().getResource(KEY_NAME).getPath();
			//path = "D:/cert/businessgate.cer";
			SignUtil.verifyXml(entity.getSign(), entity.getMerkey(), path);
			if ("00".equals(entity.getPayResult())) {// 订单付款成功了
				rtnMsg = 10;// 验签成功
			} else {
				rtnMsg = 1;// 交易失败
			}
		} catch (Exception e) {
			e.printStackTrace();
			rtnMsg = 4;// 签名被篡改
		}
		entity.setReturnMsg(String.valueOf(rtnMsg));
		entity.setCreateTime(format.format(new Date()));
		entity.setDataType("1");// 数据来源，1：PC
		return entity;
	}

	/**
	 * 递归解析xml对象
	 * 
	 * @param element
	 * @param map
	 */
	public void getElementList(Element element, Map<String, String> map) {
		List<Element> elements = element.elements();// 所有一级子节点的list
		if (element.elements().size() == 0) {
			map.put(element.getName(), element.getText());
		} else {
			for (Element e : elements) {// 遍历所有一级子节点
				getElementList(e, map);// 递归
			}
		}

	}

	public static void main(String[] args) {
		String xml = "<?xml version='1.0' encoding='GBK'?><yspay><aa>fds</aa><head><Ver>1.0</Ver><Src>ysepay</Src><MsgCode>R3501</MsgCode><Time>20151106105414</Time></head><body>"
				+ "<Order><OrderId>2015110611015411015418101810</OrderId><ShopDate>20190716</ShopDate><Cur>CNY</Cur><Amount>50000</Amount><Remark></Remark></Order>"
				+ "<Result><BusiState>00</BusiState><Code>0000</Code><Note>即时到账支付成功</Note><TradeSN>31115110612549576143</TradeSN><Fee>200</Fee></Result>" + "</body></yspay>";

		String check = "klaUy/poztY4GfnEJ/yuoei0Y86O2Zwc5XGlN9WQeV/mV6MGWJM6FjxMFAz2nAWPkkhmfHAsvQdLZTHPBVw4SLt+8yW5amQykzgRTHDM57ftQDEKlEFGaQKHT0DuEX98DDYx88XlCj5/NUoulfkp8s+3iWoUNJANx3A5IdpcJbw=                                                                                    ";
		String msg = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iR0JLIj8+Cjx5c3BheT48aGVhZD48VmVyPjEuMDwvVmVyPjxTcmM+eXNlcGF5PC9TcmM+PE1zZ0NvZGU+UjM1MDE8L01zZ0NvZGU+PFRpbWU+MjAxNTExMDkxMTIzMDE8L1RpbWU+PC9oZWFkPjxib2R5PjxPcmRlcj48T3JkZXJJZD4yMDE1MTEwOTExMzAzNzExMzAzNzUzNTk1MzU5PC9PcmRlcklkPjxTaG9wRGF0ZT4yMDE5MDcxNjwvU2hvcERhdGU+PEN1cj5DTlk8L0N1cj48QW1vdW50PjUwMDAwPC9BbW91bnQ+PFJlbWFyaz5bQ045NDIyMDEyMiwwXTwvUmVtYXJrPjwvT3JkZXI+PFJlc3VsdD48QnVzaVN0YXRlPjAwPC9CdXNpU3RhdGU+PENvZGU+MDAwMDwvQ29kZT48Tm90ZT68tMqxtb3Vy9anuLazybmmPC9Ob3RlPjxUcmFkZVNOPjMxMTE1MTEwOTEyNTQ5NjY2MTk2PC9UcmFkZVNOPjxGZWU+MjAwPC9GZWU+PC9SZXN1bHQ+PC9ib2R5PjwveXNwYXk+";

		check = "klaUy/poztY4GfnEJ/yuoei0Y86O2Zwc5XGlN9WQeV/mV6MGWJM6FjxMFAz2nAWPkkhmfHAsvQdLZTHPBVw4SLt+8yW5amQykzgRTHDM57ftQDEKlEFGaQKHT0DuEX98DDYx88XlCj5/NUoulfkp8s+3iWoUNJANx3A5IdpcJbw=                                                                                  ";
		msg = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iR0JLIj8+Cjx5c3BheT48aGVhZD48VmVyPjEuMDwvVmVyPjxTcmM+eXNlcGF5PC9TcmM+PE1zZ0NvZGU+UjM1MDE8L01zZ0NvZGU+PFRpbWU+MjAxNTExMDkxMTIzMDE8L1RpbWU+PC9oZWFkPjxib2R5PjxPcmRlcj48T3JkZXJJZD4yMDE1MTEwOTExMzAzNzExMzAzNzUzNTk1MzU5PC9PcmRlcklkPjxTaG9wRGF0ZT4yMDE5MDcxNjwvU2hvcERhdGU+PEN1cj5DTlk8L0N1cj48QW1vdW50PjUwMDAwPC9BbW91bnQ+PFJlbWFyaz5bQ045NDIyMDEyMiwwXTwvUmVtYXJrPjwvT3JkZXI+PFJlc3VsdD48QnVzaVN0YXRlPjAwPC9CdXNpU3RhdGU+PENvZGU+MDAwMDwvQ29kZT48Tm90ZT68tMqxtb3Vy9anuLazybmmPC9Ob3RlPjxUcmFkZVNOPjMxMTE1MTEwOTEyNTQ5NjY2MTk2PC9UcmFkZVNOPjxGZWU+MjAwPC9GZWU+PC9SZXN1bHQ+PC9ib2R5PjwveXNwYXk+";

		try {

			System.out.println(Base64.decode(check).length);
			SignUtil.verifyXml(msg, check, "D:/cert/businessgate.cer");

			Document doc = DocumentHelper.parseText(xml);
			Element element = doc.getRootElement();
			Map<String, String> map = new HashMap<String, String>();
			YspayUtilImpl y = new YspayUtilImpl();
			y.getElementList(element, map);
			for (String key : map.keySet()) {
				System.out.println(key + "=" + map.get(key));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
