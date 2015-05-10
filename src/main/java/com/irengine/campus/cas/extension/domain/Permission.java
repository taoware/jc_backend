package com.irengine.campus.cas.extension.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "jc_permission")
public class Permission extends IdEntity{
	private String perName;//权限名
	
	public String getPerName() {
		return perName;
	}
	public void setPerName(String perName) {
		this.perName = perName;
	}
	
}
