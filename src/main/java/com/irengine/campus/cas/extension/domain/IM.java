package com.irengine.campus.cas.extension.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/** 环信 */
@Entity
@Table(name = "jc_im")
public class IM extends IdEntity {
	private String username;// 用户名
	private String password;// 密码

	public IM() {
		super();
	}

	public IM(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Column(nullable=false,unique=true)
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
