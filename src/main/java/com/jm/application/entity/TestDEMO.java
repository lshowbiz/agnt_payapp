package com.jm.application.entity;

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

/** 
 *Description: <类功能描述>. <br>
 *<p>
	<使用说明>
 </p>                          
 */
@Entity
@Table(name ="TESTDEMO")
@Lazy(value=true)
@XmlRootElement
public class TestDEMO implements java.io.Serializable{
	
	private static final long serialVersionUID = -827759092352842181L;


	@Id
	@Column(name="SP_ID")
	@SequenceGenerator(name = "sequenceGenerator", sequenceName = "SEQ_SPRING_DEMO")
	@GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
	private Long id;
	

	@Column(name="SP_NAME")
	private String name;
	
	
	@Column(name="SP_AGE")
	private Integer age;
	
	
	@Column(name="SP_CREATEDATE")
	private Date createDate;
	
	@Column(name="SP_MODIFYDATE")
	private Date modifyDate;

	@XmlElement
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@XmlElement
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
	
	
}


