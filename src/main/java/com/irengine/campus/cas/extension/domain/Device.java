package com.irengine.campus.cas.extension.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

//Device:设备?
@Entity
@Table(name = "cas_devices")
public class Device extends IdEntity {

	private String code;
	
	private String machineId;//注册码?
	
	private User user;//用户信息
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(unique=true, nullable=false)
	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	
	//@JsonIgnore:这个属性不进行json的转化，忽略这个属性的json转化。
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
