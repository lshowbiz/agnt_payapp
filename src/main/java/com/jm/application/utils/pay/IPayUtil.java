package com.jm.application.utils.pay;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import com.jm.application.entity.JfiPayLog;

/** 
 * 支付 接口
 * @author lizg
 * date:2015-10-23
 */
public interface IPayUtil {
	
	final String datFmt = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat format = new SimpleDateFormat(datFmt);
	/**
	 * 支付付款通知处理
	 * companyCode 默认="CN"
	 * @param request
	 * @return
	 */
	public JfiPayLog getJfiPayLog(HttpServletRequest request,String companyCode);
	
	/**
	 * 该接口的基础配置为数组为三
	 * ｛"支付平台编码,中文名称[4,宝易互通]{paycompany}"，"支付平台对应的金额类型,手续费编码[120,2]{字典表：fibankbooktemp.moneytype}",，"打印控制台的表示关闭的"｝
	 * @return
	 */
	public String[] getConfiguration();
}
