/**
 * =================================================================
 * 版权所有 2011-2020 泰海网络支付服务有限公司，并保留所有权利
 * -----------------------------------------------------------------
 * 这不是一个自由软件！您不能在任何未经允许的前提下对程序代码进行修改和使用；
 * 不允许对程序代码以任何形式任何目的的再发布
 * =================================================================
 */
package com.jm.application.utils.pay.sypay;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jm.application.utils.pay.MD5Util;

/**
 * 类说明：<br>
 * 签名工具类
 * 
 * <p>
 * 详细描述：<br>
 * 
 * 
 * </p>
 * 
 * @author 顺银收单开发组
 * 
 * CreateDate: 2013-7-22
 */
public final class SignatureUtil {	
	private final static Log log = LogFactory.getLog(SignatureUtil.class);
	/**
	 * 方法说明：
	 * 签名
	 * 1.对请求报文排序 对请求参数数组里的每一个值从 a 到 z 的顺序排序，若遇到相同首字母，则看第二个字母，以此类推
	 * 2.排序完成之后，再用&拼接key1=value1&key2=value2
	 * 3.把私钥进行MD5加密后拼接到待签名字符串后面（连接符&）形成新的字符串
	 * 4.利用 MD5 的签名函数对这个新的字符串进行签名运算，从而得到 32位签名结果字符串
	 *   md5(key1=value1&key2=&key3=value3&md5(secretKey))
	 * @param meta 
	 * @param merKey 私钥
	 * @return
	 */
	public static String sign(StringBuffer meta, String merKey) {
		if(null == meta || meta.length() == 0) {
			return null;
		}
		String item = null;
		String key = null;
		String val = null;
		int pos = -1;
		List<String> keys = new ArrayList<String>();
		Map<String, String> vals = new HashMap<String, String>(30);
		StringTokenizer token = new StringTokenizer(meta.toString(), "&");
		while(token.hasMoreTokens()) {
			item = token.nextToken();
			pos = item.indexOf('=');
			if(pos == -1) {
				continue;
			}
			key = item.substring(0, pos);			
			val = item.substring(pos+1);
			if(null==val || val.length()==0) {//空值不参与签名
				continue;
			}
			keys.add(key);
			vals.put(key, val);
			key = null;
			val = null;
		}
		if(keys.size() == 0) {
			log.info("待签名字符串不合法["+meta+"].");
			return null;
		}
		Collections.sort(keys);
		StringBuffer rs = new StringBuffer();
		for(Iterator<String> it = keys.iterator(); it.hasNext(); ) {
			String ky = it.next();
			rs.append("&").append(ky).append("=").append(vals.get(ky));
		}
		
		String plain="";
		try {
			plain = rs.append("&").append(MD5Util.md5Hex(merKey.getBytes("UTF-8"))).substring(1);//对密钥先md5后拼接到最后，再整体md5
			return MD5Util.md5Hex(plain.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {				
			e.printStackTrace();
			return null;
		}	
	}
	
	public static String innerSign(StringBuffer meta, String merKey) {
		if(null == meta || meta.length() == 0) {
			return null;
		}
		String item = null;
		String key = null;
		String val = null;
		int pos = -1;
		List<String> keys = new ArrayList<String>();
		Map<String, String> vals = new HashMap<String, String>(30);
		StringTokenizer token = new StringTokenizer(meta.toString(), "&");
		while(token.hasMoreTokens()) {
			item = token.nextToken();
			pos = item.indexOf('=');
			if(pos == -1) {
				continue;
			}
			key = item.substring(0, pos);			
			val = item.substring(pos+1);
			if(null==val || val.length()==0) {//空值不参与签名
				continue;
			}
			keys.add(key);
			vals.put(key, val);
			key = null;
			val = null;
		}
		if(keys.size() == 0) {
			log.info("待签名字符串不合法["+meta+"].");
			return null;
		}
		Collections.sort(keys);
		StringBuffer rs = new StringBuffer();
		for(Iterator<String> it = keys.iterator(); it.hasNext(); ) {
			String ky = it.next();
			rs.append("&").append(ky).append("=").append(vals.get(ky));
		}
		
		String plain="";
		try {
			plain = rs.append("&").append(merKey).substring(1);//对密钥先md5后拼接到最后，再整体md5
			return MD5Util.md5Hex(plain.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {				
			e.printStackTrace();
			return null;
		}	
	}
		
}
