package com.jm.application.utils.pay.channel;

import chinapnr.SecureLink;
import com.alibaba.fastjson.JSON;
import com.huifu.npay.master.domain.CfcaInfoBo;
import com.huifu.npay.master.util.constant.Constants;
import com.huifu.npay.master.util.security.SecurityService;
import com.huifu.saturn.cfca.CFCASignature;
import com.huifu.saturn.cfca.VerifyResult;
import com.jm.application.entity.JfiPayLog;
import com.jm.application.utils.pay.IPayUtil;
import com.jm.application.utils.pay.PayUtils;
import com.jm.application.utils.web.RequestUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ChinapnrUtilMobileImpl implements IPayUtil {

	private final Log log = LogFactory.getLog(ChinapnrUtilMobileImpl.class);

	private final static String pfxFile="key/app/hftxPay/mulan.pfx";
	private final static String cerFile="key/app/hftxPay/CFCA_ACS_TEST_OCA31.cer";
	private final static String password="123456";
	private final static String nPayMerId="100001";
	//private final static String appId="wx2a5538052969956e";

	@Override
	public String[] getConfiguration() {
		return new String[] { "6,汇付天下 ", "130,2", "000000" };
	}

	@Override
	public JfiPayLog getJfiPayLog(HttpServletRequest request, String companyCode) {
		JfiPayLog entity = this.setJfiPayLog(request, companyCode);
		return this.checkJfiPayLog(entity);
	}

	private JfiPayLog setJfiPayLog(HttpServletRequest request, String companyCode) {
		JfiPayLog entity = new JfiPayLog();
		try {

			request.setCharacterEncoding("utf-8");
			// 取得密文
			String sign = request.getParameter("check_value");

			entity.setIp(RequestUtil.getIpAddr(request));
			entity.setInc("0");
			entity.setCompanyCode(companyCode);
			entity.setUrl(request.getRequestURL().toString() + "?" + RequestUtil.paramStr(request));
			log.info(entity.getUrl());
			entity.setUrlRemark("");

			entity.setSign(sign);// 签名信息
			entity.setPayType("6");
		} catch (Exception e) {
			entity = null;
			e.printStackTrace();
		}
		return entity;
	}

	private JfiPayLog checkJfiPayLog(JfiPayLog entity) {
		SimpleDateFormat format = new SimpleDateFormat(datFmt);

		int rtnMsg = 1;
		//加解签证书参数
		CfcaInfoBo cfcaInfoBo = new CfcaInfoBo();
		cfcaInfoBo=setDefaultCfcaInfoBo(cfcaInfoBo);


		VerifyResult verifyResult = CFCASignature.verifyMerSign(cfcaInfoBo.getNpayMerId(), entity.getSign(),
				"utf-8",cfcaInfoBo.getCerFile());

		System.out.println(verifyResult.toString());
		System.out.println("verifyResult" + verifyResult.toString());
		String decrptyContent ="";
		if ("000".equals(verifyResult.getCode())) {
			rtnMsg = 10;
			// 取得base64格式内容
			String content = new String(verifyResult.getContent(),
					Charset.forName("utf-8"));
			log.info("content = " + content);
			// base64格式解码
			 decrptyContent = new String(Base64.decodeBase64(content),
					Charset.forName("utf-8"));
			log.info("decrptyContent = " + decrptyContent);
		} else {
			rtnMsg = 4;// 签名被篡改
		}


		if(decrptyContent!=null&&!"".equals(decrptyContent)){
			Map<String,String> mapResult = JSON.parseObject(decrptyContent, Map.class );
			entity.setMerchantId(mapResult.get(Constants.MER_CUST_ID)); // MerId商户编号
			entity.setDealId(mapResult.get(Constants.PLATFORM_SEQ_ID));// 钱管家交易唯一标识
			entity.setOrderId(mapResult.get(Constants.ORDER_ID));// 订单编号
			entity.setOrderAmount(mapResult.get(Constants.TRANS_AMT));// 订单金额
			entity.setAmtType("01");//
			entity.setPayResult(mapResult.get(Constants.RESP_CODE));// state状态：000000→成功相符；其它→失败；
			entity.setSignType(mapResult.get(Constants.CMD_ID));// 消息类型
			entity.setBankId(mapResult.get(Constants.MER_CUST_ID));// paybank 支付银行
			entity.setErrCode(mapResult.get(Constants.TRANS_STAT)); // interface版本号
			entity.setRemark(mapResult.get(Constants.MER_PRIV)); // 分账明细

			String remark = mapResult.get(Constants.MER_PRIV);
			if(StringUtils.isNotEmpty(remark)){
				entity.setRemarkBean(PayUtils.getRemarkBean(remark));
				entity.setUserCode(entity.getRemarkBean().getUserCode());
				entity.setFlag(entity.getRemarkBean().getPayType());
			}
			if("10".equals(mapResult.get(Constants.PAY_TYPE))){

				entity.setPayType("61");
			}
			if("05".equals(mapResult.get(Constants.PAY_TYPE))){

				entity.setPayType("62");
			}

		}




		entity.setVersion("10"); // interface版本号
		entity.setReturnMsg(String.valueOf(rtnMsg));
		entity.setCreateTime(format.format(new Date()));
		entity.setDataType("2");// 数据来源，1：PC
		return entity;
	}
	private CfcaInfoBo setDefaultCfcaInfoBo(CfcaInfoBo cfcaInfoBo){

		String cerFile = this.getClass().getClassLoader().getResource(ChinapnrUtilMobileImpl.cerFile).getPath().replaceAll("%20", " ");

		String pfxFile = this.getClass().getClassLoader().getResource(ChinapnrUtilMobileImpl.pfxFile).getPath().replaceAll("%20", " ");
		// 解签证书
		cfcaInfoBo.setCerFile(cerFile);
		// 加签证书
		cfcaInfoBo.setPfxFile(pfxFile);
		// 加签密码
		cfcaInfoBo.setPfxFilePwd(ChinapnrUtilMobileImpl.password);
		//商户ID
		cfcaInfoBo.setNpayMerId(ChinapnrUtilMobileImpl.nPayMerId);
		return cfcaInfoBo;
	}


}
