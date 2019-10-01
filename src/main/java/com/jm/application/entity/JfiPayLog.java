package com.jm.application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Lazy;

import com.jm.application.utils.pay.RemarkBean;

@Entity
@Table(name = "JFI_PAY_LOG")
@Lazy(value = true)
@XmlRootElement
public class JfiPayLog implements java.io.Serializable {

	private static final long serialVersionUID = 1572977193211171728L;
	// Fields
	@Id
	@Column(name = "LOG_ID")
	@SequenceGenerator(name = "sequenceGenerator", sequenceName = "SEQ_FI")
	@GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
	private Long logId;
	@Column(name = "COMPANY_CODE")
	private String companyCode;
	@Column(name = "PAY_TYPE")
	private String payType;
	@Column(name = "DEAL_ID")
	private String dealId;
	@Column(name = "FLAG")
	private String flag;
	@Column(name = "USER_CODE")
	private String userCode;
	@Column(name = "MERCHANT_ID")
	private String merchantId;
	@Column(name = "VERSION")
	private String version;
	@Column(name = "SIGN_TYPE")
	private String signType;
	@Column(name = "SIGN")
	private String sign;
	@Column(name = "MERKEY")
	private String merkey;
	@Column(name = "BANK_ID")
	private String bankId;
	@Column(name = "BANK_DEAL_ID")
	private String bankDealId;
	@Column(name = "AMT_TYPE")
	private String amtType;
	@Column(name = "ORDER_ID")
	private String orderId;
	@Column(name = "ORDER_DATE")
	private String orderDate;
	@Column(name = "ORDER_AMOUNT")
	private String orderAmount;
	@Column(name = "FEE")
	private String fee = "0";
	@Column(name = "ORDER_TIME")
	private String orderTime;
	@Column(name = "EXPIRE_TIME")
	private String expireTime;
	@Column(name = "PAY_RESULT")
	private String payResult;
	@Column(name = "ERR_CODE")
	private String errCode;
	@Column(name = "ERR_MSG")
	private String errMsg;
	@Column(name = "REMARK")
	private String remark;
	@Column(name = "RETURN_MSG")
	private String returnMsg;
	@Column(name = "CREATE_TIME")
	private String createTime;
	@Column(name = "URL")
	private String url;
	@Column(name = "URL_REMARK")
	private String urlRemark;
	@Column(name = "INC")
	private String inc;
	@Column(name = "IP")
	private String ip;
	@Column(name = "DATA_TYPE")
	private String dataType;
	
	@Transient
	private RemarkBean remarkBean; //不使用字段
	
	@XmlElement
	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	@XmlElement
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	@XmlElement
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@XmlElement
	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	@XmlElement
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@XmlElement
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@XmlElement
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@XmlElement
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement
	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	@XmlElement
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@XmlElement
	public String getMerkey() {
		return merkey;
	}

	public void setMerkey(String merkey) {
		this.merkey = merkey;
	}

	@XmlElement
	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	@XmlElement
	public String getBankDealId() {
		return bankDealId;
	}

	public void setBankDealId(String bankDealId) {
		this.bankDealId = bankDealId;
	}

	@XmlElement
	public String getAmtType() {
		return amtType;
	}

	public void setAmtType(String amtType) {
		this.amtType = amtType;
	}

	@XmlElement
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@XmlElement
	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	@XmlElement
	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	@XmlElement
	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	@XmlElement
	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	@XmlElement
	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	@XmlElement
	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	@XmlElement
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	@XmlElement
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	@XmlElement
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlElement
	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	@XmlElement
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@XmlElement
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlElement
	public String getUrlRemark() {
		return urlRemark;
	}

	public void setUrlRemark(String urlRemark) {
		this.urlRemark = urlRemark;
	}

	@XmlElement
	public String getInc() {
		return inc;
	}

	public void setInc(String inc) {
		this.inc = inc;
	}

	@XmlElement
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@XmlElement
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public RemarkBean getRemarkBean() {
		return remarkBean;
	}

	public void setRemarkBean(RemarkBean remarkBean) {
		this.remarkBean = remarkBean;
	}

}
