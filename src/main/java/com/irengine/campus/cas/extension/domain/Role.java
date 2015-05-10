package com.irengine.campus.cas.extension.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "jc_role")
public class Role extends IdEntity{
	private String role;//角色名
	private Set<Permission> permissions=new HashSet<Permission>();
	
	@Column(nullable=false,unique=true)
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            targetEntity = Permission.class,
            fetch = FetchType.EAGER
        )
    @JoinTable(
            name="jc_role_permission",
            joinColumns=@JoinColumn(name="userId"),
            inverseJoinColumns=@JoinColumn(name="perId")
        )
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
}
