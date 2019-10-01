package com.jm.application.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Lazy;

@Entity
@Table(name = "JFI_AMOUNT_DETAIL")
@Lazy(value = true)
@XmlRootElement
public class JfiAmountDetail implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "sequenceGenerator", sequenceName = "SEQ_FI")
	@GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(name = "QUOTA_ID")
	private Long quotaId;
	@Column(name = "MEMBER_ORDER_NO")
	private String memberOrderNo;
	@Column(name = "MONEY")
	private String money;
	@Column(name = "USER_CODE")
	private String userCode;
	@Column(name = "CREATE_TIME")
	private Date createTime;
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "QUOTA_ID", insertable = false, updatable = false, nullable = false)
	@Lazy(value = false)
	private JfiQuota jfiQuota = new JfiQuota();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuotaId() {
		return quotaId;
	}

	public void setQuotaId(Long quotaId) {
		this.quotaId = quotaId;
	}

	public String getMemberOrderNo() {
		return memberOrderNo;
	}

	public void setMemberOrderNo(String memberOrderNo) {
		this.memberOrderNo = memberOrderNo;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public JfiQuota getJfiQuota() {
		return jfiQuota;
	}

	public void setJfiQuota(JfiQuota jfiQuota) {
		this.jfiQuota = jfiQuota;
	}

}
