package com.irengine.campus.cas.extension.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "jc_uploaded_file")
public class UploadedFile extends IdEntity {
	private String entityType;// 文件所属实体类
	private Long entityId;// 文件所属实体类对象的id
	private String name;// 文件名
	private String tName;// 文件缩略图名字
	private Long size;// 文件大小
	private String photoDescription;// 图片说明
	
	public UploadedFile() {
		super();
	}

	public UploadedFile(String entityType, Long entityId, String name,
			String tName, Long size, String photoDescription) {
		super();
		this.entityType = entityType;
		this.entityId = entityId;
		this.name = name;
		this.tName = tName;
		this.size = size;
		this.photoDescription = photoDescription;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	@Transient
	public String getThumbnailUrl() {
		return StringUtils.join(new Object[] { entityType, entityId, tName },
				"/");
	}

	public String getPhotoDescription() {
		return photoDescription;
	}

	public void setPhotoDescription(String photoDescription) {
		this.photoDescription = photoDescription;
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

	@Transient
	public String getUrl() {
		return StringUtils.join(new Object[] { entityType, entityId, name },
				"/");
	}
}
