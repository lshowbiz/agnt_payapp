package com.jm.application.utils.pay.bill99;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import sun.misc.BASE64Decoder;
@SuppressWarnings("restriction")
public class Pkipair {
	private static final String pubKeyName = "key/99bill[1].cert.rsa.20140728.cer";
	private static final String appKeyName = "key/app/99bill.cert.rsa.20340630.cer";
	
	
	public boolean enCodeByCer(String val, String msg,String payType) {
		boolean flag = false;
		InputStream inStream = null;
		try {
			String fileName = pubKeyName;
			if ("2".equals(payType)) {
				fileName = appKeyName;
			}
			//String file = "D:/cert/" + pubKeyName;
			//inStream = this.getClass().getClassLoader().getResourceAsStream(pubKeyName);
			String file = this.getClass().getClassLoader().getResource(fileName).getPath();//.replaceAll("%20", " ");
			file= URLDecoder.decode(file, "utf-8");
			inStream = new FileInputStream(file);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
			// 获得公钥
			PublicKey pk = cert.getPublicKey();
			// 签名
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(pk);
			signature.update(val.getBytes());
			// 解码
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			//System.out.println(new String(decoder.decodeBuffer(msg)));
			flag = signature.verify(decoder.decodeBuffer(msg));
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * Add By WUCF 20160727  手机端支付验签
	 * @param orgdata
	 * @param sign
	 * @param pubKeyNames
	 * @return
	 */
	public boolean enCodeByCerMobile(String orgdata, String sign, String pubKeyNames) {
		BASE64Decoder decoder = new BASE64Decoder();
		boolean flag = false;
		InputStream inputStream = null;
		try {
			if (Bill99UtilMobileImpl.publicKey == null) {
				String file = this.getClass().getClassLoader().getResource(pubKeyNames).getPath();
				file = URLDecoder.decode(file, "utf-8");
				inputStream = new FileInputStream(new File(file));
				CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");// 初始化密钥
				Certificate certificate = certificateFactory.generateCertificate(inputStream);// 加载公钥
				PublicKey publicKey = certificate.getPublicKey();// 获取公钥
				Bill99UtilMobileImpl.publicKey = publicKey;
				byte[] signData = decoder.decodeBuffer(sign);
				Signature s = Signature.getInstance("SHA1withRSA");
				s.initVerify(publicKey);
				s.update(orgdata.getBytes("utf-8"));
				flag = s.verify(signData);// 验签字符串
			} else {
				byte[] signData;
				signData = decoder.decodeBuffer(sign);
				Signature s = Signature.getInstance("SHA1withRSA");
				s.initVerify(Bill99UtilMobileImpl.publicKey);
				s.update(orgdata.getBytes("utf-8"));
				flag = s.verify(signData);// 验签字符串
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	public static void main(String[] args) {
		String val ="merchantAcctId=1002438213401&version=v2.0&language=1&signType=4&payType=10&bankId=CMB&orderId=CN20160224000030&orderTime=20160224134504&orderAmount=3200&dealId=2155680248&bankDealId=8401512083&dealTime=20160224134632&payAmount=3200&fee=10&ext2=CN18756134,0,0.00&payResult=10";
		String msg = "yR9TkDUMXYV6g3Itbk2ty6ZrQmXRO3pLeVbUtEmJ67o4NfUB3BuxLUdahhlfSN1j0dCyus00cnHj/IPs9KLTGEhyjf71pDcE/LrFox7ZUvAVJ4H4caeMy9Xz+J0liIrJSPW4KYMEiLTpdMtFU9Y+f5icawbf4TVKyVbSOYZIQrvyA+yNaQw6n8VmRIPGWG91tH4+2oNQsgPgzD89HslqNvL5Ecy0z39khxcVjlfPvM8nBA52gLsrYDnS3tDY3jeUNmCqYc0lA4SyTLnR3hjoov7LycNG0rTb24Smi3SlIZvHyvrkQSi5QkwazL9Q1FQPO/AkCB+DdfosieRzfNyy+w==";
		System.out.println(new Pkipair().enCodeByCer(val, msg,"1"));
	}
}
