package com.irengine.campus.cas.extension.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

/** 资讯 */
@Entity
@Table(name = "jc_news")
public class Information extends IdEntity {
	private String summary;// 副标题
	private String title;// 标题(不为空)
	private String address;// 地址
	private Date createTime;// 创建时间
	private Date updateTime;// 修改时间
	private Date deleteTime;// 删除时间
	private String content;// 正文
	private long userId;// 该咨询的发表用户的id
	private String type;// 类型,(slideshow or listshow)(非空)
	private UploadedFile photo;// 大图url
	
	public Information() {
		super();
	}

	public Information(String summary, String title, String address,
			String content, long userId, String type) {
		super();
		this.summary = summary;
		this.title = title;
		this.address = address;
		this.content = content;
		this.userId = userId;
		this.type = type;
	}

	@OneToOne
	@JoinColumn(name = "fileId")
	public UploadedFile getPhoto() {
		return photo;
	}

	public void setPhoto(UploadedFile photo) {
		this.photo = photo;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(length=1500)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Transient
	public String getUrl(){
		return "http://vps1.taoware.com:8080/jc/news/"+getId()+"/send";
	}
	
}
