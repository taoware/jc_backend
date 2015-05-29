package com.irengine.campus.cas.extension.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

/** 广场实体类 */
@Entity
@Table(name = "jc_square")
public class Square extends IdEntity implements Comparable<Square>{
	private String type;// 广场类型:员工,联采,供应
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

	public Square(String type, String information, Unit unit, User user) {
		super();
		this.type = type;
		this.information = information;
		this.unit = unit;
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name = "userId")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name = "unitId")
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name = "squareId")
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

	@Column(nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Transient
	public Long getUserId(){
		return user.getId();
	}
	
	@Transient
	public Long getUnitId(){
		return unit.getId();
	}
	
	@Transient
	public String getSquareName(){
		if(user!=null&&unit!=null){
			return unit.getName()+user.getPosition();
		}else{
			return "";
		}
	}

	@Override
	public int compareTo(Square o) {
	       Square square=(Square)o;
	      return square.getId().compareTo(this.getId());
	}
	
}
