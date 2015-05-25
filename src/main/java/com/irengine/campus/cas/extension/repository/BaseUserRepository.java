package com.irengine.campus.cas.extension.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.BaseUser;

public interface BaseUserRepository extends CrudRepository<BaseUser, Long>{

	@Query("select b from BaseUser b where b.name=:name and b.mobile=:mobile")
	public BaseUser findByNameAndMobile(@Param("name") String name,@Param("mobile") String mobile);

}
