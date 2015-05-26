package com.irengine.campus.cas.extension.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.Unit;

public interface UnitRepository extends CrudRepository<Unit, Long>{
	//查找最上级unit
	@Query("SELECT u FROM Unit u WHERE u.parent = null")
	public Unit findRoot();

	@Query("SELECT u FROM Unit u WHERE u.enable = true order by u.left")
	public List<Unit> findAll();
	//查找所有下级unit
	@Query("SELECT u FROM Unit u WHERE u.enable = true and u.left > :parentLeft and u.right < :parentRight order by u.left")
	public List<Unit> findAllChildren(@Param("parentLeft") Long parentLeft, @Param("parentRight") Long parentRight);

	@Query("SELECT u FROM Unit u WHERE u.enable = true and u.left < :leafLeft and u.right > :leafRight order by u.left")
	public List<Unit> findAllAncestor(@Param("leafLeft") Long leafLeft, @Param("leafRight") Long leafRight);

	@Modifying
	@Query("UPDATE Unit u SET u.left = u.left + 2 WHERE u.left > :parentLeft")
	public void updateLRValue1(@Param("parentLeft") Long parentLeft);
	
	@Modifying
	@Query("UPDATE Unit u SET u.left = :parentLeft WHERE u.left = 0")
	public void updateLRValue2(@Param("parentLeft") Long parentLeft);

	@Modifying
	@Query("UPDATE Unit u SET u.right = u.right + 2 WHERE u.right > :parentLeft")
	public void updateLRValue3(@Param("parentLeft") Long parentLeft);

	@Modifying
	@Query("UPDATE Unit u SET u.right = :parentLeft WHERE u.right = 0")
	public void updateLRValue4(@Param("parentLeft") Long parentLeft);

	@Query("select max(u.id) from Unit u")
	public Long findMaxId();
}
