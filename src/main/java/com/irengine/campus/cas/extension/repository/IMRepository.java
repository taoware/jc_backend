package com.irengine.campus.cas.extension.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.IM;

public interface IMRepository extends CrudRepository<IM, Long>{
	@Query("select m from IM m where username=:username")
	List<IM> findByUsername(@Param("username") String username);

}
