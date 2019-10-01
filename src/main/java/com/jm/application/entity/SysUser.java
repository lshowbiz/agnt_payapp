package com.jm.application.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Lazy;

@Entity
@Table(name = "JSYS_USER")
@Lazy(value = true)
@XmlRootElement
public class SysUser implements java.io.Serializable {

	private static final long serialVersionUID = -2712440125339453354L;

	@Id
	@Column(name = "USER_CODE")
	private String userCode;
	@Column(name = "USER_NAME")
	protected String userName;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "PASSWORD2")
	private String password2;
	@Column(name = "STATE")
	private String state;
	@Column(name = "COMPANY_CODE")
	private String companyCode;
	@Column(name = "USER_TYPE")
	private String userType;
	@Column(name = "DEF_CHARACTER_CODING")
	private String defCharacterCoding;
	@Column(name = "USER_AREA")
	private String userArea;
	@Column(name = "IP_CHECK")
	private Integer ipCheck;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "LAST_NAME")
	private String lastName;
	@Column(name = "HONOR_STAR")
	private String honorStar;
	@Column(name = "LAST_LOGIN_ERROR_TIME")
	private Date lastLoginErrorTime;
	@Column(name = "FAILURE_TIMES")
	private Integer failureTimes = 0;

	@XmlElement
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@XmlElement
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@XmlElement
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	@XmlElement
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@XmlElement
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	@XmlElement
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@XmlElement
	public String getDefCharacterCoding() {
		return defCharacterCoding;
	}

	public void setDefCharacterCoding(String defCharacterCoding) {
		this.defCharacterCoding = defCharacterCoding;
	}

	@XmlElement
	public String getUserArea() {
		return userArea;
	}

	public void setUserArea(String userArea) {
		this.userArea = userArea;
	}

	@XmlElement
	public Integer getIpCheck() {
		return ipCheck;
	}

	public void setIpCheck(Integer ipCheck) {
		this.ipCheck = ipCheck;
	}

	@XmlElement
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlElement
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@XmlElement
	public String getHonorStar() {
		return honorStar;
	}

	public void setHonorStar(String honorStar) {
		this.honorStar = honorStar;
	}

	@XmlElement
	public Date getLastLoginErrorTime() {
		return lastLoginErrorTime;
	}

	public void setLastLoginErrorTime(Date lastLoginErrorTime) {
		this.lastLoginErrorTime = lastLoginErrorTime;
	}

	@XmlElement
	public Integer getFailureTimes() {
		return failureTimes;
	}

	public void setFailureTimes(Integer failureTimes) {
		this.failureTimes = failureTimes;
	}

}