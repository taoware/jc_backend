package com.irengine.campus.cas.extension.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
/**门店实体类*/
@Entity
@Table(name = "cas_store")
public class Store extends IdEntity{
	private String storeName;//门店名(非空唯一)
	private String summary;//副标题
	private String address;//门店地址(非空)
	private String province;//省份
	private String phone;//联系方式(非空)
	private String introduction;//门店简介
	private UploadedFile photo;//大图url
	private Date createTime;//创建时间
	private Date updateTime;//修改时间
	private Date deleteTime;//删除时间
	private String type;//类型:slideshow or listshow
	private Set<UploadedFile> files=new HashSet<UploadedFile>();//对应门店图片信息
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Transient
	public Set<UploadedFile> getFiles() {
		return files;
	}
	public void setFiles(Set<UploadedFile> files) {
		this.files = files;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	@Transient
	public UploadedFile getPhoto() {
		return photo;
	}
	public void setPhoto(UploadedFile photo) {
		this.photo = photo;
	}
	@Column(unique=true,nullable=false)
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	@Column(nullable=false)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(nullable=false)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
}
