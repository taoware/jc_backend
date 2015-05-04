package com.irengine.campus.cas.extension.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**环信*/
@Entity
@Table(name = "cas_im")
public class IM {
	private Long id;
	private String username;//用户名
	private String password;//密码
	
	public IM() {
		super();
	}
	
	public IM(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}