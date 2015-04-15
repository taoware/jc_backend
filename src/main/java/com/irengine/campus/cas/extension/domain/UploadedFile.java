package com.irengine.campus.cas.extension.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name="ss_uploaded_file")
public class UploadedFile {
	private Long fileId;//文件id
	private String entityType;//文件所属实体类
	private Long entityId;//文件所属实体类对象的id
	private String name;//文件url
	private Long size;//文件大小
	private String type;//类别(缩略图,大图)(smail,large)
	private String photoDescription;//图片说明
	private String thumbnailUrl;//对应的小图url
	@Transient
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getPhotoDescription() {
		return photoDescription;
	}
	public void setPhotoDescription(String photoDescription) {
		this.photoDescription = photoDescription;
	}
	@Column(nullable=false)
	@JsonIgnore
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Id
	@GeneratedValue
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	@Column(nullable = false)
	@JsonIgnore
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public Long getEntityId() {
		return entityId;
	}
	@Column(nullable = false)
	@JsonIgnore
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	@Column(nullable = false, unique = true)
	@JsonIgnore
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(nullable = false)
	@JsonIgnore
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	//@Transient表示该属性并非一个到数据库表的字段的映射,ORM框架将忽略该属性.
	@Transient
	public String getUrl() {
		return StringUtils.join(new Object[] {entityType, entityId, name}, "/");
	}
}
