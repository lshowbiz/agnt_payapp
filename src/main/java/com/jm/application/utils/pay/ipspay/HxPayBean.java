package com.jm.application.utils.pay.ipspay;


public class HxPayBean {

	String merBillNo;
	
	String currencyType;
	
	String amount;
	
	String date;
	
	/**
	 * Y#交易成功；N#交易失败P#交易处理中   
	 */
	String status;
	
	String msg;
	
	/**
	 * 自定义参数
	 */
	String attach;
	
	/**
	 * ips唯一标识订单号，对本系统无用
	 */
	String ipsBillNo;
	
	/**交易流水号
	 * 
	 */
	String ipsTradeNo;
	
	/**
	 * 银行流水号，银行订单号
	 */
	String bankBillNo;
	
	/**
	 * 17#Md5 数字签名方式
	 */
	String retEncodeType;
	
	String resultType;
	
	String ipsBillTime;
	
	String referenceID;
	
	/**
	 * 响应结果，000000表示成功
	 */
	String rspCode;
	
	String rspMsg;
	
	String reqDate;
	
	String rspDate;
	
	/**
	 * 数字签名
	 */
	String signature;

	public String getMerBillNo() {
		return merBillNo;
	}

	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getIpsBillNo() {
		return ipsBillNo;
	}

	public void setIpsBillNo(String ipsBillNo) {
		this.ipsBillNo = ipsBillNo;
	}

	public String getIpsTradeNo() {
		return ipsTradeNo;
	}

	public void setIpsTradeNo(String ipsTradeNo) {
		this.ipsTradeNo = ipsTradeNo;
	}

	public String getBankBillNo() {
		return bankBillNo;
	}

	public void setBankBillNo(String bankBillNo) {
		this.bankBillNo = bankBillNo;
	}

	public String getRetEncodeType() {
		return retEncodeType;
	}

	public void setRetEncodeType(String retEncodeType) {
		this.retEncodeType = retEncodeType;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getIpsBillTime() {
		return ipsBillTime;
	}

	public void setIpsBillTime(String ipsBillTime) {
		this.ipsBillTime = ipsBillTime;
	}

	public String getReferenceID() {
		return referenceID;
	}

	public void setReferenceID(String referenceID) {
		this.referenceID = referenceID;
	}

	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}

	public String getReqDate() {
		return reqDate;
	}

	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}

	public String getRspDate() {
		return rspDate;
	}

	public void setRspDate(String rspDate) {
		this.rspDate = rspDate;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	
		
}
