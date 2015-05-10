package com.irengine.campus.cas.extension.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

//@MappedSuperclass
//用在父类上面。当这个类肯定是父类时，加此标注。如果改成@Entity，
//则继承后，多个类继承，只会生成一个表，而不是多个继承，生成多个表。
@MappedSuperclass
public abstract class IdEntity {

	private Long id;

	// @GeneratedValue:主键生成策略
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}