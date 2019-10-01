package com.jm.application.entity;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "JPO_MEMBER_ORDER")
@XmlRootElement
public class JpoMemberOrder implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "MO_ID")
	@SequenceGenerator(name = "sequenceGenerator", sequenceName = "SEQ_PO")
	@GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
	private Long moId;
	@Column(name = "MEMBER_ORDER_NO")
    private String memberOrderNo;
	@Column(name = "AMOUNT")
    private BigDecimal amount;
	@Column(name = "AMOUNT2")
    private BigDecimal amount2;
	@Column(name = "DISCOUNT_AMOUNT")
    private BigDecimal discountAmount;
	@Column(name = "PAY_BY_JJ")
    private String payByJj;
    @Column(name = "JJ_AMOUNT")
    private BigDecimal jjAmount;
	
    @XmlElement
    public Long getMoId() {
		return moId;
	}
	public void setMoId(Long moId) {
		this.moId = moId;
	}
	
	@XmlElement
	public String getMemberOrderNo() {
		return memberOrderNo;
	}
	public void setMemberOrderNo(String memberOrderNo) {
		this.memberOrderNo = memberOrderNo;
	}
	
	@XmlElement
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@XmlElement
	public BigDecimal getAmount2() {
		return amount2;
	}
	public void setAmount2(BigDecimal amount2) {
		this.amount2 = amount2;
	}
	
	@XmlElement
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	@XmlElement
	public String getPayByJj() {
		return payByJj;
	}
	public void setPayByJj(String payByJj) {
		this.payByJj = payByJj;
	}
	
	@XmlElement
	public BigDecimal getJjAmount() {
		return jjAmount;
	}
	public void setJjAmount(BigDecimal jjAmount) {
		this.jjAmount = jjAmount;
	}
  
}
