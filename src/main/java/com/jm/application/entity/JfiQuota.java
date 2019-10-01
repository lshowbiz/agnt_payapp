package com.jm.application.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Lazy;

@Entity
@Table(name = "JFI_QUOTA")
@Lazy(value = true)
@XmlRootElement
public class JfiQuota implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "QUOTA_ID")
	@SequenceGenerator(name = "sequenceGenerator", sequenceName = "SEQ_FI")
	@GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
	private Long quotaId;
	@Column(name = "VALIDITY_PERIOD")
	private String validityPeriod;
	@Column(name = "ACCOUNT_ID")
	private Long accountId;
	@Column(name = "STATUS")
	private String status;
	@Column(name = "MAX_MONEY")
	private Long maxMoney;
	@Column(name = "OPERATE_NAME")
	private String operateName;
	@Column(name = "OPERATE_TIME")
	private Date operateTime;
	@Column(name = "REMARK")
	private String remark;

	public Long getQuotaId() {
		return quotaId;
	}

	public void setQuotaId(Long quotaId) {
		this.quotaId = quotaId;
	}

	public String getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(Long maxMoney) {
		this.maxMoney = maxMoney;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
