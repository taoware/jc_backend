package com.irengine.campus.cas.extension.domain;

import java.util.ArrayList;
import java.util.List;

/** 环信建组信息 */
public class IMGroup {
	private String groupname;// 群组名称
	private String desc;// 群组描述
	private boolean pub;// 是否是公开群
	private Integer maxusers;// 群组成员最大数(包括群主)
	private boolean approval;// 加入公开群是否需要批准
	private Long userId;// 群组的管理员的userId
	private String memberIds;// 群组成员
	private User master;//群主
	private List<User> members=new ArrayList<User>();//群成员
	
	public Integer getMaxusers() {
		return maxusers;
	}

	public void setMaxusers(Integer maxusers) {
		this.maxusers = maxusers;
	}

	public User getMaster() {
		return master;
	}

	public void setMaster(User master) {
		this.master = master;
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isPub() {
		return pub;
	}

	public void setPub(boolean pub) {
		this.pub = pub;
	}

	public boolean isApproval() {
		return approval;
	}

	public void setApproval(boolean approval) {
		this.approval = approval;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}

}
