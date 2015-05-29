package com.irengine.campus.cas.extension.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.Square;

public interface SquareRepository extends CrudRepository<Square, Long> {
	@Query("select s from Square s where id=:id")
	Square findById(@Param("id") Long id);

	@Query("select s from Square s where s.user.id=:userId")
	List<Square> findByUserId(@Param("userId") Long userId);

	@Query("Select max(s.id) from Square s")
	Long getMaxId();

	@Query("select s from Square s where s.type=:type")
	List<Square> findByType(@Param("type") String type);

	@Query("select s from Square s where s.user.id=:userId and s.unit.id=:unitId")
	List<Square> findByUserIdAndUnitId(@Param("userId") long userId,
			@Param("unitId") long unitId);

	/** 弊端:不能查找子unit对应的广场数据 */
	@Query("select s from Square s where s.type=:type and s.unit.id=:unitId")
	List<Square> findByUnitIdAndType(@Param("unitId") Long unitId,
			@Param("type") String type);

	/** 解决上述问题 */
	@Query("select s from Square s where s.type=:type and :leftId<=s.unit.left and :rightId>=s.unit.right")
	List<Square> findByUnitIdAndType2(@Param("leftId") Long leftId,
			@Param("rightId") Long rightId, @Param("type") String type);
}
