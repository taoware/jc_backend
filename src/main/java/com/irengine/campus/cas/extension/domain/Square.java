package com.irengine.campus.cas.extension.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** 广场实体类 */
@Entity
@Table(name = "cas_square")
public class Square {
	private Long id;// squareId
	private Long unitId;//unitId
	private Long userId;//userId(手动导入)
	private String type;//广场类型:员工,联采,供应
	private String information;// 信息
	private Date createTime;// 创建时间
	private Date updateTime;// 修改时间
	private Date deleteTime;// 删除时间
	private Set<UploadedFile> photos;// 对应的图片(多张)
	private Unit unit;
	private User user;
	
	public Square() {
		super();
	}

	public Square(Long unitId, Long userId, String type, String information) {
		super();
		this.unitId = unitId;
		this.userId = userId;
		this.type = type;
		this.information = information;
	}

	@ManyToOne
	@JoinColumn(name="userId",insertable = false, updatable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@JsonIgnore
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	
	@ManyToOne
	@JoinColumn(name="unitId",insertable = false, updatable = false)
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Transient
	public Set<UploadedFile> getPhotos() {
		return photos;
	}

	public void setPhotos(Set<UploadedFile> photos) {
		this.photos = photos;
	}

	@Column(nullable = false)
	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
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

	@Column(nullable=false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
