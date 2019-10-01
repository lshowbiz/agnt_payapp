package com.jm.application.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Lazy;

@Entity
@Table(name = "JPO_BANKBOOK_PAY_LIST")
@Lazy(value = true)
@XmlRootElement
public class JpoBankbookPayList {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LIST")
	@SequenceGenerator(name = "SEQ_LIST", sequenceName = "SEQ_LIST", allocationSize = 1)
	@Column(name="ID", unique=true, nullable=false) 
	private Long id;// 主键ID
	@Column(name = "USER_CODE")
	private String userCode;// 用户编号
	@Column(name = "USER_SP_CODE")
	private String userSpCode;// 代付编号 VARCHAR2(20)
	@Column(name = "MEMBER_ORDER_NO")
	private String memberOrderNo;// 订单编号 VARCHAR2(20)
	@Column(name = "ORDER_TYPE")
	private String orderType;// 订单类型 VARCHAR2(2)
	@Column(name = "IN_TYPE")
	private Integer inType;// IN_TYPE 类型 1 第三方支付 2 电子存折支付 3 基金支付 4 电子存折+积分 5
							// 电子存折+基金 6 电子存折+第三方 7 积分 8 充值（电子存折） 9 基金条目存入
							// NUMBER
	@Column(name = "CREATE_TIME")
	private Date createTime;// 记录创建时间 DATE

	@Column(name = "AMOUNT")
	private BigDecimal amount = new BigDecimal(0);// 总金额 NUMBER(18,2)
	@Column(name = "DZ_AMT")
	private BigDecimal dzAmt = new BigDecimal(0);// 电子存折 NUMBER(18,2)
	@Column(name = "JJ_AMT")
	private BigDecimal jjAmt = new BigDecimal(0);// 基金 NUMBER(18,2)
	@Column(name = "JF_AMT")
	private BigDecimal jfAmt = new BigDecimal(0);// 积分 NUMBER(18,2)
	@Column(name = "KQ_AMT")
	private BigDecimal kqAmt;// 快钱 NUMBER(18,2)
	@Column(name = "TEAM_CODE")
	private String teamCode;// 团队编号 VARCHAR2(20)
	@Column(name = "FLAG")
	private Integer flag = 0;// 0 待分派 1 已分派 2 已扣款 3 扣款失败 余额不足 4 扣款失败 账户不存在 5重复订单
								// NUMBER
	@Column(name = "DATA_TYPE")
	private Integer dataType = 1;// 数据来源1：PC，2：手机 VARCHAR2(1)
	@Column(name = "MONEY_TYPE")
	private Integer moneyType;// 资金类别 电子存折支付 NUMBER(4)
	@Column(name = "MONEY_TYPE1")
	private Integer moneyType1;// 资金类别 第三方支付 基金支付 积分 NUMBER(4)
	@Column(name = "MONEY_TYPE2")
	private Integer moneyType2;// MONEY_TYPE2 资金类别 第三方支付手续费 NUMBER(4)
	@Column(name = "DEAL_DATE")
	private Date dealDate;// DEAL_DATE DATE
	@Column(name = "LOG_ID")
	private Long logId;// 支付记录ID
	@Column(name = "PAY_TYPE")
	private String payType;// 支付记录ID
	@Column(name = "FEE")
	private BigDecimal fee;// 手续费
	@Column(name = "NOTES")
	private String notes;// 摘要
	
	@Column(name = "CHECK_USER_CODE")//审核者
	private String checkUserCode;

	@XmlElement
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@XmlElement
	public String getUserSpCode() {
		return userSpCode;
	}

	public void setUserSpCode(String userSpCode) {
		this.userSpCode = userSpCode;
	}

	@XmlElement
	public String getMemberOrderNo() {
		return memberOrderNo;
	}

	public void setMemberOrderNo(String memberOrderNo) {
		this.memberOrderNo = memberOrderNo;
	}

	@XmlElement
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@XmlElement
	public Integer getInType() {
		return inType;
	}

	public void setInType(Integer inType) {
		this.inType = inType;
	}

	@XmlElement
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@XmlElement
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@XmlElement
	public BigDecimal getDzAmt() {
		return dzAmt;
	}

	public void setDzAmt(BigDecimal dzAmt) {
		this.dzAmt = dzAmt;
	}

	@XmlElement
	public BigDecimal getJjAmt() {
		return jjAmt;
	}

	public void setJjAmt(BigDecimal jjAmt) {
		this.jjAmt = jjAmt;
	}

	@XmlElement
	public BigDecimal getJfAmt() {
		return jfAmt;
	}

	public void setJfAmt(BigDecimal jfAmt) {
		this.jfAmt = jfAmt;
	}

	@XmlElement
	public BigDecimal getKqAmt() {
		return kqAmt;
	}

	public void setKqAmt(BigDecimal kqAmt) {
		this.kqAmt = kqAmt;
	}

	@XmlElement
	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	@XmlElement
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@XmlElement
	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	@XmlElement
	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	@XmlElement
	public Integer getMoneyType1() {
		return moneyType1;
	}

	public void setMoneyType1(Integer moneyType1) {
		this.moneyType1 = moneyType1;
	}

	@XmlElement
	public Integer getMoneyType2() {
		return moneyType2;
	}

	public void setMoneyType2(Integer moneyType2) {
		this.moneyType2 = moneyType2;
	}

	@XmlElement
	public Date getDealDate() {
		return dealDate;
	}

	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}

	@XmlElement
	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	@XmlElement
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@XmlElement
	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	@XmlElement
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@XmlElement
	public String getCheckUserCode() {
		return checkUserCode;
	}

	public void setCheckUserCode(String checkUserCode) {
		this.checkUserCode = checkUserCode;
	}
}
