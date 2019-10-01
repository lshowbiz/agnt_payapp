package com.jm.application.utils.pay;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.jm.application.utils.pay.ipspay.HxPayBean;


public class PayUtils {
	
	public static RemarkBean getRemarkBean(String reserved) {
		RemarkBean entity = null;
		try {
			//Modify By WuCF 20161128 判断，如果json以[]开始结尾，则直接截取
			if(reserved!=null && reserved.trim().startsWith("[") && reserved.trim().endsWith("]")){
				reserved = reserved.substring(1,reserved.length()-1);
			}
			//汇付支付特殊情况，后面有个2
			if(reserved!=null && reserved.trim().startsWith("[") && reserved.trim().endsWith("]2")){
				reserved = reserved.substring(1,reserved.length()-2);
			}
			
			entity = getRemarkBeanToJson(reserved);
			if (entity == null) {
				entity = getRemarkBeanToArr(reserved);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return entity;
	}

	public static RemarkBean getRemarkBeanToJson(String reserved)  {
		RemarkBean entity = new RemarkBean();
		try {
			//Modify By WuCF 20161128 注释
//			String json = reserved.substring(reserved.indexOf("[") + 1, reserved.indexOf("]"));
			String json = reserved;
			JSONObject jsonObj = JSONObject.fromObject(json);
			entity = (RemarkBean) JSONObject.toBean(jsonObj, RemarkBean.class);
		} catch (Exception e) {
			System.out.println("getRemarkBeanToJson--解析错误--可以忽略" + reserved);
			e.printStackTrace();
			return null;
		}
		return entity;
	}
	
	public static RemarkBean getRemarkBeanToArr(String arrStr) throws RuntimeException {
		RemarkBean entity = new RemarkBean();
		try {
			//Modify By WuCF 20161128 注释
//			arrStr = arrStr.substring(arrStr.indexOf("[") + 1, arrStr.indexOf("]"));
			String[] reserved = arrStr.split(",");
			entity.setUserCode(reserved[0]);// 用户编号
			entity.setPayType(reserved[1]);// 支付类型0：充值，1支付
			entity.setZmType(reserved[2]);// 用于判断为那家支付公司
			entity.setDataType(reserved[3]);// 支付平台1:pc,2:手机
			entity.setMerchantId(reserved[4]);// 商户号
			entity.setIsFull(Boolean.valueOf(reserved[5]));// 是否全额支付
			entity.setPayFee(new BigDecimal(reserved[6])); // 手续费
		} catch (Exception e) {
			throw new RuntimeException("getRemarkBeanToArr--解析错误" + arrStr);
		}
		return entity;
	}
	
	/**
	 * Add By WuCF 20161125 
	 * 环迅支付数组解析
	 * @param arrStr
	 * @return
	 * @throws RuntimeException
	 */
	public static RemarkBean getRemarkBeanToArrIps(String arrStr) throws RuntimeException {
		RemarkBean entity = new RemarkBean();
		try {
//			arrStr = arrStr.substring(arrStr.indexOf("[") + 1, arrStr.indexOf("]"));
			String[] reserved = arrStr.split(",");
			entity.setUserCode(reserved[0]);// 用户编号
			entity.setPayType(reserved[1]);// 支付类型0：充值，1支付
			entity.setZmType(reserved[2]);// 用于判断为那家支付公司
			entity.setDataType(reserved[3]);// 支付平台1:pc,2:手机
			entity.setMerchantId(reserved[4]);// 商户号
			entity.setIsFull(Boolean.valueOf(reserved[5]));// 是否全额支付
			entity.setPayFee(new BigDecimal(reserved[6])); // 手续费
		} catch (Exception e) {
			throw new RuntimeException("getRemarkBeanToArr--解析错误" + arrStr);
		}
		return entity;
	}

	public static String getHxAtt(String reqXml) {
		Document doc = null;
		//自定义参数
		String att = null;
		try {
			doc = DocumentHelper.parseText(reqXml);
			Element ipsEle = doc.getRootElement(); // 获取根节点
			Element rspEle = ipsEle.element("GateWayRsp");
			Element bodyEle = rspEle.element("body");
			att = bodyEle.elementText("Attach");
		} catch (DocumentException e) {
			System.out.println("Attach is error");
		} 
		return att;
	}
	
	/**
	 * 环迅支付
	 * @param reqXml
	 * @return
	 */
	public static HxPayBean getHxXmlToHxObj(String reqXml) {
		Document doc = null;
		HxPayBean bean = new HxPayBean();
		try {
			doc = DocumentHelper.parseText(reqXml);
			Element ipsEle = doc.getRootElement(); // 获取根节点
			Element rspEle = ipsEle.element("GateWayRsp");
			Element bodyEle = rspEle.element("body");
			Element headEle = rspEle.element("head");
			bean.setReferenceID(headEle.elementText("ReferenceId"));//对应请求报文头的MsgId，只作为消息传递应答，丌作为签名验证的依据
			bean.setRspCode(headEle.elementText("RspCode"));//000000#请求响应成功，但丌作交易完成的依据
			bean.setRspMsg(headEle.elementText("RspMsg"));//响应消息说明
			bean.setReqDate(headEle.elementText("ReqDate"));//商户请求时间，对应请求报文头中的Date格式：yyyyMMddHHmmss
			bean.setRspDate(headEle.elementText("RspDate"));//报文响应时间格式：yyyyMMddHHmmss
			bean.setSignature(headEle.elementText("Signature"));//数字签名 17#MD5（默讣）
			bean.setMerBillNo(bodyEle.elementText("MerBillNo"));//商户订单号
			bean.setCurrencyType(bodyEle.elementText("CurrencyType"));//
			bean.setAmount(bodyEle.elementText("Amount"));//币种
			bean.setDate(bodyEle.elementText("Date"));//订单日期
			bean.setStatus(bodyEle.elementText("Status"));//支付状态
			bean.setMsg(bodyEle.elementText("Msg"));//支付详细信息
			bean.setAttach(bodyEle.elementText("Attach"));//组合字段详细信息(会员编号、支付&充值、商户号)
			bean.setIpsBillNo(bodyEle.elementText("IpsBillNo"));//IPS订单号 唯一标示
			bean.setIpsTradeNo(bodyEle.elementText("IpsTradeNo"));//IPS交易流水号
			bean.setBankBillNo(bodyEle.elementText("BankBillNo"));//银行订单号
			bean.setRetEncodeType(bodyEle.elementText("RetEncodeType"));//交易返回签名方式
			bean.setResultType(bodyEle.elementText("ResultType"));//支付结果返回方式 返回方式: 0 ( HTTP方式)
			bean.setIpsBillTime(bodyEle.elementText("IpsBillTime"));//IPS处理订单时间
		} catch (DocumentException e) {
			System.out.println("Attach is error");
		} 
		return bean;
	}
	
	/**
	 * 环迅支付验签校验
	 * @param reqXml
	 * @return
	 */
	public static boolean getSign(String reqXml,String merchantId)
	{
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(reqXml);
			Element ipsEle = doc.getRootElement(); // 获取根节点
			Element rspEle = ipsEle.element("GateWayRsp");
			Element bodyEle = rspEle.element("body");
			Element headEle = rspEle.element("head");
			// xml截取
			String signStr = reqXml.substring(reqXml.lastIndexOf("<body>"),
					reqXml.lastIndexOf("</body>") + 7);
			signStr = signStr + merchantId + getKeyByMerId(merchantId);
			String sign = encodePassword(signStr, "md5");
			System.out.println(headEle.elementText("Signature"));
			System.out.println("new is :" + sign);
			//验签
			if(sign.equals(headEle.elementText("Signature")))
			{
				//验签成功
				//获取处理结果000000为成功
				String rspCode = headEle.elementText("RspCode");
				if("000000".equals(rspCode))
				{
					return true;
				}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
		return false;
	}
	
	/**
	 * 环迅支付根据商户号获取key
	 * 
	 * @param merId
	 * @return
	 */
	private static String getKeyByMerId(String merId) {
		Map<String, String> map = new HashMap<String, String>();
		//Modify By WuCF 20160714 新增商户号
		map.put("183971", "HOpslHd8fctJhZFueaUNEyZ4wWqFDkQaZNcF9BPRZCANvBLXPyBwlD8JY5p77KGhw9zAt1P9Jj8Sk8bUsUdHVe3RpMDZswH5yZLdF2hoTHH1VNduJpekLUxxNFs4T7l3");//环迅支付
		map.put("192349", "RxPDshaTammFADzFzCozpMHWveTnGpLC0FrdDTm0oeouRsufhoRHx6vn1TuHwdezMt69H1l5OtEye8SmtJEtEPLTT1VuNJAHB88b8Qbo66HUe1Ux4zGTGSWFX2ovRjkQ");//环迅支付 容元堂
		return map.get(merId);
	}
	
	/**
	 * 用指定的加密算法加密String Encode a string using algorithm specified in web.xml and return the resulting encrypted password. If exception, the plain credentials string is returned
	 * 
	 * @param password Password or other credentials to use in authenticating this username
	 * @param algorithm Algorithm used to do the digest
	 * 
	 * @return encypted password based on the algorithm.
	 */
	public static String encodePassword(String password, String algorithm) {
		byte[] unencodedPassword= null;
		try {
			unencodedPassword = password.getBytes("utf-8");
		} catch (UnsupportedEncodingException e1) {
			return null;
		}

		MessageDigest md = null;

		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			return password;
		}

		md.reset();

		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);

		// now calculate the hash
		byte[] encodedPassword = md.digest();

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}

		return buf.toString();
	}
	
	public static void main(String[] args) {

		/*String s = "CN43019748,1,yspay,1,cqpy,false,0.00";
		//s = "[{\"dataType\":\"1\",\"isFull\":false,\"merchantId\":\"\",\"payFee\":0,\"payType\":\"0\",\"userCode\":\"CN19147464\",\"zmType\":\"kqpay\"}]";
		System.out.println(PayUtils.getRemarkBean(s).getUserCode());
		;*/
		System.out.println(1);

	}
}
