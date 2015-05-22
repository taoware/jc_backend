package com.irengine.campus.cas.extension.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "jc_group")
public class SimpleIMGroup extends IdEntity{
	
	private String groupId;//组id

	public String getGroupId() {
		return groupId;
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
}
