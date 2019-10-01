package com.jm.application.utils.pay.ipspay;

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
import com.jm.application.utils.pay.RemarkBean;
import com.jm.application.utils.pay.yspay.YspayUtilImpl;
import com.jm.application.utils.web.RequestUtil;

@SuppressWarnings("unchecked")
public class IpsPayUtilImpl implements IPayUtil{
	private final Log log = LogFactory.getLog(IpsPayUtilImpl.class);

	public String[] getConfiguration() {
		return new String[] { "9,环迅", "200,2", "000000" };
	}

	public JfiPayLog getJfiPayLog(HttpServletRequest request, String companyCode) {
		JfiPayLog entity = this.setJfiPayLog(request, companyCode);
		if (StringUtils.isEmpty(entity.getUserCode())) {
			return null;
		}
		return this.checkJfiPayLog(entity,request);
	}

	private JfiPayLog setJfiPayLog(HttpServletRequest request, String companyCode) {
		JfiPayLog entity = null;
		try {
			request.setCharacterEncoding("UTF-8");
        	System.out.println( "环迅支付请求地址========================"+RequestUtil.paramStr(request));
        	log.info( "环迅支付请求地址========================"+RequestUtil.paramStr(request));
        	
        	//获取环迅返回值，xml结构
            String paymentResult = request.getParameter("paymentResult");
            log.info("req content is ---------------------------------:" + paymentResult);
            
            //获取并解析文件XML
            String attachJson = PayUtils.getHxAtt(paymentResult);
            RemarkBean bean = PayUtils.getRemarkBean(attachJson);
            HxPayBean hxBean = PayUtils.getHxXmlToHxObj(paymentResult);
            
            //创建对象,并设置值
            entity = new JfiPayLog();
            entity.setRemarkBean(bean);
			entity.setUserCode(bean.getUserCode());//会员编号 
			entity.setFlag(bean.getPayType());//充值&支付  
			entity.setMerchantId(bean.getMerchantId());// 
			entity.setInc("0");//默认未进存折  
			entity.setCompanyCode(companyCode);//国别 
			entity.setUrl(request.getRequestURL().toString() + "?" + RequestUtil.paramStr(request));//URL
			entity.setIp(RequestUtil.getIpAddr(request));//IP地址 
			entity.setDataType("1");//PC端支付 
			entity.setPayType("9"); // 环迅支付  
			
			entity.setUrlRemark("");
			entity.setRemark(attachJson);// Remark[备注]
			entity.setPayResult(hxBean.getRspCode());// BusiState[业务状态] 
			entity.setErrCode(hxBean.getRspCode());// Code[返回码]
			entity.setErrMsg(hxBean.getRspMsg());// Note[返回码说明]
			entity.setExpireTime(hxBean.getIpsBillTime());// Time[支付时间]
			entity.setOrderTime(hxBean.getDate());// Time[支付时间]
			
			log.info("hxBean.getAmount():"+hxBean.getAmount());
			BigDecimal OrderAmount = new BigDecimal(hxBean.getAmount());// Amount[交易金额]
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			entity.setOrderAmount(decimalFormat.format(OrderAmount));
			
			entity.setOrderDate(hxBean.getDate());// ShopDate[商户日期]
			entity.setOrderId(hxBean.getMerBillNo());// OrderId[商家订单号]
			entity.setAmtType(hxBean.getCurrencyType());// Cur[币种（CNY）] --2
			entity.setBankDealId(hxBean.getBankBillNo());// 银行流水号
			entity.setBankId("");// 直连银行编码
			entity.setMerkey(hxBean.getRetEncodeType());// 秘药
			entity.setSign(hxBean.getSignature());//签名加密
			entity.setSignType(hxBean.getRetEncodeType()); // 签名方式 --9
			entity.setVersion("1.0");// Ver[版本]
			entity.setDealId(hxBean.getIpsBillNo());// // TradeSN[银行交易流水号] --8
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return entity;
	}

	/**
	 * 验证签名数据
	 * @param entity
	 * @param request
	 * @return
	 */
	private JfiPayLog checkJfiPayLog(JfiPayLog entity,HttpServletRequest request) {
		SimpleDateFormat format = new SimpleDateFormat(datFmt);
		int rtnMsg = 0;
		// 验证签名
		try {
			//环迅支付签名获取字段
			String paymentResult = request.getParameter("paymentResult");
			
			//签名算法返回值
			boolean signResult = PayUtils.getSign(paymentResult,entity.getMerchantId());
			
			if(signResult){
				if ("000000".equals(entity.getPayResult())) {// 订单付款成功了
					rtnMsg = 10;// 验签成功
				} else {
					rtnMsg = 1;// 交易失败
				}
			}else{
				rtnMsg = 4;// 签名被篡改
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
