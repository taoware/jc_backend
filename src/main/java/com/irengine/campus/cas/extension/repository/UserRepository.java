package com.irengine.campus.cas.extension.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.User;

public interface UserRepository extends CrudRepository<User, Long>{

	@Query("SELECT u FROM User u WHERE u.updatedTime > :queryTime")
	public List<User> findLastUpdated(@Param("queryTime") Date queryTime);
	
	@Query("SELECT u FROM User u INNER JOIN u.devices d WHERE d.machineId = :machineId")
	public User findByMachineId(@Param("machineId") String machineId);

	public User findByCode(String code);
}
