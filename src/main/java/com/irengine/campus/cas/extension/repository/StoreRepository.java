package com.irengine.campus.cas.extension.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.Store;

public interface StoreRepository  extends CrudRepository<Store, Long>{
	
	@Query("SELECT s FROM Store s WHERE s.type=:type ORDER BY id")
	List<Store> findByType(@Param("type") String type);
	@Query("SELECT s FROM Store s WHERE s.province=:province ORDER BY id")
	List<Store> findByProvince(@Param("province") String province);
	@Query("SELECT s FROM Store s WHERE s.province=:province AND s.type=:type ORDER BY id")
	List<Store> findByProvinceAndType(@Param("province")String province, @Param("type") String type);
	
}
