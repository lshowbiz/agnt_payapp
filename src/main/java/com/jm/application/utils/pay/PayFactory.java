package com.jm.application.utils.pay;

import com.jm.application.utils.pay.bill99.Bill99UtilImpl;
import com.jm.application.utils.pay.bill99.Bill99UtilMobileImpl;
import com.jm.application.utils.pay.channel.ChinapnrUtilImpl;
import com.jm.application.utils.pay.channel.ChinapnrUtilMobileImpl;
import com.jm.application.utils.pay.ipspay.IpsPayUtilImpl;
import com.jm.application.utils.pay.sypay.SypayUtilImpl;
import com.jm.application.utils.pay.umbpay.UmbpayUtilImpl;
import com.jm.application.utils.pay.ybpay.YeePayUtilImpl;
import com.jm.application.utils.pay.yspay.YspayUtilImpl;
/**
 * 第三方支付工厂
 * @author Administrator
 *
 */
public class PayFactory {

	public static IPayUtil payFirm(String payType) throws Exception {
		if(payType==null){
			payType = "";
		}
		
		if (payType.contains("sypay")) {//顺首付
			return new SypayUtilImpl();
		}
		if (payType.contains("yspay")) {//银盛
			return new YspayUtilImpl();
		}
		if (payType.contains("ybpay")) {//易宝
			return new YeePayUtilImpl();
		}
		if (payType.contains("umbpay")) {//宝义互通
			return new UmbpayUtilImpl();
		}
		if ("kqpay".equals(payType)) {//快钱
			return new Bill99UtilImpl();
		}
		if ("kqpayMobile".equals(payType)) {//手机端快钱
			return new Bill99UtilMobileImpl();
		}
		if (payType.contains("hfpay")) {//汇付天下
			return new ChinapnrUtilImpl();
		}
		//汇付移动端
		if (payType.contains("hfmobile")) {//汇付天下
			return new ChinapnrUtilMobileImpl();
		}
		if (payType.contains("ipspay")) {//环迅支付  20161122 Add By WuCF
			return new IpsPayUtilImpl();
		}
		return null;
	}
	
	
 	
}
