package com.irengine.campus.cas.extension.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.Square;

public interface SquareRepository extends CrudRepository<Square, Long>{
	@Query("select s from Square s where id=:id")
	List<Square> findById(@Param("id") Long id);
	@Query("select s from Square s where userId=:userId")
	List<Square> findByUserId(@Param("userId") Long userId);
}
