package com.irengine.campus.cas.extension.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**用户存放基础用户数据,用于自动匹配权限和unit*/
@Entity
@Table(name = "jc_base_user")
public class BaseUser extends IdEntity{
	
	private String name;//真实姓名
	private String gender;//性别
	private String category;//类别
	private String position;//职务
	private String location;//地址
	private String address;//详细地址
	private String mobile;//手机号
	private String role;//角色
	private String unit;//unit
	private Long roleId;//角色id
	private Long unitId;//unitId
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(nullable=false,unique=true)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
