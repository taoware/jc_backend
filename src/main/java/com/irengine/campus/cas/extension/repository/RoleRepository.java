package com.irengine.campus.cas.extension.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.Role;

public interface RoleRepository extends CrudRepository<Role,Long>{

	@Query("select r from Role r where role=:role")
	Role findByName(@Param("role") String role);

}
