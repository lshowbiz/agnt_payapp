package com.jm.application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name="JPO_MEMBER_ORDER_FEE")
@XmlRootElement
public class JpoMemberOrderFee implements Serializable {
    
	private static final long serialVersionUID = 1L;
	private Long mofId;
    private Long moId;
    private BigDecimal fee;
    private String feeType;
    private String detailType;

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jpo_seq")
	@SequenceGenerator(name = "jpo_seq", sequenceName = "SEQ_PO", allocationSize = 1)
	@Column(name="MOF_ID", unique=true, nullable=false, precision=10, scale=0)    
    public Long getMofId() {
        return this.mofId;
    }
    
    public void setMofId(Long mofId) {
        this.mofId = mofId;
    }
    

    
    @Column(name="FEE")
    public BigDecimal getFee() {
        return this.fee;
    }
    
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
    
    @Column(name="FEE_TYPE", nullable=false, length=2)
    public String getFeeType() {
        return this.feeType;
    }
    
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    
    @Column(name="DETAIL_TYPE", nullable=false, length=10)
    public String getDetailType() {
        return this.detailType;
    }
    
    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }
    
    @Column(name="MO_ID")
	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}
    
}
