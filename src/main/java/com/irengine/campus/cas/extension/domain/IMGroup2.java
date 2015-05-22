package com.irengine.campus.cas.extension.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IMGroup2 {
	private String name;//组名
	private String description;//组描述
	private int maxusers;//最大成员数
	private int affiliations_count;//成员数
	private List<Member> affiliations=new ArrayList<Member>();//成员
	private User owner;//群主(User类)
	private List<User> members=new ArrayList<User>();//成员(User类)

	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public List<User> getMembers() {
		return members;
	}
	public void setMembers(List<User> members) {
		this.members = members;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getMaxusers() {
		return maxusers;
	}
	public void setMaxusers(int maxusers) {
		this.maxusers = maxusers;
	}
	public int getAffiliations_count() {
		return affiliations_count;
	}
	public void setAffiliations_count(int affiliations_count) {
		this.affiliations_count = affiliations_count;
	}
	@JsonIgnore
	public List<Member> getAffiliations() {
		return affiliations;
	}
	public void setAffiliations(List<Member> affiliations) {
		this.affiliations = affiliations;
	}
	@Override
	public String toString() {
		return "IMGroup2 [name=" + name + ", description=" + description
				+ ", maxusers=" + maxusers + ", affiliations_count="
				+ affiliations_count + ", affiliations=" + affiliations + "]";
	}
	
}
