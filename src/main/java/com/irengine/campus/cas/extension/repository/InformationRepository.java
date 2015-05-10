/**
 * 
 */
package com.irengine.campus.cas.extension.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.Information;

/**资讯持久层*/
public interface InformationRepository extends CrudRepository<Information,Long>{
	
	/**根据时间查找资讯*/
	@Query("SELECT i FROM Information i WHERE i.updateTime >= :queryTime")
	public List<Information> findLastUpdated(@Param("queryTime") Date queryTime);
	
	/**根据userId查询资讯*/
	@Query("SELECT i FROM Information i WHERE i.userId=:userId")
	public List<Information> findByUserId(@Param("userId") Long userId);
	/**根据type查询资讯*/
	@Query("SELECT i FROM Information i WHERE i.type=:type ORDER BY id")
	public List<Information> findByType(@Param("type") String type);

	@Query("select max(i.id) from Information i")
	public long findMaxId();
}
