package com.irengine.campus.cas.extension.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
//组织节点
@Entity
@Table(name = "cas_units")
public class Unit extends IdEntity {

	private Set<Unit> children = new HashSet<Unit>();
	
	private Unit parent;

	private Long left = 0L;
	
	private Long right = 0L;
	
	private String name;
	
	private String uriName;
	
	private String Category;//分类
	
	private Set<User> users = new HashSet<User>();
		
	private boolean enable;
	
	public Unit() {
		this.enable = true;
	}
	
	public Unit(String name) {
		this.name = name;
		this.uriName = name;
		this.enable = true;
	}

	//cascade:级联操作设置
	@JsonIgnore
	@OneToMany(targetEntity=Unit.class, cascade=CascadeType.ALL, mappedBy="parent")
	public Set<Unit> getChildren() {
		return children;
	}

	protected void setChildren(Set<Unit> children) {
		this.children = children;
	}

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="parentId")
	public Unit getParent() {
		return parent;
	}

	public void setParent(Unit parent) {
		this.parent = parent;
	}

	@JsonIgnore
	@Column(name="leftId", nullable=false)
	public Long getLeft() {
		return left;
	}

	public void setLeft(Long left) {
		this.left = left;
	}

	@JsonIgnore
	@Column(name="rightId", nullable=false)
	public Long getRight() {
		return right;
	}

	public void setRight(Long right) {
		this.right = right;
	}

	@Column(nullable=false)
	public String getName() {
		return name;
	}
/*设置上级组织Name改名*/
	public void setName(String name) {
		this.name = name;
		
		if (null == parent)
			setUriName(name);
		else
			setUriName(parent.getUriName() + "-" + name);
	}

	@Column(unique=true, nullable=false)
	public String getUriName() {
		return uriName;
	}

	private void setUriName(String uriName) {
		this.uriName = uriName;
		
		for (Unit u : this.children) {
			u.setUriName(uriName + "-" + u.getName());
		}
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	@JsonIgnore
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "units",
            targetEntity = User.class
        )
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Column(nullable=false)
	public boolean getEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	/*
	 * add child
	 */
	public void addChild(Unit unit)
	{
		unit.setUriName(this.getUriName() + "-" + unit.getName());
		this.children.add(unit);
		unit.setParent(this);
	}
	
	/*
	 * remove child
	 */
	public void removeChild(Unit unit)
	{
		this.children.remove(unit);
		unit.setParent(null);
	}

}
